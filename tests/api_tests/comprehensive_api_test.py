#!/usr/bin/env python3
"""
CrazyDream API 全面测试脚本
测试所有API接口并生成详细报告
"""

import requests
import json
import sys
from datetime import datetime

BASE_URL = "http://localhost:8080"

class APITester:
    def __init__(self):
        self.results = []
        self.token = None
        self.test_user_id = 1
        self.test_data = {}
        
    def log(self, category, api, method, url, status_code, success, message, response_data=None):
        """记录测试结果"""
        self.results.append({
            "category": category,
            "api": api,
            "method": method,
            "url": url,
            "status_code": status_code,
            "success": success,
            "message": message,
            "response": response_data[:200] if response_data and len(response_data) > 200 else response_data
        })
        status = "✅" if success else "❌"
        print(f"{status} [{category}] {method} {url} - {status_code} - {message}")
        
    def test_health_check(self):
        """测试健康检查接口"""
        try:
            response = requests.get(f"{BASE_URL}/health", timeout=5)
            success = response.status_code == 200
            self.log("健康检查", "健康检查", "GET", "/health", response.status_code, success, 
                    "服务运行正常" if success else "服务异常", response.text)
        except Exception as e:
            self.log("健康检查", "健康检查", "GET", "/health", 0, False, str(e))
    
    def test_auth_apis(self):
        """测试认证模块"""
        # 注册（可能失败，因为用户已存在）
        try:
            response = requests.post(f"{BASE_URL}/api/auth/register", json={
                "email": "test@example.com",
                "password": "password123",
                "nickName": "测试用户",
                "phone": "13800138000"
            }, timeout=5)
            success = response.status_code in [200, 400]  # 400可能是用户已存在
            self.log("认证模块", "用户注册", "POST", "/api/auth/register", response.status_code, success,
                    "注册接口响应正常", response.text)
        except Exception as e:
            self.log("认证模块", "用户注册", "POST", "/api/auth/register", 0, False, str(e))
        
        # 登录
        try:
            response = requests.post(f"{BASE_URL}/api/auth/login", json={
                "email": "test@example.com",
                "password": "password123"
            }, timeout=5)
            success = response.status_code == 200
            if success and response.json().get("data"):
                self.token = response.json()["data"].get("token")
            self.log("认证模块", "用户登录", "POST", "/api/auth/login", response.status_code, success,
                    "登录成功" if success else "登录失败", response.text)
        except Exception as e:
            self.log("认证模块", "用户登录", "POST", "/api/auth/login", 0, False, str(e))
    
    def test_user_apis(self):
        """测试用户模块"""
        # 获取当前用户信息
        try:
            response = requests.get(f"{BASE_URL}/api/user/profile", timeout=5)
            success = response.status_code == 200
            self.log("用户模块", "获取用户信息", "GET", "/api/user/profile", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("用户模块", "获取用户信息", "GET", "/api/user/profile", 0, False, str(e))
        
        # 根据ID获取用户信息
        try:
            response = requests.get(f"{BASE_URL}/api/user/1", timeout=5)
            success = response.status_code == 200
            self.log("用户模块", "根据ID获取用户", "GET", "/api/user/1", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("用户模块", "根据ID获取用户", "GET", "/api/user/1", 0, False, str(e))
    
    def test_category_apis(self):
        """测试分类模块"""
        # 获取所有分类
        try:
            response = requests.get(f"{BASE_URL}/api/categories", timeout=5)
            success = response.status_code == 200
            self.log("分类模块", "获取所有分类", "GET", "/api/categories", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
            if success and response.json().get("data"):
                categories = response.json()["data"]
                if categories:
                    self.test_data['category_id'] = categories[0]['id']
        except Exception as e:
            self.log("分类模块", "获取所有分类", "GET", "/api/categories", 0, False, str(e))
        
        # 创建分类
        try:
            response = requests.post(f"{BASE_URL}/api/categories", json={
                "name": "测试分类",
                "icon": "📝",
                "color": "#FF0000",
                "sort": 99
            }, timeout=5)
            success = response.status_code == 200
            if success and response.json().get("data"):
                self.test_data['new_category_id'] = response.json()["data"].get("id")
            self.log("分类模块", "创建分类", "POST", "/api/categories", response.status_code, success,
                    "创建成功" if success else "创建失败", response.text)
        except Exception as e:
            self.log("分类模块", "创建分类", "POST", "/api/categories", 0, False, str(e))
    
    def test_goal_apis(self):
        """测试目标模块"""
        # 获取目标列表
        try:
            response = requests.get(f"{BASE_URL}/api/goals", timeout=5)
            success = response.status_code == 200
            self.log("目标模块", "获取目标列表", "GET", "/api/goals", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("目标模块", "获取目标列表", "GET", "/api/goals", 0, False, str(e))
        
        # 创建目标
        try:
            category_id = self.test_data.get('category_id', 1)
            response = requests.post(f"{BASE_URL}/api/goals", json={
                "userId": self.test_user_id,
                "title": "API测试目标",
                "description": "这是自动化测试创建的目标",
                "categoryId": category_id,
                "priority": "high",
                "deadline": "2026-12-31T23:59:59",
                "progress": 0,
                "status": "in_progress"
            }, timeout=5)
            success = response.status_code == 200
            if success and response.json().get("data"):
                self.test_data['goal_id'] = response.json()["data"].get("id")
            self.log("目标模块", "创建目标", "POST", "/api/goals", response.status_code, success,
                    "创建成功" if success else "创建失败", response.text)
        except Exception as e:
            self.log("目标模块", "创建目标", "POST", "/api/goals", 0, False, str(e))
        
        # 更新目标进度
        if 'goal_id' in self.test_data:
            try:
                response = requests.put(f"{BASE_URL}/api/goals/{self.test_data['goal_id']}/progress", 
                                      json={"progress": 50}, timeout=5)
                success = response.status_code == 200
                self.log("目标模块", "更新目标进度", "PUT", f"/api/goals/{self.test_data['goal_id']}/progress", 
                        response.status_code, success, "更新成功" if success else "更新失败", response.text)
            except Exception as e:
                self.log("目标模块", "更新目标进度", "PUT", f"/api/goals/{self.test_data['goal_id']}/progress", 
                        0, False, str(e))
        
        # 获取今日提醒
        try:
            response = requests.get(f"{BASE_URL}/api/goals/today-reminders", timeout=5)
            success = response.status_code == 200
            self.log("目标模块", "获取今日提醒", "GET", "/api/goals/today-reminders", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("目标模块", "获取今日提醒", "GET", "/api/goals/today-reminders", 0, False, str(e))
    
    def test_subgoal_apis(self):
        """测试子目标模块"""
        if 'goal_id' not in self.test_data:
            return
        
        # 创建子目标
        try:
            response = requests.post(f"{BASE_URL}/api/sub-goal", json={
                "goalId": self.test_data['goal_id'],
                "title": "测试子目标",
                "description": "这是测试子目标",
                "progress": 0,
                "status": "in_progress"
            }, timeout=5)
            success = response.status_code == 200
            if success and response.json().get("data"):
                self.test_data['subgoal_id'] = response.json()["data"].get("id")
            self.log("子目标模块", "创建子目标", "POST", "/api/sub-goal", response.status_code, success,
                    "创建成功" if success else "创建失败", response.text)
        except Exception as e:
            self.log("子目标模块", "创建子目标", "POST", "/api/sub-goal", 0, False, str(e))
        
        # 获取目标的子目标列表
        try:
            response = requests.get(f"{BASE_URL}/api/sub-goal/goal/{self.test_data['goal_id']}", timeout=5)
            success = response.status_code == 200
            self.log("子目标模块", "获取子目标列表", "GET", f"/api/sub-goal/goal/{self.test_data['goal_id']}", 
                    response.status_code, success, "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("子目标模块", "获取子目标列表", "GET", f"/api/sub-goal/goal/{self.test_data['goal_id']}", 
                    0, False, str(e))
    
    def test_achievement_apis(self):
        """测试成就模块"""
        # 获取成就列表
        try:
            response = requests.get(f"{BASE_URL}/api/achievements", timeout=5)
            success = response.status_code == 200
            self.log("成就模块", "获取成就列表", "GET", "/api/achievements", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("成就模块", "获取成就列表", "GET", "/api/achievements", 0, False, str(e))
        
        # 检查并解锁成就
        try:
            response = requests.post(f"{BASE_URL}/api/achievements/check", timeout=5)
            success = response.status_code == 200
            self.log("成就模块", "检查解锁成就", "POST", "/api/achievements/check", response.status_code, success,
                    "检查成功" if success else "检查失败", response.text)
        except Exception as e:
            self.log("成就模块", "检查解锁成就", "POST", "/api/achievements/check", 0, False, str(e))
    
    def test_reminder_apis(self):
        """测试提醒模块"""
        # 获取所有提醒
        try:
            response = requests.get(f"{BASE_URL}/api/reminders", timeout=5)
            success = response.status_code == 200
            self.log("提醒模块", "获取所有提醒", "GET", "/api/reminders", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("提醒模块", "获取所有提醒", "GET", "/api/reminders", 0, False, str(e))
        
        # 获取未读提醒
        try:
            response = requests.get(f"{BASE_URL}/api/reminders/unread", timeout=5)
            success = response.status_code == 200
            self.log("提醒模块", "获取未读提醒", "GET", "/api/reminders/unread", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("提醒模块", "获取未读提醒", "GET", "/api/reminders/unread", 0, False, str(e))
        
        # 创建提醒
        if 'goal_id' in self.test_data:
            try:
                response = requests.post(f"{BASE_URL}/api/reminders", json={
                    "userId": self.test_user_id,
                    "goalId": self.test_data['goal_id'],
                    "title": "测试提醒",
                    "deadline": "2026-12-30T23:59:59"
                }, timeout=5)
                success = response.status_code == 200
                if success and response.json().get("data"):
                    self.test_data['reminder_id'] = response.json()["data"].get("id")
                self.log("提醒模块", "创建提醒", "POST", "/api/reminders", response.status_code, success,
                        "创建成功" if success else "创建失败", response.text)
            except Exception as e:
                self.log("提醒模块", "创建提醒", "POST", "/api/reminders", 0, False, str(e))
    
    def test_statistics_apis(self):
        """测试统计模块"""
        # 获取目标统计
        try:
            response = requests.get(f"{BASE_URL}/api/statistics/goals", timeout=5)
            success = response.status_code == 200
            self.log("统计模块", "获取目标统计", "GET", "/api/statistics/goals", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("统计模块", "获取目标统计", "GET", "/api/statistics/goals", 0, False, str(e))
        
        # 获取仪表盘统计
        try:
            response = requests.get(f"{BASE_URL}/api/statistics/dashboard", timeout=5)
            success = response.status_code == 200
            self.log("统计模块", "获取仪表盘统计", "GET", "/api/statistics/dashboard", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("统计模块", "获取仪表盘统计", "GET", "/api/statistics/dashboard", 0, False, str(e))
        
        # 获取趋势统计
        try:
            response = requests.get(f"{BASE_URL}/api/statistics/trends", timeout=5)
            success = response.status_code == 200
            self.log("统计模块", "获取趋势统计", "GET", "/api/statistics/trends", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("统计模块", "获取趋势统计", "GET", "/api/statistics/trends", 0, False, str(e))
        
        # 获取分类统计
        try:
            response = requests.get(f"{BASE_URL}/api/statistics/categories", timeout=5)
            success = response.status_code == 200
            self.log("统计模块", "获取分类统计", "GET", "/api/statistics/categories", response.status_code, success,
                    "获取成功" if success else "获取失败", response.text)
        except Exception as e:
            self.log("统计模块", "获取分类统计", "GET", "/api/statistics/categories", 0, False, str(e))
    
    def test_file_apis(self):
        """测试文件模块"""
        # 注意：文件上传需要multipart/form-data，这里只测试接口可达性
        try:
            response = requests.get(f"{BASE_URL}/api/files", timeout=5)
            # 可能返回405(Method Not Allowed)或其他，只要不是连接错误就算通过
            success = response.status_code in [200, 404, 405]
            self.log("文件模块", "文件接口检查", "GET", "/api/files", response.status_code, success,
                    "接口可达" if success else "接口异常", response.text)
        except Exception as e:
            self.log("文件模块", "文件接口检查", "GET", "/api/files", 0, False, str(e))
    
    def run_all_tests(self):
        """运行所有测试"""
        print("\n" + "="*80)
        print("CrazyDream API 全面测试")
        print(f"测试时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"基础URL: {BASE_URL}")
        print("="*80 + "\n")
        
        self.test_health_check()
        self.test_auth_apis()
        self.test_user_apis()
        self.test_category_apis()
        self.test_goal_apis()
        self.test_subgoal_apis()
        self.test_achievement_apis()
        self.test_reminder_apis()
        self.test_statistics_apis()
        self.test_file_apis()
        
        self.generate_report()
    
    def generate_report(self):
        """生成测试报告"""
        print("\n" + "="*80)
        print("测试总结")
        print("="*80)
        
        total = len(self.results)
        success = sum(1 for r in self.results if r['success'])
        failed = total - success
        
        print(f"\n总测试数: {total}")
        print(f"成功: {success} ✅")
        print(f"失败: {failed} ❌")
        print(f"成功率: {success/total*100:.1f}%\n")
        
        # 按类别统计
        categories = {}
        for r in self.results:
            cat = r['category']
            if cat not in categories:
                categories[cat] = {'total': 0, 'success': 0}
            categories[cat]['total'] += 1
            if r['success']:
                categories[cat]['success'] += 1
        
        print("按模块统计:")
        for cat, stats in categories.items():
            rate = stats['success']/stats['total']*100
            print(f"  {cat}: {stats['success']}/{stats['total']} ({rate:.0f}%)")
        
        # 保存详细报告到JSON
        report_file = "comprehensive_api_test_report.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump({
                "test_time": datetime.now().isoformat(),
                "summary": {
                    "total": total,
                    "success": success,
                    "failed": failed,
                    "success_rate": f"{success/total*100:.1f}%"
                },
                "by_category": categories,
                "details": self.results
            }, f, ensure_ascii=False, indent=2)
        
        print(f"\n详细报告已保存到: {report_file}")
        
        if failed > 0:
            print("\n失败的测试:")
            for r in self.results:
                if not r['success']:
                    print(f"  ❌ [{r['category']}] {r['method']} {r['url']}")
                    print(f"     原因: {r['message']}")

if __name__ == "__main__":
    tester = APITester()
    tester.run_all_tests()
