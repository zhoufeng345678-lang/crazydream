package com.crazydream.application.user.assembler;

import com.crazydream.application.user.dto.*;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.model.valueobject.*;

public class UserAssembler {
    
    public static User toDomain(RegisterCommand command, String encodedPassword) {
        return User.create(
            Email.of(command.getEmail()),
            encodedPassword,
            NickName.of(command.getNickName())
        );
    }
    
    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        
        UserDTO dto = new UserDTO();
        if (user.getId() != null) {
            dto.setId(user.getId().getValue());
        }
        dto.setNickName(user.getNickName().getValue());
        dto.setEmail(user.getEmail().getValue());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone() != null ? user.getPhone().getValue() : null);
        dto.setWechatOpenId(user.getWechatOpenId() != null ? user.getWechatOpenId().getValue() : null);
        dto.setBio(user.getBio() != null ? user.getBio().getValue() : null);
        dto.setLevel(user.getLevel().getLevel());
        dto.setLevelDescription(user.getLevel().getDescription());
        dto.setPoints(user.getPoints());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        return dto;
    }
}
