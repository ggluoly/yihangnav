# Yihang 导航站

基于 Spring Boot 2.7 + SQLite + JWT 的卡片式导航站点，前端复用 `web_tool-master` 的静态页面，后端提供聚合搜索、栏目/卡片/广告/友链、上传、备份等接口。

## 快速开始
- 运行环境：JDK 1.8，Maven，SQLite 驱动内置。
- 启动：`mvn spring-boot:run`（默认端口 8888，可用环境变量 `PORT` 覆盖）。
- 前端访问：http://localhost:8888  
  后台 API：以 `/api/**` 提供（示例：`/api/auth/login`、`/api/public/home`）。

## 环境变量
- `PORT`：服务端口，默认 `8888`
- `ADMIN_USERNAME` / `ADMIN_PASSWORD`：初始管理员账号，默认 `admin / 123456`
- `JWT_SECRET`：JWT 密钥
- `JWT_EXPIRE_HOURS`：Token 过期小时数，默认 `24`
- `SQLITE_PATH`：SQLite 文件路径，默认 `./data/yihangnav.db`
- `UPLOAD_DIR`：上传文件保存目录，默认 `./uploads`
- `BACKUP_DIR`：备份目录，默认 `./backups`
- `BACKUP_MAX_COPIES`：最大保留备份份数，默认 `10`
- `BACKUP_KEEP_DAYS` / `BACKUP_KEEP_MONTHS`：自动备份保留阈值

## 主要功能
- 聚合导航：栏目树 + 卡片，卡片支持启用/禁用、排序、logo/描述。
- 聚合搜索：`/api/search?keyword=xxx`（记录关键词/IP/UA），并提供外部搜索引擎列表 `/api/public/search-engines`（Google/Baidu/Bing/GitHub/站内）。
- 广告/友链：左右悬浮广告位、友情链接 CRUD。
- 站点配置：任意 KV，支持设置背景、标题、广告开关等 UI 配置（默认背景、标题、广告开关会自动初始化）。
- 用户/认证：JWT 登录，记录登录时间/IP，管理员 CRUD。
- 上传：`/api/admin/upload`，返回可访问 URL（静态映射 `/uploads/**`）。
- 备份/恢复：手动/自动备份为 ZIP（包含 SQLite 数据库 + 上传文件），自动定时备份（凌晨 3 点），支持删除历史、上传 ZIP 恢复。
- 统计：简单登录统计接口 `/api/admin/stats`。
- 卡片元数据解析：`GET /api/admin/cards/metadata?url=...` 自动尝试抓取站点标题/描述/Logo，便于快速录入。

## 关键接口速览
- 认证：`POST /api/auth/login`，`GET /api/auth/me`
- 公共数据：`GET /api/public/home`
- 栏目：`/api/admin/categories`
- 卡片：`/api/admin/cards`
- 卡片元信息抓取：`GET /api/admin/cards/metadata?url=xxx`
- 广告：`/api/admin/ads`
- 友链：`/api/admin/friends`
- 配置：`/api/admin/config`
- 上传：`POST /api/admin/upload`
- 搜索：`GET /api/search`
- 聚合搜索引擎：`GET /api/public/search-engines`
- 备份：`POST /api/admin/backups`，`GET /api/admin/backups`，`POST /api/admin/backups/restore`

## 开发提示
- 静态资源已复制到 `src/main/resources/static`，可直接访问或改为模板引擎。
- JWT 需要设置足够随机的 `JWT_SECRET`，生产环境请修改默认管理员密码。
- 默认 DDL 策略为 `update`，首次启动会自动创建 SQLite 文件和表。
