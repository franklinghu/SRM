-- 创建数据库
CREATE DATABASE IF NOT EXISTS srm_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE srm_db;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    status INT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    tenant_id BIGINT COMMENT '租户ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 供应商主表
CREATE TABLE IF NOT EXISTS supplier (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 供应商联系人表
CREATE TABLE IF NOT EXISTS supplier_contact (
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
    INDEX idx_supplier_id (supplier_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商联系人表';

-- 供应商资质文件表
CREATE TABLE IF NOT EXISTS supplier_certificate (
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
    INDEX idx_supplier_id (supplier_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商资质表';

-- 插入测试用户 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, email, status, tenant_id)
VALUES ('admin', '$2a$10$XQnS4f8L7Z5mQvJ1wY1kKO5g8zX0kY2sD3p6wR9xB2mN4cV5uJ6fG', '系统管理员', 'admin@example.com', 1, 1);

-- 插入测试供应商数据
INSERT INTO supplier (supplier_code, supplier_name, short_name, supplier_type, industry, country, province, city, address, contact_name, contact_phone, contact_email, credit_level, status, tenant_id, created_by) VALUES 
('SUP001', '北京科技有限公司', '北京科技', 1, '电子信息', '中国', '北京市', '北京市', '海淀区中关村大街1号', '张三', '13800138001', 'zhangsan@beijingtech.com', 'A', 1, 1, 1),
('SUP002', '上海贸易公司', '上海贸易', 2, '贸易', '中国', '上海市', '上海市', '浦东新区陆家嘴环路100号', '李四', '13800138002', 'lisi@shanghaitrade.com', 'B', 1, 1, 1);

-- 插入测试联系人数据
INSERT INTO supplier_contact (supplier_id, contact_name, department, position, phone, mobile, email, is_primary) VALUES 
(1, '张三', '销售部', '客户经理', '010-12345678', '13800138001', 'zhangsan@beijingtech.com', 1),
(2, '李四', '业务部', '业务经理', '021-87654321', '13800138002', 'lisi@shanghaitrade.com', 1);

-- 采购需求表
CREATE TABLE IF NOT EXISTS purchase_request (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购需求表';

-- 采购订单表
CREATE TABLE IF NOT EXISTS purchase_order (
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
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_by BIGINT COMMENT '创建人',
    approved_by BIGINT COMMENT '审批人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- 插入测试采购需求数据
INSERT INTO purchase_request (request_no, title, request_type, department, requester_id, expected_date, total_amount, priority, reason, status, tenant_id) VALUES 
('REQ202405001', '办公设备采购', '设备', '行政部', 1, '2024-06-30', 50000.00, 2, '办公室需要更换一批新的办公设备', 'approved', 1),
('REQ202405002', '原材料采购', '物料', '生产部', 1, '2024-06-15', 200000.00, 1, '生产所需的原材料即将不足', 'quote', 1);

-- 插入测试采购订单数据
INSERT INTO purchase_order (order_no, supplier_id, order_type, order_amount, delivery_address, expected_date, payment_status, delivery_status, status, tenant_id, created_by) VALUES 
('PO202405001', 1, '常规采购', 48000.00, '北京市朝阳区xxx地址', '2024-06-25', 'unpaid', 'undelivered', 'pending', 1, 1),
('PO202405002', 2, '常规采购', 195000.00, '上海市浦东新区xxx地址', '2024-06-10', 'paid', 'delivered', 'completed', 1, 1);

-- 合同表
CREATE TABLE IF NOT EXISTS contract (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '合同ID',
    contract_no VARCHAR(50) NOT NULL UNIQUE COMMENT '合同编号',
    contract_name VARCHAR(200) NOT NULL COMMENT '合同名称',
    supplier_id BIGINT COMMENT '供应商ID',
    contract_type VARCHAR(50) COMMENT '合同类型',
    total_amount DECIMAL(15,2) COMMENT '合同金额',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    sign_date DATE COMMENT '签订日期',
    party_a VARCHAR(200) COMMENT '甲方',
    party_b VARCHAR(200) COMMENT '乙方',
    content TEXT COMMENT '合同内容',
    attachment_url VARCHAR(500) COMMENT '附件URL',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态：draft/active/completed/terminated',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';

-- 评估模板表
CREATE TABLE IF NOT EXISTS evaluation_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    template_name VARCHAR(200) NOT NULL COMMENT '模板名称',
    description TEXT COMMENT '描述',
    criteria TEXT COMMENT '评估标准',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评估模板表';

-- 供应商评估表
CREATE TABLE IF NOT EXISTS supplier_evaluation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评估ID',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    template_id BIGINT COMMENT '模板ID',
    evaluation_no VARCHAR(50) NOT NULL UNIQUE COMMENT '评估编号',
    evaluation_period VARCHAR(50) COMMENT '评估周期',
    evaluation_date DATE COMMENT '评估日期',
    total_score DECIMAL(5,2) COMMENT '总分',
    level VARCHAR(10) COMMENT '等级：A/B/C/D',
    evaluator VARCHAR(100) COMMENT '评估人',
    remarks TEXT COMMENT '备注',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态：draft/ongoing/completed',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id),
    FOREIGN KEY (template_id) REFERENCES evaluation_template(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商评估表';

-- 发票表
CREATE TABLE IF NOT EXISTS invoice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '发票ID',
    invoice_no VARCHAR(50) NOT NULL UNIQUE COMMENT '发票编号',
    supplier_id BIGINT COMMENT '供应商ID',
    order_id BIGINT COMMENT '订单ID',
    invoice_type VARCHAR(50) COMMENT '发票类型：special/normal',
    amount DECIMAL(15,2) COMMENT '金额',
    tax_amount DECIMAL(15,2) COMMENT '税额',
    total_amount DECIMAL(15,2) COMMENT '总金额',
    invoice_date DATE COMMENT '发票日期',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending/approved/rejected/paid',
    remarks TEXT COMMENT '备注',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发票表';

-- 付款申请表
CREATE TABLE IF NOT EXISTS payment_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    request_no VARCHAR(50) NOT NULL UNIQUE COMMENT '申请编号',
    supplier_id BIGINT COMMENT '供应商ID',
    invoice_id BIGINT COMMENT '发票ID',
    order_id BIGINT COMMENT '订单ID',
    amount DECIMAL(15,2) COMMENT '付款金额',
    payment_date DATE COMMENT '付款日期',
    payment_method VARCHAR(50) COMMENT '付款方式：bank/cash/check/online',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending/approved/rejected/paid',
    remarks TEXT COMMENT '备注',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='付款申请表';

-- 插入测试合同数据
INSERT INTO contract (contract_no, contract_name, supplier_id, contract_type, total_amount, start_date, end_date, party_a, party_b, status, tenant_id, created_by) VALUES 
('CON202405001', '设备采购合同', 1, 'purchase', 48000.00, '2024-06-01', '2024-12-31', '本公司', '北京科技有限公司', 'active', 1, 1),
('CON202405002', '原材料供应合同', 2, 'purchase', 200000.00, '2024-06-01', '2024-12-31', '本公司', '上海贸易公司', 'active', 1, 1);

-- 插入测试评估模板数据
INSERT INTO evaluation_template (template_name, description, criteria, status, tenant_id, created_by) VALUES 
('供应商季度评估模板', '用于供应商季度评估', '质量评分,交货评分,服务评分,价格评分', 1, 1, 1),
('供应商年度评估模板', '用于供应商年度评估', '质量评分,交货评分,服务评分,价格评分,财务评分', 1, 1, 1);

-- 插入测试供应商评估数据
INSERT INTO supplier_evaluation (supplier_id, template_id, evaluation_no, evaluation_period, evaluation_date, total_score, level, evaluator, status, tenant_id, created_by) VALUES 
(1, 1, 'EVA2024Q2001', '2024年第二季度', '2024-06-30', 88.50, 'A', '张经理', 'completed', 1, 1),
(2, 1, 'EVA2024Q2002', '2024年第二季度', '2024-06-30', 75.00, 'B', '张经理', 'completed', 1, 1);

-- 插入测试发票数据
INSERT INTO invoice (invoice_no, supplier_id, invoice_type, amount, tax_amount, total_amount, invoice_date, status, tenant_id, created_by) VALUES 
('INV202405001', 1, 'special', 42477.88, 5522.12, 48000.00, '2024-06-01', 'approved', 1, 1),
('INV202405002', 2, 'special', 172566.37, 22433.63, 195000.00, '2024-06-01', 'paid', 1, 1);

-- 插入测试付款申请数据
INSERT INTO payment_request (request_no, supplier_id, invoice_id, amount, payment_date, payment_method, status, tenant_id, created_by) VALUES 
('PAY202405001', 2, 2, 195000.00, '2024-06-15', 'bank', 'paid', 1, 1),
('PAY202405002', 1, 1, 48000.00, '2024-06-20', 'bank', 'approved', 1, 1);
