CREATE DATABASE bads_db;
GO
USE bads_db;
GO
CREATE TABLE Role (
                      role_id INT PRIMARY KEY IDENTITY(1,1),
                      role_name NVARCHAR(50) NOT NULL
);
---date_of_birth (ngày sinh)
--DATE
--avatar (ảnh đại diện)
--NVARCHAR(255) (lưu URL)
--user_code  NVARCHAR(50) NOT NULL UNIQUE hoac là tự động tạo
CREATE TABLE [User] (
                        user_id INT PRIMARY KEY IDENTITY(1,1),
                        user_code NVARCHAR(50) NOT NULL UNIQUE,
                        username NVARCHAR(50) NOT NULL UNIQUE,
                        password NVARCHAR(255) NOT NULL,
                        date_of_birth DATE NULL,
                        avatar NVARCHAR(255) NULL,
                        full_name NVARCHAR(100),
                        email NVARCHAR(100),
                        phone NVARCHAR(20),
                        status BIT DEFAULT 1,
                        role_id INT,
                        gender NVARCHAR(16) NULL,
                        address NVARCHAR(255) NULL,
                        created_at DATETIME DEFAULT GETDATE(),
                        updated_at DATETIME NULL,
                        FOREIGN KEY (role_id) REFERENCES Role(role_id)
);
CREATE TABLE Category (
                          category_id INT PRIMARY KEY IDENTITY(1,1),
                          category_name NVARCHAR(100) NOT NULL
);

CREATE TABLE Unit (
                      unit_id INT PRIMARY KEY IDENTITY(1,1),
                      unit_name NVARCHAR(50) NOT NULL
);
--sku (tốt nhất) — NVARCHAR(50) NOT NULL UNIQUE
CREATE TABLE Product (
                         product_id INT PRIMARY KEY IDENTITY(1,1),
                         sku NVARCHAR(50) NOT NULL UNIQUE,
                         name NVARCHAR(100) NOT NULL,
                         category_id INT,
                         unit_id INT,
                         cost_price DECIMAL(18,2),
                         selling_price DECIMAL(18,2),
                         stock_quantity INT DEFAULT 0,
                         min_stock INT DEFAULT 0,
                         max_stock INT DEFAULT 0,
                         status BIT DEFAULT 1, --Active
                         created_at DATETIME DEFAULT GETDATE(),
                         created_by INT NOT NULL,
                         updated_by INT NULL,
                         updated_at DATETIME NULL,
                         FOREIGN KEY (category_id) REFERENCES Category(category_id),
                         FOREIGN KEY (unit_id) REFERENCES Unit(unit_id),
                         FOREIGN KEY (created_by) REFERENCES [User](user_id),
                         FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE TABLE CustomerType (
                              type_id INT PRIMARY KEY IDENTITY(1,1),
                              type_name NVARCHAR(50)
);

CREATE TABLE Customer (
                          customer_id INT PRIMARY KEY IDENTITY(1,1),
                          name NVARCHAR(100) NOT NULL,
                          address NVARCHAR(255),
                          type_id INT,
                          email NVARCHAR(100),
                          phone NVARCHAR(20),
                          tax_code NVARCHAR(50),
                          status BIT DEFAULT 1,
                          created_at DATETIME DEFAULT GETDATE(),
                          created_by INT NOT NULL,
                          updated_by INT NULL,
                          updated_at DATETIME NULL,
                          FOREIGN KEY (type_id) REFERENCES CustomerType(type_id),
                          FOREIGN KEY (created_by) REFERENCES [User](user_id),
                          FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);

CREATE TABLE Supplier (
                          supplier_id INT PRIMARY KEY IDENTITY(1,1),
                          supplier_name NVARCHAR(100) NOT NULL,
                          contact_name NVARCHAR(100),
                          phone NVARCHAR(20),
                          email NVARCHAR(100),
                          address NVARCHAR(255),
                          tax_code NVARCHAR(50),
                          status BIT DEFAULT 1,
                          created_at DATETIME DEFAULT GETDATE(),
                          created_by INT NOT NULL,
                          updated_by INT NULL,
                          updated_at DATETIME NULL,
                          FOREIGN KEY (created_by) REFERENCES [User](user_id),
                          FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE TABLE Warehouse (
                           warehouse_id INT PRIMARY KEY IDENTITY(1,1),
                           warehouse_name NVARCHAR(100),
                           address NVARCHAR(255),
                           manager_id INT,
                           status BIT DEFAULT 1,
                           FOREIGN KEY (manager_id) REFERENCES [User](user_id)
);

CREATE TABLE Inventory (
                           inventory_id INT PRIMARY KEY IDENTITY(1,1),
                           warehouse_id INT,
                           product_id INT,
                           quantity INT DEFAULT 0,
                           FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id),
                           FOREIGN KEY (product_id) REFERENCES Product(product_id)
);
CREATE TABLE SalesOrder (
                            order_id INT PRIMARY KEY IDENTITY(1,1),
                            customer_id INT,
                            user_id INT,
                            status NVARCHAR(20) DEFAULT 'Pending',
                            payment_method NVARCHAR(50),
                            note NVARCHAR(255),
                            created_at DATETIME DEFAULT GETDATE(),
                            created_by INT NOT NULL,
                            updated_by INT NULL,
                            updated_at DATETIME NULL,
                            FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
                            FOREIGN KEY (user_id) REFERENCES [User](user_id),
                            FOREIGN KEY (created_by) REFERENCES [User](user_id),
                            FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);

CREATE TABLE SalesOrderDetail (
                                  order_detail_id INT PRIMARY KEY IDENTITY(1,1),
                                  order_id INT,
                                  product_id INT,
                                  quantity INT,
                                  unit_price DECIMAL(18,2),
                                  discount DECIMAL(5,2) DEFAULT 0,
                                  total_price AS ((quantity * unit_price) - ((quantity * unit_price) * discount / 100)) PERSISTED,
                                  FOREIGN KEY (order_id) REFERENCES SalesOrder(order_id),
                                  FOREIGN KEY (product_id) REFERENCES Product(product_id)
);
CREATE TABLE PurchaseOrder (
                               po_id INT PRIMARY KEY IDENTITY(1,1),
                               supplier_id INT,
                               user_id INT,
                               status NVARCHAR(20) DEFAULT 'Pending',
                               note NVARCHAR(255),
                               created_at DATETIME DEFAULT GETDATE(),
                               created_by INT NOT NULL,
                               updated_by INT NULL,
                               updated_at DATETIME NULL,
                               FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id),
                               FOREIGN KEY (user_id) REFERENCES [User](user_id),
                               FOREIGN KEY (created_by) REFERENCES [User](user_id),
                               FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);

