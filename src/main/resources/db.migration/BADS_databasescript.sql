IF DB_ID('bads_db') IS NULL
    BEGIN
        CREATE DATABASE bads_db;
    END;

------------------------------------------------------------
-- 1. Reference tables
------------------------------------------------------------
CREATE TABLE [Role] (
                        role_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        role_name NVARCHAR(50) NOT NULL
);

CREATE TABLE [CustomerType] (
                                type_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                type_name NVARCHAR(50)
);

CREATE TABLE [ProductCategory] (
                                   category_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                   category NVARCHAR(100) NOT NULL
);

CREATE TABLE [Unit] (
                        unit_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        unit_name NVARCHAR(50) NOT NULL
);

CREATE TABLE [SupplierCategory] (
                                    category_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                    category_name NVARCHAR(100) NOT NULL
);

CREATE TABLE [Qrcode] (
                          qr_id INT IDENTITY(1,1) PRIMARY KEY,
                          qr_image NVARCHAR(255) NOT NULL,
                          product_id BIGINT NULL,
                          warehouse_id BIGINT NULL
);

------------------------------------------------------------
-- 2. Core: User
------------------------------------------------------------
CREATE TABLE [User] (
                        user_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        user_code NVARCHAR(50) NOT NULL UNIQUE,
                        username NVARCHAR(50) NOT NULL UNIQUE,
                        [password] NVARCHAR(255) NOT NULL,
                        date_of_birth DATE NULL,
                        avatar NVARCHAR(255) NULL,
                        full_name NVARCHAR(100) NULL,
                        email NVARCHAR(100) NULL,
                        phone NVARCHAR(20) NULL,
                        [status] NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                        role_id BIGINT NULL,
                        gender NVARCHAR(16) NULL,
                        address NVARCHAR(255) NULL,
                        created_at DATETIME DEFAULT GETDATE(),
                        created_by BIGINT NOT NULL ,
                        CONSTRAINT FK_User_Role FOREIGN KEY (role_id) REFERENCES [Role](role_id)
);
CREATE INDEX IX_User_role ON [User](role_id);

------------------------------------------------------------
-- 3. Warehouse, Product, Inventory
------------------------------------------------------------
CREATE TABLE [Warehouse] (
                             warehouse_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                             warehouse_name NVARCHAR(100),
                             address NVARCHAR(255),
                             manager_id BIGINT NULL,
                             [status] NVARCHAR(20),
                             code NVARCHAR(20) NOT NULL UNIQUE,
                             phone NVARCHAR(20),
                             email NVARCHAR(100),
                             is_active BIT DEFAULT 1,
                             created_at DATETIME DEFAULT GETDATE(),
                             created_by BIGINT NOT NULL,
                             CONSTRAINT FK_Warehouse_Manager FOREIGN KEY (manager_id) REFERENCES [User](user_id)
);

CREATE TABLE [Product] (
                           product_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           sku NVARCHAR(50) NOT NULL UNIQUE,
                           [description] NVARCHAR(255),
                           barcode NVARCHAR(64),
                           image NVARCHAR(255),
                           name NVARCHAR(100) NOT NULL,
                           cost_price DECIMAL(18,2),
                           selling_price DECIMAL(18,2),
                           min_stock BIGINT DEFAULT 0,
                           max_stock BIGINT DEFAULT 0,
                           [status] NVARCHAR(20) DEFAULT 'ACTIVE',
                           created_at DATETIME DEFAULT GETDATE(),
                           created_by BIGINT NOT NULL,
                           category_id BIGINT,
                           note NVARCHAR(255),
                           unit_id BIGINT,
                           reorder_qty BIGINT DEFAULT 0,
                           CONSTRAINT FK_Product_Category FOREIGN KEY (category_id) REFERENCES [ProductCategory](p_category_id),
                           CONSTRAINT FK_Product_Unit FOREIGN KEY (unit_id) REFERENCES [Unit](unit_id),
                           CONSTRAINT FK_Product_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
CREATE INDEX IX_Product_category ON [Product](p_category_id);
CREATE INDEX IX_Product_unit ON [Product](p_unit_id);

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
                             created_at DATETIME DEFAULT GETDATE(),
                             created_by BIGINT NOT NULL,
                             CONSTRAINT FK_Inventory_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id),
                             CONSTRAINT FK_Inventory_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id),
                             CONSTRAINT FK_Inventory_QR FOREIGN KEY (qr_id) REFERENCES [Qrcode](qr_id),
                             CONSTRAINT CK_Inventory_nonneg CHECK (quantity >= 0 AND reserved_quantity >= 0)
);
CREATE INDEX IX_Inventory_product ON [Inventory](product_id);
CREATE INDEX IX_Inventory_updated ON [Inventory](last_in_at, last_out_at);

