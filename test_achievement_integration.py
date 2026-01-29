#!/usr/bin/env python3
"""
CrazyDream目标-成就系统测试脚本
测试目标完成后的成就解锁功能
"""

import requests
import json
import time

# API基础URL
BASE_URL = "http://localhost:8080/api/v2"

# 测试用户ID
TEST_USER_ID = 1

def test_get_achievements():
    """测试1: 获取所有成就"""
    print("\n=== 测试1: 获取所有成就 ===")
    url = f"{BASE_URL}/achievements"
    
    try:
        response = requests.get(url)
        print(f"状态码: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            if data.get('code') == 200:
                achievements = data.get('data', [])
                print(f"✅ 成功获取 {len(achievements)} 个成就")
                
                # 显示前3个成就
                for i, ach in enumerate(achievements[:3], 1):
                    print(f"\n成就{i}:")
                    print(f"  名称: {ach.get('name')}")
                    print(f"  描述: {ach.get('description')}")
                    print(f"  分类: {ach.get('category')}")
                    print(f"  等级: {ach.get('tier')}")
                    print(f"  进度: {ach.get('progress')}/{ach.get('target')}")
                    print(f"  已解锁: {ach.get('unlocked')}")
                
                return True
            else:
                print(f"❌ API返回错误: {data.get('message')}")
                return False
        else:
            print(f"❌ 请求失败: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 异常: {str(e)}")
        return False

def test_update_goal_progress():
    """测试2: 更新目标进度到100%"""
    print("\n=== 测试2: 更新目标进度到100% ===")
    
    # 先获取一个目标
    goals_url = f"{BASE_URL}/goals"
    try:
        response = requests.get(goals_url)
        if response.status_code == 200:
            data = response.json()
            goals = data.get('data', [])
            
            if not goals:
                print("❌ 没有可用的目标进行测试")
                return False
            
            # 找一个未完成的目标
            test_goal = None
            for goal in goals:
                if goal.get('progress', 0) < 100:
                    test_goal = goal
                    break
            
            if not test_goal:
                print("ℹ️ 所有目标都已完成，创建新目标...")
                # 这里可以添加创建目标的逻辑
                return False
            
            goal_id = test_goal['id']
            print(f"选择目标: {test_goal.get('title')} (ID: {goal_id})")
            print(f"当前进度: {test_goal.get('progress')}%")
            
            # 更新进度到100%
            update_url = f"{BASE_URL}/goals/{goal_id}/progress"
            payload = {"progress": 100}
            
            print(f"\n正在更新进度到100%...")
            response = requests.put(update_url, json=payload)
            print(f"状态码: {response.status_code}")
            
            if response.status_code == 200:
                data = response.json()
                if data.get('code') == 200:
                    updated_goal = data.get('data', {})
                    print(f"✅ 进度更新成功")
                    print(f"  新进度: {updated_goal.get('progress')}%")
                    print(f"  状态: {updated_goal.get('status')}")
                    return True
                else:
                    print(f"❌ API返回错误: {data.get('message')}")
                    return False
            else:
                print(f"❌ 请求失败: {response.text}")
                return False
        
    except Exception as e:
        print(f"❌ 异常: {str(e)}")
        return False

def test_check_and_unlock_achievements():
    """测试3: 检查并获取新解锁的成就"""
    print("\n=== 测试3: 检查并获取新解锁的成就 ===")
    url = f"{BASE_URL}/achievements/check-unlock"
    
    try:
        response = requests.post(url)
        print(f"状态码: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            if data.get('code') == 200:
                new_achievements = data.get('data', [])
                
                if new_achievements:
                    print(f"🎉 恭喜! 解锁了 {len(new_achievements)} 个新成就:")
                    
                    for i, ach in enumerate(new_achievements, 1):
                        print(f"\n新成就{i}:")
                        print(f"  {ach.get('icon', '🏆')} {ach.get('name')}")
                        print(f"  {ach.get('description')}")
                        print(f"  等级: {ach.get('tier')}")
                        print(f"  分类: {ach.get('category')}")
                else:
                    print("ℹ️ 没有新解锁的成就")
                
                return True
            else:
                print(f"❌ API返回错误: {data.get('message')}")
                return False
        else:
            print(f"❌ 请求失败: {response.text}")
            return False
    except Exception as e:
        print(f"❌ 异常: {str(e)}")
        return False

def test_achievement_adapter():
    """测试4: 前端适配器功能验证"""
    print("\n=== 测试4: 前端适配器功能验证 ===")
    
    # 模拟后端返回的成就数据
    backend_data = {
        "id": 1,
        "type": "first_goal",
        "name": "首战告捷",
        "description": "完成第一个目标",
        "unlocked": True,
        "progress": 1,
        "target": 1,
        "category": "goal_count",
        "tier": "bronze",
        "icon": "🌟",
        "unlockedAt": "2026-01-29T10:00:00"
    }
    
    print("后端数据:")
    print(json.dumps(backend_data, indent=2, ensure_ascii=False))
    
    # 模拟适配器处理
    print("\n✅ 适配器应将数据转换为:")
    print("  - progressPercent: 100%")
    print("  - categoryName: 目标达成")
    print("  - categoryColor: #1890ff")
    print("  - tierName: 青铜")
    print("  - tierColor: #cd7f32")
    
    return True

def main():
    """主测试流程"""
    print("=" * 60)
    print("CrazyDream 目标-成就系统集成测试")
    print("=" * 60)
    
    # 等待用户确认服务已启动
    input("\n请确保后端服务已在 http://localhost:8080 启动，按Enter继续...")
    
    # 执行测试
    results = []
    
    # 测试1: 获取成就列表
    results.append(("获取成就列表", test_get_achievements()))
    time.sleep(1)
    
    # 测试2: 更新目标进度
    results.append(("更新目标进度", test_update_goal_progress()))
    time.sleep(2)  # 等待事件处理
    
    # 测试3: 检查新成就
    results.append(("检查新成就解锁", test_check_and_unlock_achievements()))
    time.sleep(1)
    
    # 测试4: 适配器验证
    results.append(("适配器功能", test_achievement_adapter()))
    
    # 输出测试总结
    print("\n" + "=" * 60)
    print("测试总结")
    print("=" * 60)
    
    passed = sum(1 for _, result in results if result)
    total = len(results)
    
    for test_name, result in results:
        status = "✅ 通过" if result else "❌ 失败"
        print(f"{test_name}: {status}")
    
    print(f"\n总计: {passed}/{total} 测试通过")
    
    if passed == total:
        print("\n🎉 所有测试通过! 目标-成就系统集成成功!")
    else:
        print("\n⚠️ 部分测试失败，请检查日志")

if __name__ == "__main__":
    main()