CREATE TABLE PurchaseOrderDetail (
                                     po_detail_id INT PRIMARY KEY IDENTITY(1,1),
                                     po_id INT,
                                     product_id INT,
                                     quantity INT,
                                     unit_price DECIMAL(18,2),
                                     total_price AS (quantity * unit_price) PERSISTED,
                                     FOREIGN KEY (po_id) REFERENCES PurchaseOrder(po_id),
                                     FOREIGN KEY (product_id) REFERENCES Product(product_id)
);
CREATE TABLE Invoice (
                         invoice_id INT PRIMARY KEY IDENTITY(1,1),
                         order_id INT,
                         vat_rate DECIMAL(5,2),
                         vat_amount DECIMAL(18,2),
                         grand_total DECIMAL(18,2),
                         status NVARCHAR(20) DEFAULT 'Pending',
                         created_at DATETIME DEFAULT GETDATE(),
                         created_by INT NOT NULL,
                         updated_by INT NULL,
                         updated_at DATETIME NULL,
                         FOREIGN KEY (order_id) REFERENCES SalesOrder(order_id),
                         FOREIGN KEY (created_by) REFERENCES [User](user_id),
                         FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);
CREATE TABLE ExportNote (
                            export_note_id INT PRIMARY KEY IDENTITY(1,1),
                            warehouse_id INT,
                            reason NVARCHAR(255),
                            total_quantity INT,
                            status NVARCHAR(20) DEFAULT 'Draft',
                            created_at DATETIME DEFAULT GETDATE(),
                            created_by INT NOT NULL,
                            updated_by INT NULL,
                            updated_at DATETIME NULL,
                            note NVARCHAR(255),
                            FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id),
                            FOREIGN KEY (created_by) REFERENCES [User](user_id),
                            FOREIGN KEY (updated_by) REFERENCES [User](user_id)
);

