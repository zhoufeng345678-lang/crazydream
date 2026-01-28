package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPersistenceMapper {
    int insert(UserPO user);
    int update(UserPO user);
    UserPO selectById(Long id);
    UserPO selectByEmail(String email);
    UserPO selectByWechatOpenId(String openid);
}