ALTER TABLE [Qrcode]
    ADD CONSTRAINT FK_QR_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id);
ALTER TABLE [Qrcode]
    ADD CONSTRAINT FK_QR_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id);

------------------------------------------------------------
-- 4. Customer
------------------------------------------------------------
CREATE TABLE [Customer] (
                            customer_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                            customer_code NVARCHAR(50) UNIQUE,
                            name NVARCHAR(100) NOT NULL,
                            address NVARCHAR(255),
                            type_id BIGINT,
                            email NVARCHAR(100),
                            phone NVARCHAR(20),
                            tax_code NVARCHAR(50),
                            [status] NVARCHAR(20) DEFAULT 'ACTIVE',
                            district NVARCHAR(100),
                            province NVARCHAR(100),
                            balance_limit DECIMAL(18,2),
                            current_balance DECIMAL(18,2) DEFAULT 0,
                            note NVARCHAR(255),
                            created_at DATETIME DEFAULT GETDATE(),
                            created_by BIGINT NULL,
                            CONSTRAINT FK_Customer_Type FOREIGN KEY (type_id) REFERENCES [CustomerType](type_id),
                            CONSTRAINT FK_Customer_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
CREATE INDEX IX_Customer_type ON [Customer](type_id);

------------------------------------------------------------
-- 5. Supplier
------------------------------------------------------------
CREATE TABLE [Supplier] (
                            supplier_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                            supplier_name NVARCHAR(100) NOT NULL,
                            contact_name NVARCHAR(100),
                            phone NVARCHAR(20),
                            email NVARCHAR(100),
                            address NVARCHAR(255),
                            tax_code NVARCHAR(50),
                            category_id BIGINT,
                            [status] NVARCHAR(20),
                            created_at DATETIME DEFAULT GETDATE(),
                            created_by BIGINT NOT NULL,
                            CONSTRAINT FK_Supplier_Category FOREIGN KEY (category_id) REFERENCES [SupplierCategory](category_id),
                            CONSTRAINT FK_Supplier_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id)
);

------------------------------------------------------------
-- 6. Sales
------------------------------------------------------------
CREATE TABLE [SalesOrder] (
                              order_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                              order_code NVARCHAR(50) UNIQUE,
                              customer_id BIGINT,
                              user_id BIGINT,
                              total_amount DECIMAL(18,2) DEFAULT 0,
                              payment_method NVARCHAR(50),
                              [status] NVARCHAR(20) DEFAULT 'Pending',
                              created_at DATETIME DEFAULT GETDATE(),
                              created_by BIGINT NOT NULL,
                              CONSTRAINT FK_SalesOrder_Customer FOREIGN KEY (customer_id) REFERENCES [Customer](customer_id),
                              CONSTRAINT FK_SalesOrder_User FOREIGN KEY (user_id) REFERENCES [User](user_id),
                              CONSTRAINT FK_SalesOrder_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
CREATE INDEX IX_SO_customer ON [SalesOrder](customer_id);

CREATE TABLE [SalesOrderDetail] (
                                    order_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                    order_id BIGINT NOT NULL,
                                    product_id BIGINT NOT NULL,
                                    quantity BIGINT NOT NULL,
                                    unit_price DECIMAL(18,2) NOT NULL,
                                    discount DECIMAL(5,2) DEFAULT 0,
                                    vat_amount DECIMAL(18,2),
                                    total_price AS (CONVERT(DECIMAL(18,2), ROUND((quantity * unit_price) + vat_amount - discount, 2))) PERSISTED,
                                    [status] NVARCHAR(20) DEFAULT 'Draft',
                                    note NVARCHAR(255),
                                    CONSTRAINT FK_SOD_Order FOREIGN KEY (order_id) REFERENCES [SalesOrder](order_id) ON DELETE CASCADE,
                                    CONSTRAINT FK_SOD_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id)
);
CREATE INDEX IX_SOD_order ON [SalesOrderDetail](order_id);

CREATE TABLE [Invoice] (
                           invoice_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           invoice_code NVARCHAR(50) UNIQUE NOT NULL,
                           order_id BIGINT UNIQUE NOT NULL,
                           vat_amount DECIMAL(18,2),
                           grand_total DECIMAL(18,2),
                           [status] NVARCHAR(20) DEFAULT 'Pending',
                           payment_method NVARCHAR(50),
                           created_at DATETIME DEFAULT GETDATE(),
                           created_by BIGINT NOT NULL,
                           CONSTRAINT FK_Invoice_Order FOREIGN KEY (order_id) REFERENCES [SalesOrder](order_id),
                           CONSTRAINT FK_Invoice_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id)
);

