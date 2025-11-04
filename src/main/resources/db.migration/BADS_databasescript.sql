-- CREATE DATABASE and use
IF DB_ID('bads_db') IS NULL
BEGIN
    CREATE DATABASE bads_db;
END
GO
USE bads_db;
GO

/****** 1. Reference tables ******/
CREATE TABLE [Role] (
    role_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    role_name NVARCHAR(50) NOT NULL
);
GO

CREATE TABLE [CustomerType] (
    type_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    type_name NVARCHAR(50) NULL
);
GO

CREATE TABLE [ProductCategory] (
    p_category_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    p_category NVARCHAR(100) NOT NULL
);
GO

CREATE TABLE [Unit] (
    unit_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    unit_name NVARCHAR(50) NOT NULL
);
GO

CREATE TABLE [SupplierCategory] (
    s_category_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    s_category_name NVARCHAR(100) NOT NULL
);
GO

CREATE TABLE [Qrcode] (
    qr_id INT IDENTITY(1,1) PRIMARY KEY,
    qr_image NVARCHAR(255) NOT NULL,
    product_id BIGINT NULL,
    warehouse_id BIGINT NULL
);
GO

/****** 2. Core: User and details ******/
CREATE TABLE [User] (
    user_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_code NVARCHAR(50) NOT NULL UNIQUE,
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    date_of_birth DATE NULL,
    avatar NVARCHAR(255) NULL,
    full_name NVARCHAR(100) NULL,
    email NVARCHAR(100) NULL,
    phone NVARCHAR(20) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    role_id BIGINT NULL,
    gender NVARCHAR(16) NULL,
    address NVARCHAR(255) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    CONSTRAINT FK_User_Role FOREIGN KEY (role_id) REFERENCES [Role](role_id)
);
CREATE INDEX IX_User_role ON [User](role_id);
GO

/****** 3. Warehouse, Product, ProductDetail, Inventory ******/
CREATE TABLE [Warehouse] (
    warehouse_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    warehouse_name NVARCHAR(100) NULL,
    address NVARCHAR(255) NULL,
    manager_id BIGINT NULL,
    status NVARCHAR(20) NULL,
    code NVARCHAR(20) NOT NULL UNIQUE,
    phone NVARCHAR(20) NULL,
    email NVARCHAR(100) NULL,
    is_active BIT DEFAULT 1,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    CONSTRAINT FK_Warehouse_Manager FOREIGN KEY (manager_id) REFERENCES [User](user_id)
);
GO

CREATE TABLE [Product] (
    product_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    sku NVARCHAR(50) NOT NULL UNIQUE,
    description NVARCHAR(255) NULL,
    barcode NVARCHAR(64) NULL,
    image NVARCHAR(255) NULL,
    name NVARCHAR(100) NOT NULL,
    cost_price DECIMAL(18,2) NULL,
    selling_price DECIMAL(18,2) NULL,
    min_stock BIGINT DEFAULT 0,
    max_stock BIGINT DEFAULT 0,
    status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    p_category_id BIGINT NULL,
    p_unit_id BIGINT NULL,
    reorder_qty BIGINT DEFAULT 0,
    CONSTRAINT FK_Product_Category FOREIGN KEY (p_category_id) REFERENCES [ProductCategory](p_category_id),
    CONSTRAINT FK_Product_Unit FOREIGN KEY (p_unit_id) REFERENCES [Unit](unit_id),
    CONSTRAINT FK_Product_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_Product_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE INDEX IX_Product_category ON [Product](p_category_id);
CREATE INDEX IX_Product_unit ON [Product](p_unit_id);
GO

CREATE TABLE [ProductDetail] (
    productdetail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    min_stock BIGINT DEFAULT 0,
    max_stock BIGINT DEFAULT 0,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    p_note NVARCHAR(255) NULL,
    status NVARCHAR(50) DEFAULT 'ACTIVE',
    CONSTRAINT FK_ProductDetail_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id),
    CONSTRAINT FK_ProductDetail_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_ProductDetail_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
GO

CREATE TABLE [Inventory] (
    inventory_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    qr_id INT NULL,
    quantity BIGINT DEFAULT 0,
    reserved_quantity BIGINT DEFAULT 0,
    safety_stock BIGINT DEFAULT 0,
    manufacture_date DATE NULL,
    expiry_date DATE NULL,
    last_in_at DATETIME NULL,
    last_out_at DATETIME NULL,
    CONSTRAINT FK_Inventory_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id),
    CONSTRAINT FK_Inventory_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id),
    CONSTRAINT FK_Inventory_QR FOREIGN KEY (qr_id) REFERENCES [Qrcode](qr_id),
    CONSTRAINT CK_Inventory_nonneg CHECK (quantity >= 0 AND reserved_quantity >= 0)
);
CREATE INDEX IX_Inventory_product ON [Inventory](product_id);
CREATE INDEX IX_Inventory_updated ON [Inventory](last_in_at, last_out_at);
GO

