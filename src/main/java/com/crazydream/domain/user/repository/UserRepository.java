package com.crazydream.domain.user.repository;

import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.model.valueobject.Email;
import com.crazydream.domain.user.model.valueobject.WechatOpenId;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    
    /**
     * 根据微信OpenID查询用户
     * @param wechatOpenId 微信OpenID
     * @return 用户Optional
     */
    Optional<User> findByWechatOpenId(WechatOpenId wechatOpenId);
}