CREATE TABLE [Request] (
                           request_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           request_code NVARCHAR(50) UNIQUE,
                           order_id BIGINT,
                           customer_id BIGINT,
                           request_status NVARCHAR(100),
                           request_type NVARCHAR(20) NOT NULL,
                           reason NVARCHAR(255),
                           reason_detail NVARCHAR(255),
                           created_at DATETIME DEFAULT GETDATE(),
                           created_by BIGINT NULL,
                           CONSTRAINT FK_Request_Order FOREIGN KEY (order_id) REFERENCES [SalesOrder](order_id),
                           CONSTRAINT FK_Request_Customer FOREIGN KEY (customer_id) REFERENCES [Customer](customer_id)
);

CREATE TABLE [RequestDetail] (
                                 request_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                 request_id BIGINT NOT NULL,
                                 order_detail_id BIGINT NOT NULL,
                                 quantity BIGINT NOT NULL,
--                                  reason_for_item NVARCHAR(255),
                                 CONSTRAINT FK_RequestDetail_Request FOREIGN KEY (request_id) REFERENCES [Request](request_id) ON DELETE CASCADE,
                                 CONSTRAINT FK_RequestDetail_OrderDetail FOREIGN KEY (order_detail_id) REFERENCES [SalesOrderDetail](order_detail_id)
);

------------------------------------------------------------
-- 7. Contract (replaces Contract)
------------------------------------------------------------
CREATE TABLE [Contract] (
                            contract_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                            contract_code NVARCHAR(50) UNIQUE,
                            supplier_id BIGINT NOT NULL,
                            user_id BIGINT NULL,
                            [status] NVARCHAR(20) DEFAULT 'Pending',
                            note NVARCHAR(255),
                            created_at DATETIME DEFAULT GETDATE(),
                            created_by BIGINT NOT NULL,
                            CONSTRAINT FK_Contract_Supplier FOREIGN KEY (supplier_id) REFERENCES [Supplier](supplier_id),
                            CONSTRAINT FK_Contract_User FOREIGN KEY (user_id) REFERENCES [User](user_id),
                            CONSTRAINT FK_Contract_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id)
);

CREATE TABLE [ContractDetail] (
                                  contract_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                  contract_id BIGINT NOT NULL,
                                  product_id BIGINT NOT NULL,
                                  quantity BIGINT NOT NULL,
                                  unit_price DECIMAL(18,2) NOT NULL,
                                  vat_amount DECIMAL(18,2),
                                  total_price AS (quantity * unit_price) + vat_amount PERSISTED,
                                  [status] NVARCHAR(20) DEFAULT 'Pending',
                                  estimated_delivery_date DATE NULL,
                                  note NVARCHAR(255),
                                  CONSTRAINT FK_CD_Contract FOREIGN KEY (contract_id) REFERENCES [Contract](contract_id) ON DELETE CASCADE,
                                  CONSTRAINT FK_CD_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id)
);

------------------------------------------------------------
-- 8. Goods Receipt / Issues
------------------------------------------------------------
CREATE TABLE [GoodsReceipt] (
                                receipt_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                receipt_code NVARCHAR(50) UNIQUE,
                                contract_id BIGINT NULL,
                                warehouse_id BIGINT NOT NULL,
                                created_at DATETIME DEFAULT GETDATE(),
                                created_by BIGINT NOT NULL,
                                posted_at DATETIME NULL,
                                posted_by BIGINT NULL,
                                [status] NVARCHAR(20) DEFAULT 'Draft',
                                note NVARCHAR(255),
                                CONSTRAINT FK_GR_Contract FOREIGN KEY (contract_id) REFERENCES [Contract](contract_id),
                                CONSTRAINT FK_GR_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id),
                                CONSTRAINT FK_GR_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
                                CONSTRAINT FK_GR_PostedBy FOREIGN KEY (posted_by) REFERENCES [User](user_id)
);

CREATE TABLE [GoodsReceiptDetail] (
                                      receipt_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                      receipt_id BIGINT NOT NULL,
                                      product_id BIGINT NOT NULL,
                                      quantity BIGINT NOT NULL,
                                      manufacture_date DATE NULL,
                                      expiry_date DATE NULL,
                                      [status] NVARCHAR(20),
                                      note NVARCHAR(255),
                                      CONSTRAINT FK_GRD_Receipt FOREIGN KEY (receipt_id) REFERENCES [GoodsReceipt](receipt_id) ON DELETE CASCADE,
                                      CONSTRAINT FK_GRD_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id)
);

