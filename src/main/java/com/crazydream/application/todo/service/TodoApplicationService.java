package com.crazydream.application.todo.service;

import com.crazydream.application.todo.assembler.TodoAssembler;
import com.crazydream.application.todo.command.CreateTodoCommand;
import com.crazydream.application.todo.command.CompleteTodoCommand;
import com.crazydream.application.todo.dto.TodoDTO;
import com.crazydream.application.todo.event.TodoCompletedEvent;
import com.crazydream.domain.todo.model.aggregate.Todo;
import com.crazydream.domain.todo.model.valueobject.TodoId;
import com.crazydream.domain.todo.model.valueobject.TodoStatus;
import com.crazydream.domain.todo.repository.TodoRepository;
import com.crazydream.domain.shared.model.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoApplicationService.class);
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public TodoDTO createTodo(CreateTodoCommand command) {
        logger.info("创建待办: userId={}, title={}", command.getUserId(), command.getTitle());
        
        Todo todo = TodoAssembler.toDomain(command);
        todo = todoRepository.save(todo);
        
        return TodoAssembler.toDTO(todo);
    }
    
    @Transactional
    public TodoDTO completeTodo(CompleteTodoCommand command) {
        logger.info("完成待办: todoId={}", command.getId());
        
        Todo todo = todoRepository.findById(TodoId.of(command.getId()))
                .orElseThrow(() -> new IllegalArgumentException("待办不存在: " + command.getId()));
        
        if (!todo.getUserId().getValue().equals(command.getUserId())) {
            throw new IllegalArgumentException("无权限操作此待办");
        }
        
        todo.complete(command.getActualMinutes());
        todo = todoRepository.save(todo);
        
        // 发布事件
        eventPublisher.publishEvent(new TodoCompletedEvent(todo.getId().getValue(), command.getUserId()));
        
        return TodoAssembler.toDTO(todo);
    }
    
    @Transactional
    public TodoDTO startTodo(Long todoId, Long userId) {
        Todo todo = todoRepository.findById(TodoId.of(todoId))
                .orElseThrow(() -> new IllegalArgumentException("待办不存在"));
        
        if (!todo.getUserId().getValue().equals(userId)) {
            throw new IllegalArgumentException("无权限操作此待办");
        }
        
        todo.start();
        todo = todoRepository.save(todo);
        
        return TodoAssembler.toDTO(todo);
    }
    
    @Transactional
    public void deleteTodo(Long todoId, Long userId) {
        Todo todo = todoRepository.findById(TodoId.of(todoId))
                .orElseThrow(() -> new IllegalArgumentException("待办不存在"));
        
        if (!todo.getUserId().getValue().equals(userId)) {
            throw new IllegalArgumentException("无权限删除此待办");
        }
        
        todoRepository.delete(TodoId.of(todoId));
    }
    
    public List<TodoDTO> getTodayTodos(Long userId) {
        List<Todo> todos = todoRepository.findTodayTodos(UserId.of(userId), LocalDate.now());
        return TodoAssembler.toDTOList(todos);
    }
    
    public List<TodoDTO> getUserTodos(Long userId) {
        List<Todo> todos = todoRepository.findByUserId(UserId.of(userId));
        return TodoAssembler.toDTOList(todos);
    }
    
    public List<TodoDTO> getTodosByStatus(Long userId, String status) {
        List<Todo> todos = todoRepository.findByUserIdAndStatus(
            UserId.of(userId), 
            TodoStatus.fromCode(status)
        );
        return TodoAssembler.toDTOList(todos);
    }
}
