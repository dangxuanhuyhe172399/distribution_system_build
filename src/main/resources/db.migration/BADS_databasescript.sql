CREATE DATABASE bads_db;
GO
USE bads_db;
GO
CREATE TABLE Role (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    role_name NVARCHAR(50) NOT NULL
);

CREATE TABLE [User] (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(100),
    email NVARCHAR(100),
    phone NVARCHAR(20),
    status BIT DEFAULT 1,
    role_id INT,
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

CREATE TABLE Product (
    product_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    category_id INT,
    unit_id INT,
    cost_price DECIMAL(18,2),
    selling_price DECIMAL(18,2),
    stock_quantity INT DEFAULT 0,
    min_stock INT DEFAULT 0,
    max_stock INT DEFAULT 0,
    status BIT DEFAULT 1,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES Category(category_id),
    FOREIGN KEY (unit_id) REFERENCES Unit(unit_id)
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
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (type_id) REFERENCES CustomerType(type_id)
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
    created_at DATETIME DEFAULT GETDATE()
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
    order_date DATETIME DEFAULT GETDATE(),
    status NVARCHAR(50),
    payment_method NVARCHAR(50),
    note NVARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (user_id) REFERENCES [User](user_id)
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
    po_date DATETIME DEFAULT GETDATE(),
    status NVARCHAR(50),
    note NVARCHAR(255),
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id),
    FOREIGN KEY (user_id) REFERENCES [User](user_id)
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
    invoice_date DATETIME DEFAULT GETDATE(),
    vat_rate DECIMAL(5,2),
    vat_amount DECIMAL(18,2),
    grand_total DECIMAL(18,2),
    created_by INT,
    FOREIGN KEY (order_id) REFERENCES SalesOrder(order_id),
    FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
CREATE TABLE ExportNote (
    export_note_id INT PRIMARY KEY IDENTITY(1,1),
    export_date DATETIME DEFAULT GETDATE(),
    warehouse_id INT,
    reason NVARCHAR(255),
    total_quantity INT,
    created_by INT,
    note NVARCHAR(255),
    FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id),
    FOREIGN KEY (created_by) REFERENCES [User](user_id)
);