CREATE TABLE [GoodsIssues] (
                               issue_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                               issue_code NVARCHAR(50) UNIQUE,
                               warehouse_id BIGINT NOT NULL,
                               order_id BIGINT NULL,
                               created_at DATETIME DEFAULT GETDATE(),
                               created_by BIGINT NOT NULL,
                               posted_at DATETIME NULL,
                               posted_by BIGINT NULL,
                               [status] NVARCHAR(20) DEFAULT 'Draft',
                               note NVARCHAR(255),
                               CONSTRAINT FK_GI_Warehouse FOREIGN KEY (warehouse_id) REFERENCES [Warehouse](warehouse_id),
                               CONSTRAINT FK_GI_Order FOREIGN KEY (order_id) REFERENCES [SalesOrder](order_id),
                               CONSTRAINT FK_GI_CreatedBy FOREIGN KEY (created_by) REFERENCES [User](user_id),
                               CONSTRAINT FK_GI_PostedBy FOREIGN KEY (posted_by) REFERENCES [User](user_id)
);

CREATE TABLE [GoodsIssuesDetail] (
                                     issue_detail_id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                     issue_id BIGINT NOT NULL,
                                     product_id BIGINT NOT NULL,
                                     quantity BIGINT NOT NULL,
                                     [status] NVARCHAR(20) DEFAULT 'Draft',
                                     note NVARCHAR(255),
                                     CONSTRAINT FK_GID_Issue FOREIGN KEY (issue_id) REFERENCES [GoodsIssues](issue_id) ON DELETE CASCADE,
                                     CONSTRAINT FK_GID_Product FOREIGN KEY (product_id) REFERENCES [Product](product_id)
);

------------------------------------------------------------
-- 9. Zalo integration
------------------------------------------------------------
CREATE TABLE [ZaloCustomerLink] (
                                    zalo_user_id NVARCHAR(64) PRIMARY KEY,
                                    customer_id BIGINT NULL,
                                    follow_status NVARCHAR(20) DEFAULT 'unknown',
                                    consent_at DATETIME NULL,
                                    created_at DATETIME DEFAULT GETDATE(),
                                    updated_at DATETIME NULL,
                                    CONSTRAINT FK_ZCL_Customer FOREIGN KEY (customer_id) REFERENCES [Customer](customer_id)
);
CREATE INDEX IX_ZCL_customer ON [ZaloCustomerLink](customer_id);

CREATE TABLE [ZaloEventLog] (
                                id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
                                event_id NVARCHAR(190),
                                event_name NVARCHAR(190),
                                signature NVARCHAR(256),
                                received_at DATETIME DEFAULT GETDATE(),
                                processed_at DATETIME NULL,
                                [status] NVARCHAR(40) DEFAULT 'RECEIVED',
                                payload NVARCHAR(MAX) NOT NULL,
                                created_at DATETIME DEFAULT GETDATE(),
                                updated_at DATETIME NULL,
                                created_by BIGINT NULL,
                                updated_by BIGINT NULL,
                                CONSTRAINT FK_ZEL_created_by FOREIGN KEY (created_by) REFERENCES [User](user_id),
                                CONSTRAINT FK_ZEL_updated_by FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE UNIQUE INDEX IX_ZaloEventLog_event ON [ZaloEventLog](event_id) WHERE event_id IS NOT NULL;

------------------------------------------------------------
-- 10. Additional constraints & indexes
------------------------------------------------------------
CREATE INDEX IX_Product_sku ON [Product](sku);
CREATE INDEX IX_Customer_code ON [Customer](customer_code);
CREATE INDEX IX_Supplier_name ON [Supplier](supplier_name);

ALTER TABLE [Product] ADD CONSTRAINT CK_Product_prices_nonneg CHECK (cost_price >= 0 AND selling_price >= 0);
ALTER TABLE [SalesOrderDetail] ADD CONSTRAINT CK_SOD_quantity_nonneg CHECK (quantity >= 0);
ALTER TABLE [ContractDetail] ADD CONSTRAINT CK_CD_quantity_nonneg CHECK (quantity >= 0);

ALTER TABLE [SalesOrder] ADD CONSTRAINT DF_SalesOrder_status DEFAULT 'Pending' FOR [status];
ALTER TABLE [Contract] ADD CONSTRAINT DF_Contract_status DEFAULT 'Pending' FOR [status];
ALTER TABLE [Invoice] ADD CONSTRAINT DF_Invoice_status DEFAULT 'Pending' FOR [status];
