package com.crazydream.infrastructure.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.crazydream.config.OssConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * OSS文件上传服务
 */
@Service
public class OssService {
    
    private static final Logger logger = LoggerFactory.getLogger(OssService.class);
    
    // 支持的图片格式
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    
    // 最大文件大小：5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    @Autowired
    private OSS ossClient;
    
    @Autowired
    private OssConfig ossConfig;
    
    /**
     * 上传头像文件到OSS
     * 
     * @param file 上传的文件
     * @param userId 用户ID
     * @return OSS文件URL
     * @throws IllegalArgumentException 如果文件格式不支持或文件过大
     * @throws RuntimeException 如果上传失败
     */
    public String uploadAvatar(MultipartFile file, Long userId) {
        // 验证文件不为空
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制（最大5MB）");
        }
        
        // 验证文件格式
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        String extension = getFileExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件格式，仅支持 JPG、PNG、GIF");
        }
        
        // 生成唯一文件名
        String fileName = generateFileName(userId, extension);
        String objectKey = "avatars/" + userId + "/" + fileName;
        
        try (InputStream inputStream = file.getInputStream()) {
            // 上传到OSS
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                ossConfig.getBucketName(),
                objectKey,
                inputStream
            );
            
            ossClient.putObject(putObjectRequest);
            
            // 为图片格式文件设置公共读权限
            if (ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                try {
                    ossClient.setObjectAcl(
                        ossConfig.getBucketName(),
                        objectKey,
                        CannedAccessControlList.PublicRead
                    );
                    logger.info("设置OSS对象公共读权限成功: objectKey={}", objectKey);
                } catch (Exception aclException) {
                    // ACL设置失败不影响主流程，记录错误日志
                    logger.error("设置OSS对象公共读权限失败: objectKey={}, error={}", 
                        objectKey, aclException.getMessage());
                }
            }
            
            // 返回完整的访问URL（带https协议）
            String fileUrl = "https://" + ossConfig.getDomain() + "/" + objectKey;
            logger.info("文件上传成功: userId={}, objectKey={}, url={}", userId, objectKey, fileUrl);
            
            return fileUrl;
            
        } catch (IOException e) {
            logger.error("读取文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败，请稍后重试");
        } catch (Exception e) {
            logger.error("上传文件到OSS失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败，请稍后重试");
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
    
    /**
     * 生成唯一文件名
     * 格式: {timestamp}-{uuid}.{extension}
     */
    private String generateFileName(Long userId, String extension) {
        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return timestamp + "-" + uuid + "." + extension;
    }
    
    /**
     * 删除OSS文件（可选功能，用于清理旧头像）
     */
    public void deleteFile(String objectKey) {
        try {
            ossClient.deleteObject(ossConfig.getBucketName(), objectKey);
            logger.info("文件删除成功: {}", objectKey);
        } catch (Exception e) {
            logger.error("删除OSS文件失败: {}", e.getMessage(), e);
        }
    }
}
