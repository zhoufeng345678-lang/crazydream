#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
DDD架构v2 API完整测试脚本
测试所有 /api/v2/* 接口
"""

import requests
import json
from datetime import datetime

class DDDV2APITester:
    def __init__(self, base_url="http://localhost:8080"):
        self.base_url = base_url
        self.results = []
        self.test_data = {}
        
    def log(self, module, api, method, url, http_status, business_code, success, message, response_data=None):
        """记录测试结果（同时检查HTTP状态码和业务状态码）"""
        self.results.append({
            "module": module,
            "api": api,
            "method": method,
            "url": url,
            "http_status": http_status,
            "business_code": business_code,
            "success": success,
            "message": message,
            "response": str(response_data)[:200] if response_data else None,
            "timestamp": datetime.now().isoformat()
        })
        
        status_icon = "✅" if success else "❌"
        print(f"{status_icon} [{module}] {method} {api}")
        print(f"   HTTP: {http_status} | Business: {business_code} | {message}")
        if not success and response_data:
            print(f"   Response: {str(response_data)[:100]}")
        print()
    
    def test_health_check(self):
        """测试健康检查"""
        print("\n" + "="*60)
        print("【健康检查】")
        print("="*60)
        
        try:
            url = f"{self.base_url}/health"
            response = requests.get(url, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "健康检查", "/health", "GET", url,
                response.status_code, data.get("code"),
                success, data.get("message", "")
            )
        except Exception as e:
            self.log("健康检查", "/health", "GET", url, 0, 0, False, str(e))
    
    def test_goal_apis(self):
        """测试目标管理API"""
        print("\n" + "="*60)
        print("【目标管理模块】")
        print("="*60)
        
        # 1. 创建目标
        try:
            url = f"{self.base_url}/api/v2/goals"
            payload = {
                "userId": 1,
                "title": "完整测试目标",
                "description": "用于测试完整CRUD操作",
                "categoryId": 1,
                "priority": "high"
            }
            response = requests.post(url, json=payload, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            if success and data.get("data"):
                self.test_data["goal_id"] = data["data"]["id"]
            
            self.log(
                "目标管理", "创建目标", "POST", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("目标管理", "创建目标", "POST", url, 0, 0, False, str(e))
        
        # 2. 查询目标列表
        try:
            url = f"{self.base_url}/api/v2/goals?userId=1"
            response = requests.get(url, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "目标管理", "查询目标列表", "GET", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("目标管理", "查询目标列表", "GET", url, 0, 0, False, str(e))
        
        # 3. 查询目标详情
        if "goal_id" in self.test_data:
            try:
                goal_id = self.test_data["goal_id"]
                url = f"{self.base_url}/api/v2/goals/{goal_id}"
                response = requests.get(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "目标管理", "查询目标详情", "GET", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("目标管理", "查询目标详情", "GET", url, 0, 0, False, str(e))
        
        # 4. 更新目标
        if "goal_id" in self.test_data:
            try:
                goal_id = self.test_data["goal_id"]
                url = f"{self.base_url}/api/v2/goals/{goal_id}"
                payload = {
                    "title": "更新后的目标",
                    "description": "测试更新功能",
                    "priority": "medium"
                }
                response = requests.put(url, json=payload, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "目标管理", "更新目标", "PUT", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("目标管理", "更新目标", "PUT", url, 0, 0, False, str(e))
        
        # 5. 更新目标进度
        if "goal_id" in self.test_data:
            try:
                goal_id = self.test_data["goal_id"]
                url = f"{self.base_url}/api/v2/goals/{goal_id}/progress?progress=50"
                response = requests.patch(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "目标管理", "更新目标进度", "PATCH", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("目标管理", "更新目标进度", "PATCH", url, 0, 0, False, str(e))
        
        # 6. 删除目标（放在最后）
        if "goal_id" in self.test_data:
            try:
                goal_id = self.test_data["goal_id"]
                url = f"{self.base_url}/api/v2/goals/{goal_id}"
                response = requests.delete(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "目标管理", "删除目标", "DELETE", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("目标管理", "删除目标", "DELETE", url, 0, 0, False, str(e))
    
    def test_subgoal_apis(self):
        """测试子目标管理API"""
        print("\n" + "="*60)
        print("【子目标管理模块】")
        print("="*60)
        
        # 先创建一个目标用于测试
        try:
            url = f"{self.base_url}/api/v2/goals"
            payload = {"userId": 1, "title": "测试目标（用于子目标）", "categoryId": 1}
            response = requests.post(url, json=payload, timeout=5)
            data = response.json()
            if data.get("code") == 200:
                self.test_data["test_goal_id"] = data["data"]["id"]
        except:
            pass
        
        # 1. 创建子目标
        if "test_goal_id" in self.test_data:
            try:
                url = f"{self.base_url}/api/v2/subgoals"
                payload = {
                    "goalId": self.test_data["test_goal_id"],
                    "title": "测试子目标",
                    "description": "子目标描述"
                }
                response = requests.post(url, json=payload, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                if success and data.get("data"):
                    self.test_data["subgoal_id"] = data["data"]["id"]
                
                self.log(
                    "子目标管理", "创建子目标", "POST", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("子目标管理", "创建子目标", "POST", url, 0, 0, False, str(e))
        
        # 2. 查询子目标列表
        if "test_goal_id" in self.test_data:
            try:
                goal_id = self.test_data["test_goal_id"]
                url = f"{self.base_url}/api/v2/subgoals?goalId={goal_id}"
                response = requests.get(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "子目标管理", "查询子目标列表", "GET", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("子目标管理", "查询子目标列表", "GET", url, 0, 0, False, str(e))
        
        # 3. 更新子目标
        if "subgoal_id" in self.test_data:
            try:
                subgoal_id = self.test_data["subgoal_id"]
                url = f"{self.base_url}/api/v2/subgoals/{subgoal_id}"
                payload = {"title": "更新后的子目标", "description": "更新描述"}
                response = requests.put(url, json=payload, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "子目标管理", "更新子目标", "PUT", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("子目标管理", "更新子目标", "PUT", url, 0, 0, False, str(e))
        
        # 4. 完成子目标
        if "subgoal_id" in self.test_data:
            try:
                subgoal_id = self.test_data["subgoal_id"]
                url = f"{self.base_url}/api/v2/subgoals/{subgoal_id}/complete"
                response = requests.patch(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "子目标管理", "完成子目标", "PATCH", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("子目标管理", "完成子目标", "PATCH", url, 0, 0, False, str(e))
        
        # 5. 删除子目标
        if "subgoal_id" in self.test_data:
            try:
                subgoal_id = self.test_data["subgoal_id"]
                url = f"{self.base_url}/api/v2/subgoals/{subgoal_id}"
                response = requests.delete(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "子目标管理", "删除子目标", "DELETE", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("子目标管理", "删除子目标", "DELETE", url, 0, 0, False, str(e))
    
    def test_user_apis(self):
        """测试用户管理API"""
        print("\n" + "="*60)
        print("【用户管理模块】")
        print("="*60)
        
        # 1. 查询用户信息
        try:
            url = f"{self.base_url}/api/v2/users/1"
            response = requests.get(url, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "用户管理", "查询用户信息", "GET", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("用户管理", "查询用户信息", "GET", url, 0, 0, False, str(e))
        
        # 2. 更新用户资料
        try:
            url = f"{self.base_url}/api/v2/users/1"
            payload = {
                "nickName": "更新后的昵称",
                "avatar": "https://example.com/new-avatar.jpg"
            }
            response = requests.put(url, json=payload, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "用户管理", "更新用户资料", "PUT", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("用户管理", "更新用户资料", "PUT", url, 0, 0, False, str(e))
    
    def test_category_apis(self):
        """测试分类管理API"""
        print("\n" + "="*60)
        print("【分类管理模块】")
        print("="*60)
        
        # 1. 查询所有分类
        try:
            url = f"{self.base_url}/api/v2/categories"
            response = requests.get(url, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "分类管理", "查询所有分类", "GET", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("分类管理", "查询所有分类", "GET", url, 0, 0, False, str(e))
        
        # 2. 创建分类
        try:
            url = f"{self.base_url}/api/v2/categories"
            payload = {
                "name": "测试分类",
                "icon": "🧪",
                "colorCode": "#FF5733"
            }
            response = requests.post(url, json=payload, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            if success and data.get("data"):
                self.test_data["category_id"] = data["data"]["id"]
            
            self.log(
                "分类管理", "创建分类", "POST", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("分类管理", "创建分类", "POST", url, 0, 0, False, str(e))
        
        # 3. 更新分类
        if "category_id" in self.test_data:
            try:
                category_id = self.test_data["category_id"]
                url = f"{self.base_url}/api/v2/categories/{category_id}"
                payload = {"name": "更新后的分类", "colorCode": "#00FF00"}
                response = requests.put(url, json=payload, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "分类管理", "更新分类", "PUT", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("分类管理", "更新分类", "PUT", url, 0, 0, False, str(e))
        
        # 4. 删除分类
        if "category_id" in self.test_data:
            try:
                category_id = self.test_data["category_id"]
                url = f"{self.base_url}/api/v2/categories/{category_id}"
                response = requests.delete(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "分类管理", "删除分类", "DELETE", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("分类管理", "删除分类", "DELETE", url, 0, 0, False, str(e))
    
    def test_achievement_apis(self):
        """测试成就管理API"""
        print("\n" + "="*60)
        print("【成就管理模块】")
        print("="*60)
        
        # 1. 查询用户成就
        try:
            url = f"{self.base_url}/api/v2/achievements?userId=1"
            response = requests.get(url, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "成就管理", "查询用户成就", "GET", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("成就管理", "查询用户成就", "GET", url, 0, 0, False, str(e))
        
        # 2. 解锁成就
        try:
            url = f"{self.base_url}/api/v2/achievements/unlock"
            payload = {"userId": 1, "achievementId": 1}
            response = requests.post(url, json=payload, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "成就管理", "解锁成就", "POST", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("成就管理", "解锁成就", "POST", url, 0, 0, False, str(e))
    
    def test_reminder_apis(self):
        """测试提醒管理API"""
        print("\n" + "="*60)
        print("【提醒管理模块】")
        print("="*60)
        
        # 1. 查询用户提醒
        try:
            url = f"{self.base_url}/api/v2/reminders?userId=1"
            response = requests.get(url, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "提醒管理", "查询用户提醒", "GET", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("提醒管理", "查询用户提醒", "GET", url, 0, 0, False, str(e))
        
        # 2. 创建提醒
        try:
            url = f"{self.base_url}/api/v2/reminders"
            payload = {
                "userId": 1,
                "goalId": 1,
                "title": "测试提醒",
                "remindTime": "2026-12-31T23:59:59"
            }
            response = requests.post(url, json=payload, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            if success and data.get("data"):
                self.test_data["reminder_id"] = data["data"]["id"]
            
            self.log(
                "提醒管理", "创建提醒", "POST", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("提醒管理", "创建提醒", "POST", url, 0, 0, False, str(e))
        
        # 3. 标记为已读
        if "reminder_id" in self.test_data:
            try:
                reminder_id = self.test_data["reminder_id"]
                url = f"{self.base_url}/api/v2/reminders/{reminder_id}/read"
                response = requests.patch(url, timeout=5)
                data = response.json()
                
                http_ok = response.status_code == 200
                business_ok = data.get("code") == 200
                success = http_ok and business_ok
                
                self.log(
                    "提醒管理", "标记为已读", "PATCH", url,
                    response.status_code, data.get("code"),
                    success, data.get("message", ""), data
                )
            except Exception as e:
                self.log("提醒管理", "标记为已读", "PATCH", url, 0, 0, False, str(e))
    
    def test_file_apis(self):
        """测试文件管理API"""
        print("\n" + "="*60)
        print("【文件管理模块】")
        print("="*60)
        
        # 注意：文件上传需要multipart/form-data，这里只做基础测试
        try:
            url = f"{self.base_url}/api/v2/files/upload"
            # 创建一个测试文件
            files = {'file': ('test.txt', 'test content', 'text/plain')}
            response = requests.post(url, files=files, timeout=5)
            data = response.json()
            
            http_ok = response.status_code == 200
            business_ok = data.get("code") == 200
            success = http_ok and business_ok
            
            self.log(
                "文件管理", "上传文件", "POST", url,
                response.status_code, data.get("code"),
                success, data.get("message", ""), data
            )
        except Exception as e:
            self.log("文件管理", "上传文件", "POST", url, 0, 0, False, str(e))
    
    def generate_report(self):
        """生成测试报告"""
        print("\n" + "="*60)
        print("【测试报告】")
        print("="*60)
        
        total = len(self.results)
        passed = sum(1 for r in self.results if r["success"])
        failed = total - passed
        pass_rate = (passed / total * 100) if total > 0 else 0
        
        print(f"\n总测试数: {total}")
        print(f"通过: {passed} ✅")
        print(f"失败: {failed} ❌")
        print(f"通过率: {pass_rate:.1f}%")
        
        # 按模块统计
        print("\n【按模块统计】")
        modules = {}
        for r in self.results:
            module = r["module"]
            if module not in modules:
                modules[module] = {"total": 0, "passed": 0}
            modules[module]["total"] += 1
            if r["success"]:
                modules[module]["passed"] += 1
        
        for module, stats in modules.items():
            rate = (stats["passed"] / stats["total"] * 100) if stats["total"] > 0 else 0
            status = "✅" if rate == 100 else "⚠️" if rate >= 50 else "❌"
            print(f"{status} {module}: {stats['passed']}/{stats['total']} ({rate:.0f}%)")
        
        # 失败详情
        failures = [r for r in self.results if not r["success"]]
        if failures:
            print("\n【失败详情】")
            for f in failures:
                print(f"❌ [{f['module']}] {f['method']} {f['api']}")
                print(f"   HTTP: {f['http_status']} | Business: {f['business_code']}")
                print(f"   Message: {f['message']}")
                print()
        
        # 保存JSON报告
        report_file = "v2_api_test_report.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump({
                "summary": {
                    "total": total,
                    "passed": passed,
                    "failed": failed,
                    "pass_rate": f"{pass_rate:.1f}%",
                    "timestamp": datetime.now().isoformat()
                },
                "modules": modules,
                "results": self.results
            }, f, ensure_ascii=False, indent=2)
        
        print(f"\n详细报告已保存至: {report_file}")
    
    def run_all_tests(self):
        """运行所有测试"""
        print("\n" + "="*60)
        print("开始DDD架构v2 API完整测试")
        print("="*60)
        print(f"测试时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"目标服务: {self.base_url}")
        
        self.test_health_check()
        self.test_goal_apis()
        self.test_subgoal_apis()
        self.test_user_apis()
        self.test_category_apis()
        self.test_achievement_apis()
        self.test_reminder_apis()
        self.test_file_apis()
        
        self.generate_report()

if __name__ == "__main__":
    tester = DDDV2APITester()
    tester.run_all_tests()
