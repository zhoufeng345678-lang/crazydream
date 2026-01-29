package com.crazydream.interfaces.todo;

import com.crazydream.application.todo.command.CreateTodoCommand;
import com.crazydream.application.todo.command.CompleteTodoCommand;
import com.crazydream.application.todo.dto.TodoDTO;
import com.crazydream.application.todo.service.TodoApplicationService;
import com.crazydream.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待办控制器
 * Interface层,负责HTTP请求处理
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@RestController
@RequestMapping("/api/v2/todos")
public class TodoController {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
    
    @Autowired
    private TodoApplicationService todoApplicationService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return defaultUserId;
        }
        try {
            return Long.parseLong(authentication.getName());
        } catch (Exception e) {
            return defaultUserId;
        }
    }
    
    @PostMapping
    public ResponseResult<TodoDTO> createTodo(@RequestBody CreateTodoCommand command) {
        try {
            Long userId = getCurrentUserId();
            command.setUserId(userId);
            
            logger.info("创建待办请求: userId={}, title={}", userId, command.getTitle());
            TodoDTO todo = todoApplicationService.createTodo(command);
            
            return ResponseResult.success(todo);
        } catch (Exception e) {
            logger.error("创建待办失败", e);
            return ResponseResult.error("创建待办失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/complete")
    public ResponseResult<TodoDTO> completeTodo(@PathVariable Long id, 
                                                  @RequestBody(required = false) CompleteTodoCommand command) {
        try {
            Long userId = getCurrentUserId();
            if (command == null) {
                command = new CompleteTodoCommand();
            }
            command.setId(id);
            command.setUserId(userId);
            
            TodoDTO todo = todoApplicationService.completeTodo(command);
            return ResponseResult.success(todo);
        } catch (Exception e) {
            logger.error("完成待办失败: id={}", id, e);
            return ResponseResult.error("完成待办失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/start")
    public ResponseResult<TodoDTO> startTodo(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            TodoDTO todo = todoApplicationService.startTodo(id, userId);
            return ResponseResult.success(todo);
        } catch (Exception e) {
            logger.error("开始待办失败: id={}", id, e);
            return ResponseResult.error("开始待办失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteTodo(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            todoApplicationService.deleteTodo(id, userId);
            return ResponseResult.success(null);
        } catch (Exception e) {
            logger.error("删除待办失败: id={}", id, e);
            return ResponseResult.error("删除待办失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/today")
    public ResponseResult<List<TodoDTO>> getTodayTodos() {
        try {
            Long userId = getCurrentUserId();
            List<TodoDTO> todos = todoApplicationService.getTodayTodos(userId);
            return ResponseResult.success(todos);
        } catch (Exception e) {
            logger.error("获取今日待办失败", e);
            return ResponseResult.error("获取今日待办失败: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseResult<List<TodoDTO>> getUserTodos(@RequestParam(required = false) String status) {
        try {
            Long userId = getCurrentUserId();
            List<TodoDTO> todos;
            
            if (status != null && !status.isEmpty()) {
                todos = todoApplicationService.getTodosByStatus(userId, status);
            } else {
                todos = todoApplicationService.getUserTodos(userId);
            }
            
            return ResponseResult.success(todos);
        } catch (Exception e) {
            logger.error("获取待办列表失败", e);
            return ResponseResult.error("获取待办列表失败: " + e.getMessage());
        }
    }
}
