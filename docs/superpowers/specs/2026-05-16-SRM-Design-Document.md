# SRM 供应商关系管理系统 - 设计文档

> **文档版本**：v1.0  
> **创建日期**：2026-05-16  
> **最后更新**：2026-05-16  
> **作者**：设计团队

---

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 系统架构设计](#2-系统架构设计)
- [3. 数据库设计](#3-数据库设计)
- [4. 前端页面设计](#4-前端页面设计)
- [5. API 接口设计](#5-api-接口设计)
- [6. 实施计划](#6-实施计划)

---

## 1. 项目概述

### 1.1 产品定位

SRM（Supplier Relationship Management）是面向中型企业的SaaS化供应商关系管理系统，提供供应商档案管理、采购流程管理、合同生命周期管理、供应商绩效评估、财务对账付款等全流程解决方案。

### 1.2 目标用户

- 中型企业（员工100-1000人）
- 管理100-500家供应商
- 需要规范化采购流程的企业
- 重视供应商关系管理的企业

### 1.3 核心功能

| 模块 | 功能说明 |
|------|---------|
| 供应商管理 | 供应商档案、联系人、资质证书、供应商分析 |
| 采购管理 | 采购需求、询价、订单、入库全流程 |
| 合同管理 | 合同创建、条款、附件、变更、到期提醒 |
| 绩效评估 | 评估模板、评估流程、评估报告、统计分析 |
| 财务管理 | 发票管理、付款申请、对账、财务统计 |

---

## 2. 系统架构设计

### 2.1 技术选型

| 层次 | 技术选型 |
|------|---------|
| 前端 | Vue.js 3 + Vite + Element Plus |
| 后端 | Spring Boot 3 + Spring Security + MyBatis Plus |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis 7.0 |
| 文件存储 | 阿里云OSS |
| 容器化 | Docker + Docker Compose |
| 云服务 | 阿里云（ECS + RDS + OSS） |

### 2.2 系统架构图

```
┌─────────────────────────────────────────────────────────┐
│                     客户端层（Client）                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │ Web浏览器 │  │  移动端  │  │  供应商  │  │  集成API │ │
│  │ (Vue.js) │  │ (H5/小程序)│  │ 门户(PC) │  │ (REST)  │ │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘ │
└─────────────────────────────────────────────────────────┘
                            ↓ HTTP/HTTPS
┌─────────────────────────────────────────────────────────┐
│                     网关层（Gateway）                     │
│  ┌─────────────────────────────────────────────────┐   │
│  │              Nginx反向代理 + SSL卸载              │   │
│  │         负载均衡 | 请求路由 | 限流防护              │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                     应用层（Application）                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   用户认证    │  │   权限管理    │  │   操作日志    │   │
│  │   (JWT)      │  │  (RBAC)      │  │              │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │  供应商管理   │  │  采购管理    │  │  合同管理     │   │
│  │  服务模块     │  │  服务模块    │  │  服务模块     │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐                       │
│  │  绩效评估     │  │  财务管理    │                       │
│  │  服务模块     │  │  服务模块    │                       │
│  └──────────────┘  └──────────────┘                       │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                     数据层（Data）                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   MySQL     │  │   Redis      │  │   OSS        │   │
│  │  (主数据库)   │  │  (缓存/会话)  │  │  (文件存储)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                     基础设施层（Infra）                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │  阿里云 ECS  │  │  阿里云 RDS  │  │  阿里云 OSS  │   │
│  │  (应用服务)   │  │  (MySQL)    │  │  (文件存储)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### 2.3 安全设计

- 全站HTTPS加密传输
- JWT无状态认证
- RBAC基于角色的权限控制
- 操作日志完整记录
- SQL注入/XSS攻击防护

---

## 3. 数据库设计

### 3.1 数据库整体结构

```
SRM_System
├── sys_.*           # 系统管理模块（用户、角色、权限）
├── supplier_.*      # 供应商管理模块
├── purchase_.*      # 采购管理模块
├── contract_.*      # 合同管理模块
├── evaluation_.*    # 绩效评估模块
├── finance_.*       # 财务管理模块
└── common_.*         # 公共模块（附件、日志等）
```

### 3.2 核心数据表

#### 3.2.1 系统管理模块

```sql
-- 用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    tenant_id BIGINT COMMENT '租户ID（多租户支持）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 角色表
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    role_name VARCHAR(100) NOT NULL COMMENT '角色名称',
    description VARCHAR(255) COMMENT '角色描述',
    tenant_id BIGINT COMMENT '租户ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 权限表
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    path VARCHAR(255) COMMENT '路由路径',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    type TINYINT COMMENT '类型：1菜单 2按钮',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### 3.2.2 供应商管理模块

```sql
-- 供应商主表
CREATE TABLE supplier (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
    supplier_code VARCHAR(50) NOT NULL UNIQUE COMMENT '供应商编码',
    supplier_name VARCHAR(200) NOT NULL COMMENT '供应商名称',
    short_name VARCHAR(100) COMMENT '简称',
    supplier_type TINYINT COMMENT '类型：1制造商 2代理商 3服务商',
    industry VARCHAR(100) COMMENT '所属行业',
    country VARCHAR(50) COMMENT '国家',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    address VARCHAR(255) COMMENT '详细地址',
    contact_name VARCHAR(100) COMMENT '联系人姓名',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    website VARCHAR(200) COMMENT '官网',
    tax_number VARCHAR(50) COMMENT '税号',
    bank_name VARCHAR(100) COMMENT '开户银行',
    bank_account VARCHAR(50) COMMENT '银行账号',
    credit_level VARCHAR(10) COMMENT '信用等级',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用 2待审核',
    register_capital DECIMAL(15,2) COMMENT '注册资本',
    employee_count INT COMMENT '员工人数',
    intro TEXT COMMENT '公司简介',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_status (status)
);

-- 供应商资质文件表
CREATE TABLE supplier_certificate (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    cert_type VARCHAR(50) COMMENT '资质类型',
    cert_name VARCHAR(200) COMMENT '证书名称',
    cert_number VARCHAR(100) COMMENT '证书编号',
    issue_date DATE COMMENT '发证日期',
    expire_date DATE COMMENT '到期日期',
    file_url VARCHAR(500) COMMENT '文件URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0失效 1有效',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- 供应商联系人表
CREATE TABLE supplier_contact (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    contact_name VARCHAR(100) NOT NULL COMMENT '联系人姓名',
    department VARCHAR(100) COMMENT '部门',
    position VARCHAR(100) COMMENT '职位',
    phone VARCHAR(20) COMMENT '电话',
    mobile VARCHAR(20) COMMENT '手机',
    email VARCHAR(100) COMMENT '邮箱',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主联系人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);
```

#### 3.2.3 采购管理模块

```sql
-- 采购需求表
CREATE TABLE purchase_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_no VARCHAR(50) NOT NULL UNIQUE COMMENT '需求单号',
    title VARCHAR(200) NOT NULL COMMENT '需求标题',
    request_type VARCHAR(50) COMMENT '需求类型：物料/服务/设备',
    department VARCHAR(100) COMMENT '需求部门',
    requester_id BIGINT NOT NULL COMMENT '申请人ID',
    expected_date DATE COMMENT '期望交付日期',
    total_amount DECIMAL(15,2) COMMENT '预算金额',
    priority TINYINT DEFAULT 2 COMMENT '优先级：1紧急 2普通 3低',
    reason TEXT COMMENT '申请理由',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态：draft/demand/quote/approved/closed',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_status (status)
);

-- 采购订单表
CREATE TABLE purchase_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    quote_id BIGINT COMMENT '关联询价单',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    order_type VARCHAR(50) COMMENT '订单类型',
    order_amount DECIMAL(15,2) NOT NULL COMMENT '订单金额',
    paid_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已付款金额',
    delivery_address VARCHAR(255) COMMENT '交货地址',
    expected_date DATE COMMENT '期望交货日期',
    actual_date DATE COMMENT '实际交货日期',
    payment_status VARCHAR(20) DEFAULT 'unpaid' COMMENT '付款状态',
    delivery_status VARCHAR(20) DEFAULT 'undelivered' COMMENT '交货状态',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态',
    tenant_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);
```

#### 3.2.4 合同管理模块

```sql
-- 合同主表
CREATE TABLE contract (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    contract_no VARCHAR(50) NOT NULL UNIQUE COMMENT '合同编号',
    contract_name VARCHAR(200) NOT NULL COMMENT '合同名称',
    contract_type VARCHAR(50) COMMENT '合同类型',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    order_id BIGINT COMMENT '关联订单ID',
    contract_amount DECIMAL(15,2) COMMENT '合同金额',
    signed_date DATE COMMENT '签订日期',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    contract_file VARCHAR(500) COMMENT '合同文件URL',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态',
    tenant_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);
```

#### 3.2.5 绩效评估模块

```sql
-- 评估模板表
CREATE TABLE evaluation_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(200) NOT NULL COMMENT '模板名称',
    template_type VARCHAR(50) COMMENT '模板类型',
    description TEXT COMMENT '模板描述',
    total_score INT DEFAULT 100 COMMENT '总分',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认模板',
    tenant_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 供应商评估表
CREATE TABLE supplier_evaluation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    evaluation_no VARCHAR(50) NOT NULL UNIQUE COMMENT '评估单号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    template_id BIGINT NOT NULL COMMENT '评估模板ID',
    total_score DECIMAL(5,2) COMMENT '总分',
    grade VARCHAR(10) COMMENT '评级：A/B/C/D',
    evaluator_id BIGINT COMMENT '评估人',
    evaluate_date DATE COMMENT '评估日期',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态',
    tenant_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES supplier(id),
    FOREIGN KEY (template_id) REFERENCES evaluation_template(id)
);
```

#### 3.2.6 财务管理模块

```sql
-- 发票表
CREATE TABLE invoice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    invoice_no VARCHAR(50) NOT NULL UNIQUE COMMENT '发票号',
    order_id BIGINT COMMENT '关联订单ID',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    invoice_amount DECIMAL(15,2) NOT NULL COMMENT '发票金额',
    tax_amount DECIMAL(15,2) COMMENT '税额',
    invoice_date DATE COMMENT '开票日期',
    due_date DATE COMMENT '到期日期',
    payment_status VARCHAR(20) DEFAULT 'unpaid' COMMENT '付款状态',
    received_status VARCHAR(20) DEFAULT 'pending' COMMENT '收票状态',
    invoice_file VARCHAR(500) COMMENT '发票文件URL',
    tenant_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- 付款申请表
CREATE TABLE payment_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_no VARCHAR(50) NOT NULL UNIQUE COMMENT '付款单号',
    order_id BIGINT COMMENT '关联订单ID',
    invoice_id BIGINT COMMENT '关联发票ID',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    payment_amount DECIMAL(15,2) NOT NULL COMMENT '付款金额',
    payment_method VARCHAR(50) COMMENT '付款方式',
    expected_date DATE COMMENT '期望付款日期',
    actual_date DATE COMMENT '实际付款日期',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态',
    tenant_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id)
);
```

---

## 4. 前端页面设计

### 4.1 页面结构

```
SRM-Web/
├── src/
│   ├── api/                    # API接口定义
│   ├── views/                 # 页面组件
│   │   ├── layout/           # 布局组件
│   │   ├── dashboard/        # 工作台/首页
│   │   ├── system/           # 系统管理
│   │   ├── supplier/         # 供应商管理
│   │   ├── purchase/         # 采购管理
│   │   ├── contract/         # 合同管理
│   │   ├── evaluation/       # 绩效评估
│   │   └── finance/          # 财务管理
│   ├── router/               # 路由配置
│   ├── store/                # 状态管理
│   └── utils/                # 工具函数
```

### 4.2 核心页面功能

#### 4.2.1 工作台首页

- 今日概览统计卡片（待审批、待付款、合同到期提醒、绩效预警）
- 待办事项列表（采购审批、合同签署、付款审批）
- 最近供应商动态
- 采购金额趋势图
- 供应商绩效分布图

#### 4.2.2 供应商管理

**供应商列表**：
- 多条件筛选（供应商名称、类型、状态、行业、评级）
- 列表展示（编号、名称、类型、行业、评级、联系人、合作状态）
- 批量操作（导出、状态变更）

**供应商详情**：
- 基本信息卡片
- 联系人列表
- 资质证书管理
- 合作历史（订单、合同、付款记录）
- 绩效评估记录

#### 4.2.3 采购管理

**流程图**：
```
需求申请 → 需求审批 → 询价管理 → 供应商比价 → 订单生成 → 审批确认 → 入库管理 → 发票核对 → 付款申请 → 付款审批 → 完成
```

#### 4.2.4 合同管理

- 合同列表（按状态、类型分类）
- 合同到期提醒
- 合同创建、编辑、审批、签署
- 合同变更历史
- 附件管理

#### 4.2.5 绩效评估

**评估流程**：
1. 选择评估对象（供应商）
2. 选择评估模板
3. 填写评估指标得分
4. 上传评估佐证
5. 提交评估结果
6. 审批确认
7. 生成评估报告

**评估维度**：
- 质量指标（30%）：产品质量、合格率、退货率
- 价格指标（25%）：价格竞争力、价格稳定性
- 交期指标（25%）：准时交货率、交货周期
- 服务指标（20%）：响应速度、售后服务、技术支持

#### 4.2.6 财务管理

- 发票管理（收票登记、核对、状态跟踪）
- 付款管理（申请、审批、执行、记录）
- 对账管理（生成、确认、差异处理）

---

## 5. API 接口设计

### 5.1 API 基础规范

```javascript
// Base URL: /api/v1

// 请求规范
{
  method: 'POST',
  url: '/supplier',
  data: {
    supplierName: 'xxx',
    supplierType: 1,
    // ...
  }
}

// 响应规范
{
  code: 200,           // 状态码：200成功，400参数错误，401未授权，403无权限，500服务器错误
  message: 'success',  // 消息
  data: {             // 数据
    records: [],
    total: 100,
    pageNum: 1,
    pageSize: 10
  },
  timestamp: 1700000000000
}
```

### 5.2 核心 API 列表

#### 系统管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/sys/users | 获取用户列表 |
| POST | /api/v1/sys/users | 创建用户 |
| GET | /api/v1/sys/roles | 获取角色列表 |
| GET | /api/v1/sys/permissions | 获取权限树 |

#### 供应商管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/suppliers | 获取供应商列表 |
| POST | /api/v1/suppliers | 创建供应商 |
| GET | /api/v1/suppliers/:id | 获取供应商详情 |
| PUT | /api/v1/suppliers/:id | 更新供应商 |
| GET | /api/v1/suppliers/:id/contacts | 获取联系人列表 |
| GET | /api/v1/suppliers/:id/certificates | 获取资质列表 |

#### 采购管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/purchase/requests | 获取需求列表 |
| POST | /api/v1/purchase/requests | 创建需求 |
| GET | /api/v1/purchase/orders | 获取订单列表 |
| POST | /api/v1/purchase/orders | 创建订单 |

#### 合同管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/contracts | 获取合同列表 |
| POST | /api/v1/contracts | 创建合同 |
| GET | /api/v1/contracts/expiring | 即将到期合同 |

#### 绩效评估 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/evaluations/templates | 获取评估模板列表 |
| POST | /api/v1/evaluations/templates | 创建模板 |
| GET | /api/v1/evaluations | 获取评估列表 |
| POST | /api/v1/evaluations | 创建评估 |

#### 财务管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/finance/invoices | 获取发票列表 |
| POST | /api/v1/finance/invoices | 登记发票 |
| GET | /api/v1/finance/payments | 获取付款列表 |
| POST | /api/v1/finance/payments | 申请付款 |

---

## 6. 实施计划

### 6.1 阶段划分

| 阶段 | 工期 | 目标 |
|------|------|------|
| 阶段一：基础框架搭建 | 3周 | 完成项目基础架构，可运行基础系统 |
| 阶段二：核心业务开发 | 5周 | 完成供应商管理、采购管理两个核心模块 |
| 阶段三：补充功能模块 | 4周 | 完成合同管理、绩效评估、财务管理 |
| 阶段四：测试优化 | 2周 | 系统测试、Bug修复、体验优化 |
| 阶段五：部署上线 | 2周 | 生产环境部署，正式上线 |
| **总计** | **16周（4个月）** | |

### 6.2 详细计划

#### 阶段一：基础框架搭建（3周）

**任务清单**：
- [ ] 项目脚手架搭建
- [ ] 系统管理模块开发
- [ ] 通用功能（文件上传、通知）

**里程碑**：系统能够正常登录，用户可以看到工作台首页

#### 阶段二：核心业务开发（5周）

**任务清单**：
- [ ] 供应商管理模块（2周）
- [ ] 采购流程模块（3周）

**里程碑**：用户可以完成完整的采购流程

#### 阶段三：补充功能模块（4周）

**任务清单**：
- [ ] 合同管理模块（1.5周）
- [ ] 绩效评估模块（1.5周）
- [ ] 财务管理模块（1周）

**里程碑**：5个功能模块全部完成

#### 阶段四：测试优化（2周）

**任务清单**：
- [ ] 功能测试
- [ ] 性能优化
- [ ] 用户体验优化
- [ ] 安全测试

**里程碑**：系统稳定，测试通过率>95%

#### 阶段五：部署上线（2周）

**任务清单**：
- [ ] 生产环境部署
- [ ] 上线准备（文档、数据准备）
- [ ] 正式上线

**里程碑**：系统正式上线，用户开始使用

### 6.3 团队配置

| 角色 | 人数 | 职责 |
|------|------|------|
| 后端开发 | 2人 | Spring Boot开发、数据库设计、API实现 |
| 前端开发 | 2人 | Vue.js开发、页面实现、交互逻辑 |
| 产品/设计 | 1人 | 需求调研、原型设计、UI设计、验收 |
| 测试/运维 | 1人 | 测试用例、测试执行、部署上线、运维 |
| **合计** | **6人** | **完整的全栈团队** |

---

## 附录

### A. 开发规范

**代码规范**：
- 后端遵循阿里巴巴Java开发手册
- 前端遵循ESLint标准
- Git提交规范：`feat: 新功能`/`fix: 修复`/`docs: 文档`/`refactor: 重构`

**接口规范**：
- RESTful风格API
- 统一响应格式
- 详细的API文档（Swagger）

**数据库规范**：
- 表名：模块名_业务名，如 `supplier_info`
- 字段名：下划线分隔，如 `supplier_name`
- 必要字段：id、created_at、updated_at、created_by

### B. 变更记录

| 版本 | 日期 | 变更人 | 变更说明 |
|------|------|--------|---------|
| v1.0 | 2026-05-16 | 设计团队 | 初始版本 |

---

**文档结束**
