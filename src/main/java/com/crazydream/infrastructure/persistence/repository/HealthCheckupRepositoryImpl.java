package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.family.model.entity.HealthCheckup;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.repository.HealthCheckupRepository;
import com.crazydream.infrastructure.persistence.convertor.HealthCheckupConvertor;
import com.crazydream.infrastructure.persistence.mapper.HealthCheckupPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.HealthCheckupPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 健康体检仓储实现类
 */
@Repository
public class HealthCheckupRepositoryImpl implements HealthCheckupRepository {
    
    @Autowired
    private HealthCheckupPersistenceMapper healthCheckupPersistenceMapper;
    
    @Override
    public HealthCheckup save(HealthCheckup healthCheckup) {
        HealthCheckupPO po = HealthCheckupConvertor.toPO(healthCheckup);
        
        if (healthCheckup.getId() == null) {
            // 新增
            healthCheckupPersistenceMapper.insert(po);
            healthCheckup.setId(po.getId());
        } else {
            // 更新
            healthCheckupPersistenceMapper.updateById(po);
        }
        
        return healthCheckup;
    }
    
    @Override
    public Optional<HealthCheckup> findById(Long id) {
        HealthCheckupPO po = healthCheckupPersistenceMapper.selectById(id);
        return Optional.ofNullable(HealthCheckupConvertor.toEntity(po));
    }
    
    @Override
    public List<HealthCheckup> findByFamilyMemberId(FamilyMemberId familyMemberId) {
        List<HealthCheckupPO> poList = healthCheckupPersistenceMapper
                .selectByFamilyMemberId(familyMemberId.getValue());
        return poList.stream()
                .map(HealthCheckupConvertor::toEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        healthCheckupPersistenceMapper.deleteById(id);
    }
    
    @Override
    public void deleteByFamilyMemberId(FamilyMemberId familyMemberId) {
        healthCheckupPersistenceMapper.deleteByFamilyMemberId(familyMemberId.getValue());
    }
    
    @Override
    public List<HealthCheckup> findAllWithNextCheckupDate() {
        List<HealthCheckupPO> poList = healthCheckupPersistenceMapper.selectAllWithNextCheckupDate();
        return poList.stream()
                .map(HealthCheckupConvertor::toEntity)
                .collect(Collectors.toList());
    }
}
