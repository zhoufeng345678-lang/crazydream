#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
微信一键登录接口集成测试脚本
"""

import requests
import json

BASE_URL = "http://localhost:8080"

def test_wechat_login_missing_code():
    """测试缺少code参数"""
    print("\n=== 测试1: 缺少code参数 ===")
    url = f"{BASE_URL}/api/v2/auth/wechat-login"
    response = requests.post(url, json={})
    print(f"状态码: {response.status_code}")
    print(f"响应: {json.dumps(response.json(), ensure_ascii=False, indent=2)}")
    # 项目使用统一响应格式：HTTP 200 + 业务code
    assert response.status_code == 200, "HTTP状态码应该是200"
    assert response.json()["code"] == 400, "业务code应该是400"
    assert "微信授权码不能为空" in response.json()["message"], "应包含正确的错误消息"

def test_wechat_login_empty_code():
    """测试空code参数"""
    print("\n=== 测试2: 空code参数 ===")
    url = f"{BASE_URL}/api/v2/auth/wechat-login"
    response = requests.post(url, json={"code": ""})
    print(f"状态码: {response.status_code}")
    print(f"响应: {json.dumps(response.json(), ensure_ascii=False, indent=2)}")
    # 项目使用统一响应格式：HTTP 200 + 业务code
    assert response.status_code == 200, "HTTP状态码应该是200"
    assert response.json()["code"] == 400, "业务code应该是400"
    assert "微信授权码不能为空" in response.json()["message"], "应包含正确的错误消息"

def test_wechat_login_invalid_code():
    """测试无效的code（模拟微信API调用失败）"""
    print("\n=== 测试3: 无效的code ===")
    url = f"{BASE_URL}/api/v2/auth/wechat-login"
    # 使用无效的code，微信API会返回错误
    response = requests.post(url, json={"code": "invalid_test_code_123"})
    print(f"状态码: {response.status_code}")
    print(f"响应: {json.dumps(response.json(), ensure_ascii=False, indent=2)}")
    # 预期返回错误信息（HTTP 200 + 业务code 401/500）
    assert response.status_code == 200, "HTTP状态码应该是200"
    assert response.json()["code"] in [401, 500], "业务code应该是401或500"
    assert "message" in response.json(), "响应应包含错误消息"

def test_wechat_login_endpoint_accessible():
    """测试接口是否可访问（白名单测试）"""
    print("\n=== 测试4: 接口可访问性（无需JWT） ===")
    url = f"{BASE_URL}/api/v2/auth/wechat-login"
    # 不带Authorization头
    response = requests.post(url, json={"code": "test_code"})
    print(f"状态码: {response.status_code}")
    print(f"响应: {json.dumps(response.json(), ensure_ascii=False, indent=2)}")
    # HTTP状态码应该是200（白名单生效），而不是401或403
    assert response.status_code == 200, "接口应在白名单中，不需要JWT认证，HTTP状态码应该是200"
    # 业务层的错误（微信API调用失败）是正常的，因为code无效
    assert "message" in response.json(), "响应应包含消息字段"

def test_health_check():
    """测试健康检查接口"""
    print("\n=== 测试0: 健康检查 ===")
    url = f"{BASE_URL}/health"
    response = requests.get(url)
    print(f"状态码: {response.status_code}")
    print(f"响应: {response.text}")
    assert response.status_code == 200, "健康检查应返回200"

if __name__ == "__main__":
    print("=" * 60)
    print("微信一键登录接口集成测试")
    print("=" * 60)
    
    try:
        # 先测试服务是否正常
        test_health_check()
        
        # 测试微信登录接口
        test_wechat_login_endpoint_accessible()
        test_wechat_login_missing_code()
        test_wechat_login_empty_code()
        test_wechat_login_invalid_code()
        
        print("\n" + "=" * 60)
        print("✅ 所有测试完成！")
        print("=" * 60)
        print("\n测试总结:")
        print("1. ✓ 微信登录接口已添加到白名单，无需JWT认证")
        print("2. ✓ 参数验证正常工作（缺少/空code返回错误）")
        print("3. ✓ 无效code能正确处理并返回错误信息")
        print("\n注意: 由于需要真实的微信授权码，无法测试完整的登录流程")
        print("建议使用微信开发者工具获取真实code进行完整测试")
        
    except requests.exceptions.ConnectionError:
        print("\n❌ 错误: 无法连接到服务器，请确保应用程序正在运行")
    except AssertionError as e:
        print(f"\n❌ 测试失败: {e}")
    except Exception as e:
        print(f"\n❌ 发生错误: {e}")
