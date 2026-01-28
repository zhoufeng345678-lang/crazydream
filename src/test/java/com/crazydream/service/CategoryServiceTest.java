package com.crazydream.service;

import com.crazydream.entity.Category;
import com.crazydream.mapper.CategoryMapper;
import com.crazydream.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    
    @Mock
    private CategoryMapper categoryMapper;
    
    @InjectMocks
    private CategoryServiceImpl categoryService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetAllCategories() {
        // 准备测试数据
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("职业发展");
        categories.add(category1);
        
        // 模拟Mapper行为
        when(categoryMapper.getAllCategories()).thenReturn(categories);
        
        // 执行测试
        List<Category> result = categoryService.getAllCategories();
        
        // 验证结果
        assertEquals(1, result.size());
        assertEquals("职业发展", result.get(0).getName());
        verify(categoryMapper, times(1)).getAllCategories();
    }
    
    @Test
    void testGetCategoryById() {
        // 准备测试数据
        Category category = new Category();
        category.setId(1L);
        category.setName("职业发展");
        
        // 模拟Mapper行为
        when(categoryMapper.getCategoryById(1L)).thenReturn(category);
        
        // 执行测试
        Category result = categoryService.getCategoryById(1L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("职业发展", result.getName());
        verify(categoryMapper, times(1)).getCategoryById(1L);
    }
    
    @Test
    void testCreateCategory() {
        // 准备测试数据
        Category category = new Category();
        category.setName("职业发展");
        category.setId(1L);
        
        // 模拟Mapper行为
        when(categoryMapper.insertCategory(category)).thenReturn(1);
        
        // 执行测试
        Category result = categoryService.createCategory(category);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("职业发展", result.getName());
        verify(categoryMapper, times(1)).insertCategory(category);
    }
    
    @Test
    void testCreateCategoryWithEmptyName() {
        // 准备测试数据
        Category category = new Category();
        category.setName("");
        
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(category);
        });
    }
    
    @Test
    void testUpdateCategory() {
        // 准备测试数据
        Category category = new Category();
        category.setId(1L);
        category.setName("更新后的分类");
        
        // 模拟Mapper行为
        when(categoryMapper.updateCategory(category)).thenReturn(1);
        when(categoryMapper.getCategoryById(1L)).thenReturn(category);
        
        // 执行测试
        Category result = categoryService.updateCategory(category);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("更新后的分类", result.getName());
        verify(categoryMapper, times(1)).updateCategory(category);
    }
    
    @Test
    void testDeleteCategory() {
        // 模拟Mapper行为
        when(categoryMapper.deleteCategory(1L)).thenReturn(1);
        
        // 执行测试
        boolean result = categoryService.deleteCategory(1L);
        
        // 验证结果
        assertTrue(result);
        verify(categoryMapper, times(1)).deleteCategory(1L);
    }
}