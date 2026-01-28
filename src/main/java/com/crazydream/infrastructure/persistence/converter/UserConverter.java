package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.model.valueobject.*;
import com.crazydream.infrastructure.persistence.po.UserPO;

public class UserConverter {
    
    public static User toDomain(UserPO po) {
        if (po == null) return null;
        
        return User.rebuild(
            UserId.of(po.getId()),
            NickName.of(po.getNickName()),
            Email.of(po.getEmail()),
            po.getPassword(),
            po.getAvatar(),
            Phone.ofNullable(po.getPhone()),
            WechatOpenId.ofNullable(po.getOpenid()),
            Bio.of(po.getBio()),
            UserLevel.fromLevel(po.getLevel()),
            po.getPoints() != null ? po.getPoints() : 0,
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    public static UserPO toPO(User user) {
        if (user == null) return null;
        
        UserPO po = new UserPO();
        if (user.getId() != null) {
            po.setId(user.getId().getValue());
        }
        po.setNickName(user.getNickName().getValue());
        po.setEmail(user.getEmail().getValue());
        po.setPassword(user.getPassword());
        po.setAvatar(user.getAvatar());
        po.setPhone(user.getPhone() != null ? user.getPhone().getValue() : null);
        po.setOpenid(user.getWechatOpenId() != null ? user.getWechatOpenId().getValue() : null);
        po.setBio(user.getBio() != null ? user.getBio().getValue() : null);
        po.setLevel(user.getLevel().getLevel());
        po.setPoints(user.getPoints());
        po.setCreateTime(user.getCreateTime());
        po.setUpdateTime(user.getUpdateTime());
        return po;
    }
}
