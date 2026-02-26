package com.crazydream.interfaces.family;

import com.crazydream.application.family.command.FamilyMemberCreateCmd;
import com.crazydream.application.family.command.FamilyMemberDeleteCmd;
import com.crazydream.application.family.command.FamilyMemberUpdateCmd;
import com.crazydream.application.family.dto.FamilyMemberDTO;
import com.crazydream.application.family.executor.*;
import com.crazydream.application.family.query.FamilyMemberDetailQry;
import com.crazydream.application.family.query.FamilyMemberListQry;
import com.crazydream.utils.ResponseResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 家庭成员管理接口
 */
@RestController
@RequestMapping("/api/v2/family/members")
public class FamilyMemberController {
    
    @Autowired
    private FamilyMemberCreateCmdExe familyMemberCreateCmdExe;
    
    @Autowired
    private FamilyMemberUpdateCmdExe familyMemberUpdateCmdExe;
    
    @Autowired
    private FamilyMemberDeleteCmdExe familyMemberDeleteCmdExe;
    
    @Autowired
    private FamilyMemberListQryExe familyMemberListQryExe;
    
    @Autowired
    private FamilyMemberDetailQryExe familyMemberDetailQryExe;
    
    /**
     * 创建家庭成员
     */
    @PostMapping
    public ResponseResult<FamilyMemberDTO> create(
            @RequestBody @Valid FamilyMemberCreateCmd cmd,
            @RequestAttribute("userId") Long userId) {
        return familyMemberCreateCmdExe.execute(cmd, userId);
    }
    
    /**
     * 更新家庭成员
     */
    @PutMapping("/{id}")
    public ResponseResult<FamilyMemberDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid FamilyMemberUpdateCmd cmd,
            @RequestAttribute("userId") Long userId) {
        cmd.setId(id);
        return familyMemberUpdateCmdExe.execute(cmd, userId);
    }
    
    /**
     * 删除家庭成员
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        FamilyMemberDeleteCmd cmd = new FamilyMemberDeleteCmd();
        cmd.setId(id);
        return familyMemberDeleteCmdExe.execute(cmd, userId);
    }
    
    /**
     * 查询家庭成员列表
     */
    @GetMapping
    public ResponseResult<List<FamilyMemberDTO>> list(@RequestAttribute("userId") Long userId) {
        FamilyMemberListQry qry = new FamilyMemberListQry();
        return familyMemberListQryExe.execute(qry, userId);
    }
    
    /**
     * 查询家庭成员详情
     */
    @GetMapping("/{id}")
    public ResponseResult<FamilyMemberDTO> detail(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        FamilyMemberDetailQry qry = new FamilyMemberDetailQry();
        qry.setId(id);
        return familyMemberDetailQryExe.execute(qry, userId);
    }
}
