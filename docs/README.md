# 文档目录

本目录包含项目的所有技术文档、API文档和历史报告。

## 目录结构

### api_documentation/
API文档和接口说明

- `api-documentation.md` - 完整API文档（42KB）
- `WECHAT_LOGIN_API.md` - 微信一键登录API文档

**使用指南**: 开发时参考API文档了解接口定义、请求参数和响应格式。

### technical_reports/
技术报告和架构文档

- `DDD_MIGRATION_REPORT.md` - DDD架构迁移报告
- `DDD_REFACTORING_REPORT.md` - DDD重构详细报告

**使用指南**: 了解项目架构演进和重构历史。

### archived_reports/
历史归档报告

- `API_TEST_REPORT.md` - API测试报告（历史）
- `BUGFIX-2026-01-19-STATUS-TRANSITION.md` - 状态转换Bug修复记录
- `option_a_fix_summary.md` - 修复方案总结
- `API_UPDATE_SUMMARY.txt` - API更新总结

**使用指南**: 查阅历史问题和解决方案，避免重复踩坑。

## 文档维护规范

1. **API文档**: 新增或修改API时，同步更新 `api_documentation/` 中的文档
2. **技术报告**: 重大架构变更或重构完成后，在 `technical_reports/` 中添加报告
3. **归档报告**: 已解决的问题报告和过时文档移至 `archived_reports/`

## 相关链接

- 项目README: [../README.md](../README.md)
- 部署文档: [../DEPLOYMENT.md](../DEPLOYMENT.md)
- OpenSpec提案: [../openspec/](../openspec/)
