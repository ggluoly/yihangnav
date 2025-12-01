# 使用说明

- 启动：`mvn spring-boot:run`，端口默认 `8888`（可用 `PORT` 覆盖）。
- 默认管理员：`admin / 123456`，JWT 认证后访问受保护接口。
- 配置：`src/main/resources/application.properties`，支持环境变量。SQLite 文件默认 `./data/yihangnav.db`；备份目录 `./backups`；上传目录 `./uploads`。
- 站点配置：默认初始化 `site.title`、`site.background`、`ads.enabled`，可通过 `/api/admin/config` 修改。
- 备份：ZIP 包含 SQLite 数据库和上传文件，支持定时自动备份与上传 ZIP 恢复。
- 静态导航页面在 `src/main/resources/static`，后台 API 以 `/api/**` 开头。
- 常用接口提示：卡片元数据抓取 `/api/admin/cards/metadata?url=...`；聚合搜索引擎 `/api/public/search-engines`。

更多细节见 `README.md`。
