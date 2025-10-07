
CREATE DATABASE bads_db;
GO
USE bads_db;
GO


CREATE TABLE [User] (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    [password] VARCHAR(255) NOT NULL,
    full_name NVARCHAR(100),
    status BIT DEFAULT 1,
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) CHECK (role IN ('admin','sale','accountant','warehouse')) DEFAULT 'sale',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
GO


CREATE TABLE Warehouse (
    warehouse_id INT IDENTITY(1,1) PRIMARY KEY,
    warehouse_name NVARCHAR(100) NOT NULL,
    address NVARCHAR(200),
    manager_id INT,
    status BIT DEFAULT 1,
    FOREIGN KEY (manager_id) REFERENCES [User](user_id)
);
GO


CREATE TABLE Customer (
    customer_id INT IDENTITY(1,1) PRIMARY KEY,
    [name] NVARCHAR(100) NOT NULL,
    [address] NVARCHAR(200),
    [type] VARCHAR(20) CHECK ([type] IN ('cá nhân','doanh nghiệp')) DEFAULT 'cá nhân',
    email VARCHAR(100),
    tax_code VARCHAR(20),
    phone VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
GO


CREATE TABLE Supplier (
    supplier_id INT IDENTITY(1,1) PRIMARY KEY,
    supplier_name NVARCHAR(100) NOT NULL,
    contact_name NVARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    [address] NVARCHAR(200),
    tax_code VARCHAR(20),
    [status] BIT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
GO


CREATE TABLE Product (
    product_id INT IDENTITY(1,1) PRIMARY KEY,
    [name] NVARCHAR(100) NOT NULL,
    unit NVARCHAR(20),
    category NVARCHAR(50),
    cost_price DECIMAL(18,2),
    selling_price DECIMAL(18,2),
    stock_quantity INT DEFAULT 0,
    min_stock INT DEFAULT 0,
    max_stock INT DEFAULT 0,
    [status] BIT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
GO


CREATE TABLE SalesOrder (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    order_date DATE,
    customer_id INT,
    [status] VARCHAR(20) CHECK ([status] IN ('draft','confirmed','delivered','cancelled')) DEFAULT 'draft',
    user_id INT,
    total_amount DECIMAL(18,2),
    note NVARCHAR(200),
    payment_method NVARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (user_id) REFERENCES [User](user_id)
);
GO


CREATE TABLE Invoice (
    invoice_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    invoice_date DATE,
    vat_rate DECIMAL(5,2),
    vat_amount DECIMAL(18,2),
    grand_total DECIMAL(18,2),
    created_by INT,
    FOREIGN KEY (order_id) REFERENCES SalesOrder(order_id),
    FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
GO


CREATE TABLE PurchaseOrder (
    po_id INT IDENTITY(1,1) PRIMARY KEY,
    supplier_id INT,
    invoice_ref VARCHAR(50),
    po_date DATE,
    total_amount DECIMAL(18,2),
    debit_account VARCHAR(20),
    credit_account VARCHAR(20),
    qty_document DECIMAL(18,2),
    qty_received DECIMAL(18,2),
    created_by INT,
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id),
    FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
GO


CREATE TABLE ExportNote (
    export_note_id INT IDENTITY(1,1) PRIMARY KEY,
    export_date DATE,
    warehouse_id INT,
    reason NVARCHAR(200),
    total_quantity DECIMAL(18,2),
    created_by INT,
    note NVARCHAR(200),
    FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id),
    FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
GO


CREATE TABLE SalesOrderItem (
    order_item_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity DECIMAL(18,2),
    unit_price DECIMAL(18,2),
    line_total DECIMAL(18,2),
    FOREIGN KEY (order_id) REFERENCES SalesOrder(order_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);
GO


CREATE TABLE PurchaseOrderItem (
    po_item_id INT IDENTITY(1,1) PRIMARY KEY,
    po_id INT,
    product_id INT,
    quantity DECIMAL(18,2),
    unit_price DECIMAL(18,2),
    line_total DECIMAL(18,2),
    FOREIGN KEY (po_id) REFERENCES PurchaseOrder(po_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);
GO
INSERT INTO [User] (username, [password], full_name, role, email, phone)
VALUES
('admin', '123456', N'Nguyễn Văn A', 'admin', 'admin@company.vn', '0901111111'),
('sale01', '123456', N'Lê Thị B', 'sale', 'le.b@company.vn', '0902222222'),
('account01', '123456', N'Trần Văn C', 'accountant', 'tran.c@company.vn', '0903333333');
INSERT INTO Warehouse (warehouse_name, address, manager_id)
VALUES 
(N'Kho Nguyên Vật Liệu', N'Hà Nội', 1),
(N'Kho Thành Phẩm', N'Hồ Chí Minh', 2);
INSERT INTO Customer ([name], [address], [type], email, phone, tax_code)
VALUES
(N'Công ty TNHH ABC', N'Đống Đa, Hà Nội', 'doanh nghiệp', 'contact@abc.vn', '0904444444', '0101234567'),
(N'Nguyễn Văn D', N'Thanh Xuân, Hà Nội', 'cá nhân', 'nguyenvand@gmail.com', '0905555555', NULL);
INSERT INTO Supplier (supplier_name, contact_name, phone, email, [address], tax_code)
VALUES
(N'Công ty TNHH Morinaga Milk Việt Nam', N'Trần Thị E', '0906666666', 'morinaga@supply.vn', N'Bắc Ninh', '2301234567'),
(N'Công ty CP Sữa Vinamilk', N'Phạm Văn F', '0907777777', 'vinamilk@supply.vn', N'Hồ Chí Minh', '0308765432');
INSERT INTO Product ([name], unit, category, cost_price, selling_price, stock_quantity)
VALUES
(N'Phô mai Mozzarella', N'kg', N'Nguyên vật liệu', 184300.00, 250000.00, 100),
(N'Sữa đặc có đường', N'lốc', N'Thành phẩm', 20000.00, 30000.00, 300);
INSERT INTO PurchaseOrder (supplier_id, invoice_ref, po_date, total_amount, debit_account, credit_account, qty_document, qty_received, created_by)
VALUES
(1, 'HD141', '2025-03-18', 62662000.00, '156', '331', 340.00, 340.00, 3);
INSERT INTO SalesOrder (order_date, customer_id, [status], user_id, total_amount, payment_method, note)
VALUES
('2025-03-20', 1, 'confirmed', 2, 12500000.00, N'Tiền mặt', N'Bán hàng trực tiếp'),
('2025-03-21', 2, 'confirmed', 2, 6000000.00, N'Chuyển khoản', N'Bán online');
INSERT INTO Invoice (order_id, invoice_date, vat_rate, vat_amount, grand_total, created_by)
VALUES
(1, '2025-03-21', 10.00, 1250000.00, 13750000.00, 3),
(2, '2025-03-22', 8.00, 480000.00, 6480000.00, 3);
INSERT INTO ExportNote (export_date, warehouse_id, reason, total_quantity, created_by, note)
VALUES
('2025-03-21', 2, N'Xuất hàng bán cho khách', 50.00, 2, N'Giao hàng cho khách ABC'),
('2025-03-22', 2, N'Xuất hàng cho đơn online', 20.00, 2, N'Giao khách Nguyễn Văn D');
INSERT INTO SalesOrderItem (order_id, product_id, quantity, unit_price, line_total)
VALUES
(1, 2, 100, 125000.00, 12500000.00),
(2, 2, 200, 30000.00, 6000000.00);
INSERT INTO PurchaseOrderItem (po_id, product_id, quantity, unit_price, line_total)
VALUES
(1, 1, 340, 184300.00, 62662000.00);
