# 练习炒股记录 📈

一个用于记录和分析股票交易练习的Android应用，帮助用户总结交易经验，提升投资技能。

## 功能特性 ✨

### 📝 开仓记录
- 选择开仓理由（技术分析、基本面分析、消息面、市场情绪、跟随趋势、其他）
- 记录交易结果（盈利、亏损、保本）
- 支持拍照或从相册选择图片记录
- 本地数据存储，无需网络连接

### 📊 交易管理
- 查看所有交易记录列表
- 按开仓理由和交易结果筛选记录
- 点击记录查看详细信息
- 支持图片大图预览

### 📈 统计分析
- 饼图展示开仓理由分布
- 详细统计数据列表
- 百分比和数量统计
- 直观的数据可视化

### 🎨 用户体验
- Material Design 3 设计语言
- 支持日夜主题切换
- 流畅的页面切换动画
- 直观的操作界面

## 技术栈 🛠️

### 开发框架
- **语言**: Kotlin
- **最低SDK**: Android 7.0 (API 24)
- **目标SDK**: Android 14 (API 34)
- **构建工具**: Gradle 8.14.1

### 核心库
- **UI框架**: Material Design 3
- **视图绑定**: ViewBinding
- **图片加载**: Glide 4.16.0
- **权限管理**: Dexter 6.2.3
- **图表库**: MPAndroidChart 3.1.0
- **JSON处理**: Gson 2.10.1

### 架构组件
- **数据存储**: SharedPreferences + JSON序列化
- **图片存储**: 应用内部存储
- **页面管理**: ViewPager2 + Fragment
- **列表展示**: RecyclerView + Adapter

## 项目结构 📁

```
app/src/main/
├── java/com/practicestock/app/
│   ├── MainActivity.kt                 # 主Activity
│   ├── data/
│   │   └── DataManager.kt             # 数据管理类
│   ├── model/
│   │   └── TradeRecord.kt             # 数据模型
│   ├── ui/
│   │   ├── OpenPositionFragment.kt    # 开仓页面
│   │   ├── TradingFragment.kt         # 交易页面
│   │   ├── TradeListFragment.kt       # 交易列表
│   │   ├── StatisticsFragment.kt      # 统计页面
│   │   ├── TradeDetailActivity.kt     # 详情页面
│   │   └── adapter/                   # 适配器
│   │       ├── TradeRecordAdapter.kt
│   │       └── StatisticsAdapter.kt
│   └── utils/
│       └── ImageUtils.kt              # 图片工具类
├── res/
│   ├── layout/                        # 布局文件
│   ├── values/                        # 资源文件
│   ├── drawable/                      # 图标资源
│   └── xml/                          # 配置文件
└── AndroidManifest.xml               # 应用清单
```

## 安装使用 🚀

### 环境要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 17 或更高版本
- Android SDK 34
- Gradle 8.14.1

### 构建步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd practiceStock
   ```

2. **打开项目**
   - 使用Android Studio打开项目根目录
   - 等待Gradle同步完成

3. **构建应用**
   ```bash
   ./gradlew build
   ```

4. **运行应用**
   - 连接Android设备或启动模拟器
   - 点击运行按钮或执行：
   ```bash
   ./gradlew installDebug
   ```

### 权限说明
应用需要以下权限：
- `CAMERA`: 拍照功能
- `READ_EXTERNAL_STORAGE`: 读取相册图片
- `WRITE_EXTERNAL_STORAGE`: 保存图片文件
- `READ_MEDIA_IMAGES`: Android 13+读取图片

## 使用指南 📖

### 1. 记录交易
- 打开应用，默认进入"开仓"页面
- 选择开仓理由和交易结果
- 点击图片区域上传交易截图
- 点击"保存"按钮完成记录

### 2. 查看记录
- 切换到"交易"页面
- 在"列表"子页面查看所有记录
- 使用筛选功能快速查找特定记录
- 点击记录查看详细信息

### 3. 统计分析
- 在"交易"页面切换到"统计"子页面
- 查看饼图了解开仓理由分布
- 浏览详细统计数据

## 数据存储 💾

应用使用本地存储方案：
- **交易记录**: 存储在SharedPreferences中，使用JSON格式序列化
- **图片文件**: 保存在应用内部存储目录，自动压缩优化
- **配置信息**: 使用SharedPreferences存储用户设置

## 开发说明 👨‍💻

### 代码规范
- 使用Kotlin语言特性，如数据类、扩展函数等
- 遵循Material Design设计规范
- 采用MVVM架构模式
- 使用ViewBinding替代findViewById

### 扩展建议
- 添加数据导出功能
- 实现云端数据同步
- 增加更多统计图表类型
- 支持交易策略标签
- 添加交易日历功能

## 版本历史 📝

### v1.0.0 (当前版本)
- ✅ 基础交易记录功能
- ✅ 图片上传和预览
- ✅ 数据筛选和统计
- ✅ Material Design 3界面
- ✅ 本地数据存储

## 许可证 📄

本项目仅供学习和个人使用。

## 联系方式 📧

如有问题或建议，欢迎提交Issue或Pull Request。

---

**注意**: 本应用仅用于模拟交易记录，不涉及真实资金交易。投资有风险，入市需谨慎。