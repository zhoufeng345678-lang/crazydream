# 测试脚本目录

本目录包含项目的所有测试相关脚本和测试结果。

## 目录结构

### api_tests/
API集成测试脚本和测试报告

**测试脚本**:
- `comprehensive_api_test.py` - 综合API测试（旧版）
- `comprehensive_v2_api_test.py` - V2版本API综合测试
- `test_wechat_login.py` - 微信登录功能测试

**测试报告**:
- `api_test_report.json` - API测试结果
- `comprehensive_api_test_report.json` - 综合测试结果
- `v2_api_test_report.json` - V2 API测试结果
- `api_test_final_summary.txt` - 最终测试总结

### performance_tests/
性能和负载测试脚本

**测试脚本**:
- `performance_test.py` - 性能测试脚本
- `load_test.py` - 负载测试脚本

**测试结果**:
- `performance_test_results.json` - 性能测试结果（已移至api_tests/）

## 运行测试

### API测试
```bash
# 运行V2 API测试
python3 tests/api_tests/comprehensive_v2_api_test.py

# 运行微信登录测试
python3 tests/api_tests/test_wechat_login.py
```

### 性能测试
```bash
# 运行性能测试
python3 tests/performance_tests/performance_test.py

# 运行负载测试
python3 tests/performance_tests/load_test.py
```

## 注意事项

1. 运行测试前确保应用程序已启动
2. 确保配置了正确的测试环境变量
3. 某些测试需要真实的微信AppID和AppSecret
