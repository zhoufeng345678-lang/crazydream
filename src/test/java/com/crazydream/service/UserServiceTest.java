package com.crazydream.service;

import com.crazydream.entity.User;
import com.crazydream.mapper.UserMapper;
import com.crazydream.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByOpenId() {
        // 准备测试数据
        String openId = "openId123";
        User user = new User();
        user.setId(1L);
        user.setOpenId(openId);
        user.setEmail("user@example.com");

        // 模拟Mapper行为
        when(userMapper.selectByOpenId(openId)).thenReturn(user);

        // 执行测试
        User result = userService.getUserByOpenId(openId);

        // 验证结果
        assertNotNull(result);
        assertEquals(openId, result.getOpenId());
        verify(userMapper, times(1)).selectByOpenId(openId);
    }

    @Test
    void testSaveOrUpdateUser_Update() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setNickName("更新的昵称");

        // 模拟Mapper行为
        when(userMapper.updateByPrimaryKeySelective(user)).thenReturn(1);

        // 执行测试
        User result = userService.saveOrUpdateUser(user);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("更新的昵称", result.getNickName());
        verify(userMapper, times(1)).updateByPrimaryKeySelective(user);
        verify(userMapper, never()).insertSelective(user);
    }

    @Test
    void testSaveOrUpdateUser_Create() {
        // 准备测试数据
        User user = new User();
        user.setEmail("user@example.com");
        user.setNickName("新用户");

        // 模拟Mapper行为
        when(userMapper.insertSelective(user)).thenReturn(1);

        // 执行测试
        User result = userService.saveOrUpdateUser(user);

        // 验证结果
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        assertEquals("新用户", result.getNickName());
        verify(userMapper, times(1)).insertSelective(user);
        verify(userMapper, never()).updateByPrimaryKeySelective(user);
    }

    @Test
    void testGetUserById() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        // 模拟Mapper行为
        when(userMapper.selectByPrimaryKey(1L)).thenReturn(user);

        // 执行测试
        User result = userService.getUserById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user@example.com", result.getEmail());
        verify(userMapper, times(1)).selectByPrimaryKey(1L);
    }

    @Test
    void testGetUserByEmail() {
        // 准备测试数据
        String email = "user@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        // 模拟Mapper行为
        when(userMapper.selectByEmail(email)).thenReturn(user);

        // 执行测试
        User result = userService.getUserByEmail(email);

        // 验证结果
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userMapper, times(1)).selectByEmail(email);
    }

    @Test
    void testRegister_Success() {
        // 准备测试数据
        User user = new User();
        user.setEmail("user@example.com");
        String originalPassword = "password123";
        user.setPassword(originalPassword);
        user.setNickName("新用户");

        // 模拟Mapper行为
        when(userMapper.selectByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(originalPassword)).thenReturn("encodedPassword");
        when(userMapper.insertSelective(user)).thenReturn(1);

        // 执行测试
        User result = userService.register(user);

        // 验证结果
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        assertEquals("新用户", result.getNickName());
        assertNull(result.getPassword());
        verify(userMapper, times(1)).selectByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).encode(originalPassword);
        verify(userMapper, times(1)).insertSelective(user);
    }

    @Test
    void testRegister_WithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(null);
        });
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // 准备测试数据
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password123");

        // 模拟Mapper行为
        when(userMapper.selectByEmail(user.getEmail())).thenReturn(new User());

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });
    }

    @Test
    void testLogin_Success() {
        // 准备测试数据
        String email = "user@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setNickName("用户");

        // 模拟Mapper和PasswordEncoder行为
        when(userMapper.selectByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // 执行测试
        User result = userService.login(email, password);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(email, result.getEmail());
        assertEquals("用户", result.getNickName());
        assertNull(result.getPassword());
        verify(userMapper, times(1)).selectByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void testLogin_WithNullCredentials() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(null, "password123");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login("user@example.com", null);
        });
    }

    @Test
    void testLogin_UserNotFound() {
        // 准备测试数据
        String email = "user@example.com";
        String password = "password123";

        // 模拟Mapper行为
        when(userMapper.selectByEmail(email)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(email, password);
        });
    }

    @Test
    void testLogin_WrongPassword() {
        // 准备测试数据
        String email = "user@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword("encodedPassword");

        // 模拟Mapper和PasswordEncoder行为
        when(userMapper.selectByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(email, password);
        });
    }

    @Test
    void testCreateUser() {
        // 准备测试数据
        User user = new User();
        user.setEmail("user@example.com");
        user.setNickName("新用户");
        user.setId(1L);

        // 模拟Mapper行为
        when(userMapper.insertSelective(user)).thenReturn(1);
        when(userMapper.selectByPrimaryKey(1L)).thenReturn(user);

        // 执行测试
        User result = userService.createUser(user);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user@example.com", result.getEmail());
        verify(userMapper, times(1)).insertSelective(user);
        verify(userMapper, times(1)).selectByPrimaryKey(1L);
    }

    @Test
    void testCreateUser_WithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null);
        });
    }

    @Test
    void testUpdateUser() {
        // 准备测试数据
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("user@example.com");
        existingUser.setNickName("旧昵称");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("user@example.com");
        updatedUser.setNickName("更新的昵称");

        // 模拟Mapper行为
        when(userMapper.selectByPrimaryKey(1L)).thenReturn(existingUser);
        when(userMapper.updateByPrimaryKeySelective(updatedUser)).thenReturn(1);
        when(userMapper.selectByPrimaryKey(1L)).thenReturn(updatedUser);

        // 执行测试
        User result = userService.updateUser(updatedUser);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("更新的昵称", result.getNickName());
        verify(userMapper, times(2)).selectByPrimaryKey(1L);
        verify(userMapper, times(1)).updateByPrimaryKeySelective(updatedUser);
    }

    @Test
    void testUpdateUser_WithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(null);
        });

        // 测试更新用户ID为空
        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(user);
        });
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        // 模拟Mapper行为
        when(userMapper.selectByPrimaryKey(user.getId())).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(user);
        });
    }

    @Test
    void testDeleteUser_Success() {
        // 准备测试数据
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("user@example.com");

        // 模拟Mapper行为
        when(userMapper.selectByPrimaryKey(userId)).thenReturn(existingUser);
        when(userMapper.deleteByPrimaryKey(userId)).thenReturn(1);

        // 执行测试
        boolean result = userService.deleteUser(userId);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).selectByPrimaryKey(userId);
        verify(userMapper, times(1)).deleteByPrimaryKey(userId);
    }

    @Test
    void testDeleteUser_WithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(null);
        });
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // 准备测试数据
        Long userId = 1L;

        // 模拟Mapper行为
        when(userMapper.selectByPrimaryKey(userId)).thenReturn(null);

        // 执行测试
        boolean result = userService.deleteUser(userId);

        // 验证结果
        assertFalse(result);
        verify(userMapper, times(1)).selectByPrimaryKey(userId);
        verify(userMapper, never()).deleteByPrimaryKey(userId);
    }
}