--  INSERT ROLE
INSERT INTO Role (role_name) VALUES
                                 (N'admin'),               -- Quản trị toàn hệ thống
                                 (N'warehouseStaff'),     -- Nhân viên kho
                                 (N'saleStaff'),         -- Nhân viên bán hàng
                                 (N'accountstaff');    -- Nhân viên kế toán

-- INSERT UNIT (Đơn vị tính)
INSERT INTO Unit (unit_name) VALUES
                                 (N'Hộp'),
                                 (N'Gói'),
                                 (N'Khay'),
                                 (N'500gr'),
                                 (N'440gr'),
                                 (N'600gr'),
                                 (N'700gr'),
                                 (N'300gr'),
                                 (N'1200gr');

--INSERT CUSTOMER TYPE
INSERT INTO CustomerType (type_name) VALUES
                                         (N'Khách lẻ'),
                                         (N'Khách buôn');

--INSERT CATEGORY (Danh mục sản phẩm)
INSERT INTO Category (category_name) VALUES
                                         (N'Nem chua rán'),
                                         (N'Phô mai'),
                                         (N'Xúc xích'),
                                         (N'Đồ viên');

--INSERT WAREHOUSE
INSERT INTO Warehouse (warehouse_name, address, manager_id, status) VALUES
                                                                        (N'Kho Hà Nội', N'Hà Nội', NULL, 1),
                                                                        (N'Kho Hồ Chí Minh', N'Hồ Chí Minh', NULL, 1);

-----------------------------------------------------
-- TRIGGER FOR Product
-----------------------------------------------------
CREATE TRIGGER trg_Product_UpdateTime
    ON Product
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;
    UPDATE p
    SET updated_at = GETDATE()
    FROM Product p
             INNER JOIN inserted i ON p.product_id = i.product_id;
END;
GO

-----------------------------------------------------
-- TRIGGER FOR Customer
-----------------------------------------------------
CREATE TRIGGER trg_Customer_UpdateTime
    ON Customer
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;
    UPDATE c
    SET updated_at = GETDATE()
    FROM Customer c
             INNER JOIN inserted i ON c.customer_id = i.customer_id;
END;
GO

-----------------------------------------------------
-- TRIGGER FOR Supplier
-----------------------------------------------------
CREATE TRIGGER trg_Supplier_UpdateTime
    ON Supplier
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;
    UPDATE s
    SET updated_at = GETDATE()
    FROM Supplier s
             INNER JOIN inserted i ON s.supplier_id = i.supplier_id;
END;
GO

-----------------------------------------------------
-- TRIGGER FOR SalesOrder
-----------------------------------------------------
CREATE TRIGGER trg_SalesOrder_UpdateTime
    ON SalesOrder
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;
    UPDATE so
    SET updated_at = GETDATE()
    FROM SalesOrder so
             INNER JOIN inserted i ON so.order_id = i.order_id;
END;
GO

-----------------------------------------------------
-- TRIGGER FOR PurchaseOrder
-----------------------------------------------------
CREATE TRIGGER trg_PurchaseOrder_UpdateTime
    ON PurchaseOrder
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;
    UPDATE po
    SET updated_at = GETDATE()
    FROM PurchaseOrder po
             INNER JOIN inserted i ON po.po_id = i.po_id;
END;
GO

-----------------------------------------------------
-- TRIGGER FOR Invoice
-----------------------------------------------------
CREATE TRIGGER trg_Invoice_UpdateTime
    ON Invoice
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;
    UPDATE inv
    SET updated_at = GETDATE()
    FROM Invoice inv
             INNER JOIN inserted i ON inv.invoice_id = i.invoice_id;
END;
GO

-----------------------------------------------------
-- TRIGGER FOR ExportNote
-----------------------------------------------------
CREATE TRIGGER trg_ExportNote_UpdateTime
    ON ExportNote
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;
    UPDATE en
    SET updated_at = GETDATE()
    FROM ExportNote en
             INNER JOIN inserted i ON en.export_note_id = i.export_note_id;
END;
GO

