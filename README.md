[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/16JY11ZU)
[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18741859&assignment_repo_type=AssignmentRepo)

简易社交分享平台项目

实现的功能：
基础级：实现文字分享、浏览功能
标准级：增加图片上传、评论功能
挑战级：实现点赞、权限控制等高级功能

## 如何一键部署运行（Docker）

1. 确保你的 Linux 系统里安装好了 Docker 和 Docker Compose。
1. 切换到生产环境配置文件：找到`./backend/src/main/resources/application.yml`文件，将`active: dev`更改为`active: prod`。
2. 在仓库根目录运行`docker compose up`命令，等待容器构建完后，系统会开始运行。使用浏览器访问本机的 9000 端口即可进入系统，使用数据库客户端访问本机的 9001 端口可以访问系统的 MySQL 数据库，访问本机的 9002 端口可以访问系统的 Redis 数据库。

   > 注意：构建 backend 镜像的时候，可能会在运行 `gradle build --no-daemon` 这个步骤的时候卡住很久，这是 Gradle 正在下载依赖项，请耐心等待。

   > ⚠️警告：若要将服务发布至公网，为了您的系统安全，请勿暴露 9001 和 9002 端口，以避免网络攻击风险！若有在公网访问数据库的需求，请务必为 MySQL 和 Redis 设置高强度随机密码！

## 如何开发和运行（Windows/Linux）

### 开发

1. 确保你已经安装好 JDK 17。
2. 在 Visual Studio Code 中打开 `frontend` 文件夹来开发前端，执行 `npm install` 命令安装依赖，并根据 VSCode 的提示打开父目录的 git 仓库。
3. 在 IntelliJ IDEA 中打开 `backend` 文件夹来开发后端。

### 运行准备工作

1. 安装 MySQL（推荐安装 8.4 版本），创建数据库 `easyweibo`，并执行 `mysql-init` 目录中的 SQL 文件初始化数据库结构。
2. 安装 Redis
3. 编辑 `application-dev.yml`，指定数据库的地址、密码等参数。

### 运行

1. 确保你已经下载了 Caddy 并设置好了环境变量。
2. 启动后端项目。
3. 运行根目录的 `start_caddy_dev.bat`（Linux 用户可输入 `caddy run` 命令手动运行 Caddy）。
4. 在前端项目中执行 `npm run dev` 命令启动前端项目。

## 第一阶段：Web三件套基础（HTML/CSS/JavaScript）

### 学习重点
1. **HTML基础**: 语义化标签、表单设计、页面结构
2. **CSS基础**: 布局技术（Flexbox/Grid）、响应式设计、样式组织
3. **JavaScript基础**: DOM操作、事件处理、基础语法

### 阶段任务
1. 设计并实现静态页面原型
   - 主页(信息流展示)
   - 发布页面
   - 个人主页
2. 实现基本交互效果
   - 表单验证
   - 动态内容展示
   - 简单特效

### 实践建议
- 先用纸笔设计UI草图，然后转化为HTML/CSS
- 创建可重用的CSS类和组件
- 使用JavaScript实现基本的表单验证

## 第二阶段：Vue3入门与应用

### 学习重点
1. **Vue3基础**: 组件、模板语法、响应式系统
2. **Vue Router**: 前端路由设计与实现
3. **状态管理**: Composition API、响应式数据

### 阶段任务
1. 将静态页面转换为Vue组件
2. 实现前端路由系统
3. 设计数据状态管理方案
4. 实现前端模拟数据交互

### 实践建议
- 从小组件开始，逐步构建复杂组件
- 利用Vue DevTools进行调试
- 使用Vite作为开发环境
- 将页面划分为明确的组件层次

## 第三阶段：JavaWeb后端开发

### 学习重点
1. **Servlet基础**: 请求处理、响应生成
2. **数据库交互**: JDBC/MyBatis
3. **MVC设计模式**: 控制器、服务层、数据访问层
4. **RESTful API**: API设计规范、状态码

### 阶段任务
1. 设计数据库表结构
2. 实现用户认证系统
3. 开发内容CRUD接口
4. 处理文件上传与存储

### 实践建议
- 使用Spring Boot简化配置
- 编写完整的API文档
- 实现统一的异常处理
- 考虑安全性（SQL注入、XSS等）

## 第四阶段：前后端整合

### 学习重点
1. **前后端通信**: Axios/Fetch API
2. **跨域问题解决**: CORS配置
3. **数据交互优化**: 缓存、懒加载

### 阶段任务
1. 整合前端Vue与后端API
2. 实现实时数据刷新
3. 优化用户体验
4. 实现更高级功能(点赞、权限等)

### 实践建议
- 采用渐进式开发方法
- 使用Mock数据进行前端开发
- 实现加载状态和错误处理
- 注重代码可维护性

## 分级实现建议

### 基础级（满足及格要求）
- 完成静态页面设计
- 实现基本Vue组件
- 开发简单的后端API
- 完成文字发布和浏览功能

### 标准级（满足良好要求）
- 实现完整的前端路由系统
- 开发图片上传功能
- 实现评论系统
- 基本的用户认证

### 挑战级（满足优秀要求）
- 实现实时点赞和通知
- 开发完整的权限控制系统
- 优化性能和用户体验
- 添加特色功能（如表情、话题标签等）

## 学习资源推荐
- MDN Web文档（HTML/CSS/JS基础）
- Vue.js官方文档
- Spring Boot官方指南
- 视频教程平台（B站、慕课网等）
- GitHub上的开源项目参考

通过这种逐步递进的学习方法，学生可以在理解基础概念的同时，逐步构建起完整的应用，最终达成项目目标。
