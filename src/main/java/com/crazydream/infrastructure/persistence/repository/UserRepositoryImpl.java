package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.model.valueobject.Email;
import com.crazydream.domain.user.model.valueobject.WechatOpenId;
import com.crazydream.domain.user.repository.UserRepository;
import com.crazydream.infrastructure.persistence.converter.UserConverter;
import com.crazydream.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.UserPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    
    @Autowired
    private UserPersistenceMapper mapper;
    
    @Override
    public User save(User user) {
        UserPO po = UserConverter.toPO(user);
        if (user.getId() == null) {
            mapper.insert(po);
            user.setId(UserId.of(po.getId()));
        } else {
            mapper.update(po);
        }
        return user;
    }
    
    @Override
    public Optional<User> findById(UserId id) {
        logger.info("[DEBUG] UserRepository.findById被调用，userId: {}", id.getValue());
        UserPO po = mapper.selectById(id.getValue());
        logger.info("[DEBUG] 查询到的UserPO: id={}, nick_name={}, email={}", 
            po != null ? po.getId() : null,
            po != null ? po.getNickName() : null,
            po != null ? po.getEmail() : null);
        return Optional.ofNullable(UserConverter.toDomain(po));
    }
    
    @Override
    public Optional<User> findByEmail(Email email) {
        UserPO po = mapper.selectByEmail(email.getValue());
        return Optional.ofNullable(UserConverter.toDomain(po));
    }
    
    @Override
    public boolean existsByEmail(Email email) {
        return findByEmail(email).isPresent();
    }
    
    @Override
    public Optional<User> findByWechatOpenId(WechatOpenId wechatOpenId) {
        if (wechatOpenId == null || wechatOpenId.getValue() == null) {
            return Optional.empty();
        }
        try {
            UserPO po = mapper.selectByWechatOpenId(wechatOpenId.getValue());
            return Optional.ofNullable(UserConverter.toDomain(po));
        } catch (Exception e) {
            logger.error("根据微信OpenID查询用户失败", e);
            return Optional.empty();
        }
    }
}
