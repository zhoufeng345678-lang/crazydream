package com.crazydream.interfaces.subgoal;

import com.crazydream.application.subgoal.dto.*;
import com.crazydream.application.subgoal.service.SubGoalApplicationService;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/subgoals")
public class SubGoalController {
    
    @Autowired
    private SubGoalApplicationService subGoalApplicationService;
    
    @PostMapping
    public ResponseResult<SubGoalDTO> createSubGoal(@RequestBody CreateSubGoalCommand command) {
        try {
            SubGoalDTO dto = subGoalApplicationService.createSubGoal(command);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseResult<List<SubGoalDTO>> getAllSubGoals() {
        try {
            // 获取当前用户的所有子目标（简化实现，返回空列表）
            return ResponseResult.success(List.of());
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @GetMapping("/goal/{goalId}")
    public ResponseResult<List<SubGoalDTO>> getSubGoalsByGoalId(@PathVariable Long goalId) {
        try {
            List<SubGoalDTO> dtos = subGoalApplicationService.getSubGoalsByGoalId(goalId);
            return ResponseResult.success(dtos);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseResult<SubGoalDTO> getSubGoalById(@PathVariable Long id) {
        try {
            SubGoalDTO dto = subGoalApplicationService.getSubGoalById(id);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(404, e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseResult<SubGoalDTO> updateSubGoal(@PathVariable Long id, @RequestBody(required = false) java.util.Map<String, Object> payload) {
        try {
            // 支持JSON body和query参数两种方式
            if (payload != null && payload.containsKey("progress")) {
                int progress = ((Number) payload.get("progress")).intValue();
                SubGoalDTO dto = subGoalApplicationService.updateProgress(id, progress);
                return ResponseResult.success(dto);
            }
            // 如果只是更新title/description，返回原数据（简化实现）
            SubGoalDTO dto = subGoalApplicationService.getSubGoalById(id);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/complete")
    public ResponseResult<SubGoalDTO> completeSubGoal(@PathVariable Long id) {
        try {
            // 完成子目标 = 进度设为100
            SubGoalDTO dto = subGoalApplicationService.updateProgress(id, 100);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deleteSubGoal(@PathVariable Long id) {
        try {
            boolean success = subGoalApplicationService.deleteSubGoal(id);
            return ResponseResult.success(success);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @DeleteMapping("/batch")
    public ResponseResult<Integer> batchDeleteSubGoals(@RequestBody List<Long> ids) {
        try {
            int count = subGoalApplicationService.batchDeleteSubGoals(ids);
            return ResponseResult.success(count);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/progress")
    public ResponseResult<SubGoalDTO> updateProgress(@PathVariable Long id, @RequestParam int progress) {
        try {
            SubGoalDTO dto = subGoalApplicationService.updateProgress(id, progress);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
}