-- Update Qrcode foreign keys now that Product & Warehouse exist
ALTER TABLE [Qrcode]
ADD CONSTRAINT FK_QR_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id);
GO
ALTER TABLE [Qrcode]
ADD CONSTRAINT FK_QR_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id);
GO

/****** 4. Customer and detail ******/
CREATE TABLE [Customer] (
    customer_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    customer_code NVARCHAR(50) UNIQUE NULL,
    name NVARCHAR(100) NOT NULL,
    address NVARCHAR(255) NULL,
    type_id BIGINT NULL,
    email NVARCHAR(100) NULL,
    phone NVARCHAR(20) NULL,
    tax_code NVARCHAR(50) NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    district NVARCHAR(100) NULL,
    province NVARCHAR(100) NULL,
    balance_limit DECIMAL(18,2) NULL,
    current_balance DECIMAL(18,2) DEFAULT 0,
    note NVARCHAR(255) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT FK_Customer_Type FOREIGN KEY (type_id) REFERENCES [CustomerType](type_id),
    CONSTRAINT FK_Customer_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_Customer_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE INDEX IX_Customer_type ON [Customer](type_id);
GO

/****** 5. Supplier and detail ******/
CREATE TABLE [Supplier] (
    supplier_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    supplier_name NVARCHAR(100) NOT NULL,
    contact_name NVARCHAR(100) NULL,
    phone NVARCHAR(20) NULL,
    email NVARCHAR(100) NULL,
    address NVARCHAR(255) NULL,
    tax_code NVARCHAR(50) NULL,
    s_category_id BIGINT NULL,
    status NVARCHAR(20) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT FK_Supplier_Category FOREIGN KEY (s_category_id) REFERENCES [SupplierCategory](s_category_id),
    CONSTRAINT FK_Supplier_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_Supplier_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
GO

/****** 6. Sales (Orders, Details, Invoice, Request) ******/
CREATE TABLE [SalesOrder] (
    order_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    saleorder_code NVARCHAR(50) UNIQUE NULL,
    customer_id BIGINT NULL,
    user_id BIGINT NULL, -- người tạo đơn (User)
    total_amount DECIMAL(18,2) DEFAULT 0,
    note NVARCHAR(255) NULL,
    payment_method NVARCHAR(50) NULL,
    status NVARCHAR(20) DEFAULT 'Pending',
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT FK_SalesOrder_Customer FOREIGN KEY (customer_id) REFERENCES [Customer](customer_id),
    CONSTRAINT FK_SalesOrder_User FOREIGN KEY (user_id) REFERENCES [User](user_id),
    CONSTRAINT FK_SalesOrder_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_SalesOrder_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE INDEX IX_SO_customer ON [SalesOrder](customer_id);
GO

CREATE TABLE [SalesOrderDetail] (
    order_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL,
    discount DECIMAL(5,2) DEFAULT 0,
    vat_amount DECIMAL(18,2) NULL,
    total_price AS (CONVERT(DECIMAL(18,2), ROUND((quantity * unit_price) * (1 - discount / 100.0), 2))) PERSISTED,
    status NVARCHAR(20) DEFAULT 'Draft',
    note NVARCHAR(255) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    CONSTRAINT FK_SOD_Order FOREIGN KEY (order_id) REFERENCES [SalesOrder](order_id) ON DELETE CASCADE,
    CONSTRAINT FK_SOD_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id)
);
CREATE INDEX IX_SOD_order ON [SalesOrderDetail](order_id);
GO

CREATE TABLE [Invoice] (
    invoice_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    invoice_code NVARCHAR(50) NOT NULL UNIQUE,
    order_id BIGINT NOT NULL UNIQUE, -- 1:1 with SalesOrder
    vat_amount DECIMAL(18,2) NULL,
    grand_total DECIMAL(18,2) NULL,
    status NVARCHAR(20) DEFAULT 'Pending',
    payment_method NVARCHAR(50) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NOT NULL,
    updated_at DATETIME NULL,
    updated_by BIGINT NULL,
    CONSTRAINT FK_Invoice_Order FOREIGN KEY (order_id) REFERENCES [SalesOrder](order_id),
    CONSTRAINT FK_Invoice_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_Invoice_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
GO

CREATE TABLE [Request] (
    request_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_detail_id BIGINT NULL,
    customer_id BIGINT NULL,
    request_status NVARCHAR(100) NULL,
    reason NVARCHAR(255) NULL,
    reason_detail NVARCHAR(255) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NULL,
    CONSTRAINT FK_Request_OrderDetail FOREIGN KEY (order_detail_id) REFERENCES [SalesOrderDetail](order_detail_id),
    CONSTRAINT FK_Request_Customer FOREIGN KEY (customer_id) REFERENCES [Customer](customer_id)
);
GO

/****** 7. Purchase (PO) ******/
CREATE TABLE [PurchaseOrder] (
    po_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    po_code NVARCHAR(50) UNIQUE NULL,
    supplier_id BIGINT NOT NULL,
    user_id BIGINT NULL,
    status NVARCHAR(20) DEFAULT 'Pending',
    note NVARCHAR(255) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT FK_PO_Supplier FOREIGN KEY (supplier_id) REFERENCES [Supplier](supplier_id),
    CONSTRAINT FK_PO_User FOREIGN KEY (user_id) REFERENCES [User](user_id),
    CONSTRAINT FK_PO_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_PO_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
GO

CREATE TABLE [PurchaseOrderDetail] (
    po_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    po_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL,
    vat_amount DECIMAL(18,2) NULL,
    total_price AS (quantity * unit_price) PERSISTED,
    po_status NVARCHAR(20) DEFAULT 'Pending',
    estimated_delivery_date DATE NULL,
    po_note NVARCHAR(255) NULL,
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT FK_POD_PO FOREIGN KEY (po_id) REFERENCES [PurchaseOrder](po_id) ON DELETE CASCADE,
    CONSTRAINT FK_POD_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id),
    CONSTRAINT FK_POD_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
GO

/****** 8. Goods Receipt / Goods Issue ******/
CREATE TABLE [GoodsReceipt] (
    receipt_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    receipt_code NVARCHAR(50) UNIQUE NULL,
    po_id BIGINT NULL,
    warehouse_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NOT NULL,
    posted_at DATETIME NULL,
    posted_by BIGINT NULL,
    status NVARCHAR(20) DEFAULT 'Draft',
    note NVARCHAR(255) NULL,
    CONSTRAINT FK_GR_PO FOREIGN KEY (po_id) REFERENCES [PurchaseOrder](po_id),
    CONSTRAINT FK_GR_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id),
    CONSTRAINT FK_GR_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_GR_PostedBy FOREIGN KEY (posted_by) REFERENCES [User](user_id)
);
GO

CREATE TABLE [GoodsReceiptDetail] (
    receipt_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    receipt_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    manufacture_date DATE NULL,
    expiry_date DATE NULL,
    receipt_status NVARCHAR(20) NULL,
    receipt_note NVARCHAR(255) NULL,
    CONSTRAINT FK_GRD_Receipt FOREIGN KEY (receipt_id) REFERENCES [GoodsReceipt](receipt_id) ON DELETE CASCADE,
    CONSTRAINT FK_GRD_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id)
);
GO

CREATE TABLE [GoodsIssues] (
    issue_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    issue_code NVARCHAR(50) UNIQUE NULL,
    warehouse_id BIGINT NOT NULL,
    order_id BIGINT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    created_by BIGINT NOT NULL,
    posted_at DATETIME NULL,
    posted_by BIGINT NULL,
    updated_at DATETIME NULL,
    updated_by BIGINT NULL,
    status NVARCHAR(20) DEFAULT 'Draft',
    note NVARCHAR(255) NULL,
    CONSTRAINT FK_GI_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id),
    CONSTRAINT FK_GI_Order FOREIGN KEY (order_id) REFERENCES [SalesOrder](order_id),
    CONSTRAINT FK_GI_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_GI_PostedBy FOREIGN KEY (posted_by) REFERENCES [User](user_id),
    CONSTRAINT FK_GI_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
GO

CREATE TABLE [GoodsIssuesDetail] (
    issue_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    issue_id BIGINT NOT NULL,
    inventory_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    issue_status NVARCHAR(20) DEFAULT 'Draft',
    issue_note NVARCHAR(255) NULL,
    CONSTRAINT FK_GID_Issue FOREIGN KEY (issue_id) REFERENCES [GoodsIssues](issue_id) ON DELETE CASCADE,
    CONSTRAINT FK_GID_Inventory FOREIGN KEY (inventory_id) REFERENCES [Inventory](inventory_id)
);
GO

/****** 9. Contract and ContractDetail (from ERD) ******/
CREATE TABLE [Contract] (
    contract_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contract_code NVARCHAR(100) UNIQUE NULL,
    invoice_ref NVARCHAR(100) NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_by BIGINT NULL,
    updated_at DATETIME NULL,
    note NVARCHAR(255) NULL,
    CONSTRAINT FK_Contract_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_Contract_UpdatedBy FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
GO

CREATE TABLE [ContractDetail] (
    contract_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DECIMAL(18,2) NULL,
    estimated_delivery_date DATE NULL,
    quantity BIGINT NULL,
    contract_status NVARCHAR(50) NULL,
    contract_note NVARCHAR(255) NULL,
    CONSTRAINT FK_CD_Contract FOREIGN KEY (contract_id) REFERENCES [Contract](contract_id) ON DELETE CASCADE,
    CONSTRAINT FK_CD_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id)
);
GO

/****** 10. Zalo integration tables ******/
CREATE TABLE [ZaloCustomerLink] (
    zalo_user_id NVARCHAR(64) PRIMARY KEY,
    customer_id BIGINT NULL,
    follow_status NVARCHAR(20) NOT NULL DEFAULT 'unknown',
    consent_at DATETIME NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    CONSTRAINT FK_ZCL_Customer FOREIGN KEY (customer_id) REFERENCES [Customer](customer_id)
);
CREATE INDEX IX_ZCL_customer ON [ZaloCustomerLink](customer_id);
GO

CREATE TABLE [ZaloEventLog] (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    event_id NVARCHAR(190) NULL,
    event_name NVARCHAR(190) NULL,
    signature NVARCHAR(256) NULL,
    received_at DATETIME DEFAULT GETDATE(),
    processed_at DATETIME NULL,
    status NVARCHAR(40) NOT NULL DEFAULT 'RECEIVED',
    payload NVARCHAR(MAX) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    CONSTRAINT FK_ZEL_created_by FOREIGN KEY (created_by) REFERENCES [User](user_id),
    CONSTRAINT FK_ZEL_updated_by FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE UNIQUE INDEX IX_ZaloEventLog_event ON [ZaloEventLog](event_id) WHERE event_id IS NOT NULL;
GO

/****** 11. Defaults / Misc indexes and checks ******/
-- Useful indexes
CREATE INDEX IX_Product_sku ON [Product](sku);
CREATE INDEX IX_Customer_code ON [Customer](customer_code);
CREATE INDEX IX_Supplier_name ON [Supplier](supplier_name);
GO

-- Additional CHECK constraints
ALTER TABLE [Product] ADD CONSTRAINT CK_Product_prices_nonneg CHECK (cost_price >= 0 AND selling_price >= 0);
ALTER TABLE [SalesOrderDetail] ADD CONSTRAINT CK_SOD_quantity_nonneg CHECK (quantity >= 0);
ALTER TABLE [PurchaseOrderDetail] ADD CONSTRAINT CK_POD_quantity_nonneg CHECK (quantity >= 0);
GO

-- Ensure consistent default statuses
ALTER TABLE [SalesOrder] ADD CONSTRAINT DF_SalesOrder_status DEFAULT 'Pending' FOR status;
ALTER TABLE [PurchaseOrder] ADD CONSTRAINT DF_PurchaseOrder_status DEFAULT 'Pending' FOR status;
ALTER TABLE [Invoice] ADD CONSTRAINT DF_Invoice_status DEFAULT 'Pending' FOR status;
GO

/****** End of schema ******/
-- You chose NO stored procedures; none are created here.
GO
