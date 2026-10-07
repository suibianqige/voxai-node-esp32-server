<h1 align="center">voxAI Node</h1>

<p align="center">
  面向 ESP32 智能硬件的企业级服务端与前后端管理平台（私有化部署版本）
</p>

---

## 项目简介

voxAI Node 是一个 **Java 企业级智能语音助手服务端**，采用多模块 + 双进程架构设计，为 ESP32 智能硬件提供完整的后端支撑和可视化管理平台。

> 本项目基于开源项目 [xiaozhi-esp32-server-java](https://github.com/joey-zhou/xiaozhi-esp32-server-java)（MIT License）二次开发，衍生关系与上游版权声明见 [NOTICE](./NOTICE)。

### 核心特性

- **多模块 + 双进程架构** — 管理后台与对话服务独立运行，互不影响，支持分别扩容
- **多 AI 平台集成** — OpenAI / 智谱 / 讯飞 / Ollama / Dify / Coze，MCP 工具协议扩展
- **语音全链路** — 本地 & 云端 STT/TTS，双向流式交互，实时打断，智能防误打断
- **WebSocket 实时通信** — 实时双向音频流，OTA 升级
- **IoT 智能家居** — 语音指令控制设备，Function Call 智能决策
- **记忆管理** — 持久化对话，记忆总结
- **一键部署** — bin 脚本 / Docker Compose，Flyway 自动建表，模型自动下载

### 技术栈

| 类别 | 技术选型 |
|------|----------|
| **后端** | Spring Boot、Spring MVC、MyBatis-Plus、Flyway、WebSocket |
| **前端** | Vue.js、Ant Design、响应式布局 |
| **数据层** | MySQL 8.0、Redis 7 |
| **语音识别** | sherpa-onnx SenseVoice（本地）、Vosk（本地）、FunASR、阿里云、腾讯云、讯飞、火山引擎 |
| **语音合成** | sherpa-onnx（本地）、Edge TTS（仅限评估）、阿里云、腾讯云、讯飞、火山引擎、MiniMax |
| **大语言模型** | OpenAI、智谱 AI、讯飞星火、火山方舟、Ollama、Dify、Coze |

---

## 项目架构

> **双进程架构**：两个独立进程共享 MySQL 和 Redis，可分别部署与扩容。
> - `voxai-server` :8091 — 管理后台，提供 REST API、用户/设备/角色管理、OTA 升级
> - `voxai-dialogue` :8092 — 对话服务，处理 WebSocket 实时音频流、AI 对话管道
>
> `dialogue` 支持横向扩展，新实例自动注册至 `server`，通过设备 OTA 实现负载均衡。

---

<a id="deployment"></a>
## 部署

| 方式 | 适合 | 前置条件 |
|------|------|----------|
| **Docker**（推荐） | 直接用起来 | 只要 Docker |
| **源码** | 要改代码 | JDK 21、Maven、Node 22、MySQL 8、Redis 7 |

### Docker

```bash
mkdir voxai && cd voxai
curl -O https://raw.githubusercontent.com/your-org/voxai/main/docker-compose.yml
cp .env.example .env   # 编辑 .env，设置 MYSQL_PASSWORD / MYSQL_ROOT_PASSWORD（无默认值）
docker compose up -d
```

等容器都 healthy 后打开 <http://localhost:8084>，账号 **admin / 123456**（首次登录强制改密）。
详见 [Docker 部署](./docs/DOCKER.md)。

### 源码

```bash
git clone https://github.com/your-org/voxai
cd voxai
docker compose -f docker-compose-db.yml up -d   # 起 MySQL + Redis，已有可跳过
./scripts/download_models.sh                    # 下载模型和原生库，首次必须
bin/all.sh start                                # 自检、编译并启动
cd web && npm install && npm run dev            # 前端
```

Windows 用 `bin\all.ps1 start`。详见 [CentOS 部署](./docs/CENTOS_DEVELOPMENT.md) / [Windows 部署](./docs/WINDOWS_DEVELOPMENT.md)。

> `models/` 和 `lib/` 不在 Git 仓库中，首次部署需通过脚本下载。
> 语音识别与合成全用第三方 API 的话，只跑 `./scripts/download_base.sh` 即可（仅 VAD 模型和原生库）。

### 登录之后

**要自己配一个大模型的 API Key 才能对话**，系统不预置任何密钥。
语音识别用内置本地模型、语音合成用免费 Edge TTS，都可以先不管。
见[配置说明](./docs/CONFIGURATION.md#第一次使用要配什么)。

设备侧填这两个地址（分属两个进程，别写混）：

- OTA：`http://<内网IP>:8091/api/device/ota`
- WebSocket：`ws://<内网IP>:8092/ws/voxai/v1/`

改了服务端口时，要同步改 `voxai.server.port` 与 `voxai.dialogue.port`，
否则下发给设备的地址还是旧端口。

### 文档

| 文档 | 内容 |
|------|------|
| [Docker 部署](./docs/DOCKER.md) | 一键启动、升级、源码构建 |
| [配置说明](./docs/CONFIGURATION.md) | 首次配置、环境变量、安全默认值 |
| [常见问题](./docs/FAQ.md) | 部署与使用中的高频问题 |
| [CentOS 部署](./docs/CENTOS_DEVELOPMENT.md) | Linux 源码部署，推荐生产环境 |
| [Windows 部署](./docs/WINDOWS_DEVELOPMENT.md) | Windows 开发与测试 |
| [固件编译](./docs/FIRMWARE-BUILD.md) | ESP32 固件编译和烧录 |

---

## 开源声明

本项目基于 [xiaozhi-esp32-server-java](https://github.com/joey-zhou/xiaozhi-esp32-server-java) 二次开发，遵循其 MIT License：
原许可证保留于仓库根目录 [LICENSE](./LICENSE)，衍生关系见 [NOTICE](./NOTICE)。

---

## 免责声明

本项目仅提供技术实现代码，不提供任何媒体内容。用户在使用相关功能时应确保拥有合法的使用权或版权许可，并遵守所在地区的版权法律法规。

项目中可能涉及的示例内容或资源仅用于功能演示和技术测试。如有内容侵犯了您的权益，请立即联系我们，我们将在核实后立即采取删除等处理措施。

本项目开发者不对用户使用本项目代码获取或播放的任何内容承担法律责任。使用本项目即表示您同意自行承担使用过程中的全部法律风险和责任。
