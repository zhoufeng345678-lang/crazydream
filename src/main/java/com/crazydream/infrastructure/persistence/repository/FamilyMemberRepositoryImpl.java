package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.repository.FamilyMemberRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.convertor.FamilyMemberConvertor;
import com.crazydream.infrastructure.persistence.mapper.FamilyMemberPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.FamilyMemberPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 家庭成员仓储实现类
 */
@Repository
public class FamilyMemberRepositoryImpl implements FamilyMemberRepository {
    
    @Autowired
    private FamilyMemberPersistenceMapper familyMemberPersistenceMapper;
    
    @Override
    public FamilyMember save(FamilyMember familyMember) {
        FamilyMemberPO po = FamilyMemberConvertor.toPO(familyMember);
        
        if (familyMember.getId() == null) {
            // 新增
            familyMemberPersistenceMapper.insert(po);
            familyMember.setId(FamilyMemberId.of(po.getId()));
        } else {
            // 更新
            familyMemberPersistenceMapper.updateById(po);
        }
        
        return familyMember;
    }
    
    @Override
    public Optional<FamilyMember> findById(FamilyMemberId id) {
        FamilyMemberPO po = familyMemberPersistenceMapper.selectById(id.getValue());
        return Optional.ofNullable(FamilyMemberConvertor.toEntity(po));
    }
    
    @Override
    public List<FamilyMember> findByUserId(UserId userId) {
        List<FamilyMemberPO> poList = familyMemberPersistenceMapper.selectByUserId(userId.getValue());
        return poList.stream()
                .map(FamilyMemberConvertor::toEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(FamilyMemberId id) {
        familyMemberPersistenceMapper.deleteById(id.getValue());
    }
    
    @Override
    public boolean existsById(FamilyMemberId id) {
        return familyMemberPersistenceMapper.countById(id.getValue()) > 0;
    }
    
    @Override
    public List<FamilyMember> findAllWithBirthday() {
        List<FamilyMemberPO> poList = familyMemberPersistenceMapper.selectAllWithBirthday();
        return poList.stream()
                .map(FamilyMemberConvertor::toEntity)
                .collect(Collectors.toList());
    }
}
