
USE [bads_db]
GO

-- 1. admin
INSERT INTO [dbo].[Role]
([role_name]
,[created_at]
,[updated_at])
VALUES
    ('admin'
    ,GETDATE()
    ,GETDATE())
GO

-- 2. warehouseStaff
INSERT INTO [dbo].[Role]
([role_name]
,[created_at]
,[updated_at])
VALUES
    ('warehouseStaff'
    ,GETDATE()
    ,GETDATE())
GO

-- 3. saleStaff
INSERT INTO [dbo].[Role]
([role_name]
,[created_at]
,[updated_at])
VALUES
    ('saleStaff'
    ,GETDATE()
    ,GETDATE())
GO

-- 4. accountstaff
INSERT INTO [dbo].[Role]
([role_name]
,[created_at]
,[updated_at])
VALUES
    ('accountstaff'
    ,GETDATE()
    ,GETDATE())
GO



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


-- 1. Người dùng Admin (role_id = 1)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('AD001', 'admin', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1990-05-15', NULL, N'Nguyễn Văn An', 'dangquanghuynh0108@gmail.com', '0901112222', 'ACTIVE', 1, N'MALE', N'123 Đường Láng, Hà Nội', GETDATE(), GETDATE())
GO

-- 2. Người dùng Kho (warehouseStaff - role_id = 2)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('WH002', 'kho_thanh', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1995-08-20', NULL, N'Trần Văn Thanh', 'thanh.tran@company.com', '0913334444', 'ACTIVE', 2, N'MALE', N'456 Trần Hưng Đạo, TP.HCM', GETDATE(), GETDATE())
GO

-- 3. Người dùng Bán hàng (saleStaff - role_id = 3)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('SL003', 'sale_huong', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1998-03-10', NULL, N'Lê Thị Hương', 'huong.le@company.com', '0975556666', 'ACTIVE', 3, N'FEMALE', N'789 Nguyễn Văn Linh, Đà Nẵng', GETDATE(), GETDATE())
GO

-- 4. Người dùng Kế toán (accountstaff - role_id = 4)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('AC004', 'ketoan_minh', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1992-11-25', NULL, N'Phạm Minh Tú', 'minh.pham@company.com', '0987778888', 'ACTIVE', 4, N'MALE', N'101 Phan Chu Trinh, Cần Thơ', GETDATE(), GETDATE())
GO
INSERT INTO [dbo].[Warehouse]
([warehouse_name], [address], [manager_id], [status], [code], [phone], [email], [is_active])
VALUES
    ('Hanoi Main Warehouse', '1A Industrial Park, Hanoi', 2, 'ACTIVE', 'WH-HN01', '02412345678', 'wh_hn@bads.com', 1),
    ('HCMC Distribution Center', '2B Logistics Zone, HCMC', 2, 'ACTIVE', 'WH-HCM01', '02887654321', 'wh_hcm@bads.com', 1);

INSERT INTO [dbo].[ProductCategory] ([p_category])
VALUES
    ('Electronics'),
    ('Office Supplies'),
    ('Furniture'),
    ('Food & Beverages');

INSERT INTO [dbo].[Unit] ([unit_name])
VALUES
    ('Piece'),
    ('Box'),
    ('Liter'),
    ('Kilogram');

INSERT INTO [dbo].[Product]
([sku], [description], [barcode], [name], [cost_price], [selling_price], [p_category_id], [p_unit_id], [created_by], [status])
VALUES
    ('EL001', 'Wireless Mouse with high precision', '1234567890123', 'Logitech MX Master', 50.00, 80.00, 1, 1, 1, 'ACTIVE'),
    ('OS005', 'Pack of 500 A4 white paper sheets', '9876543210987', 'A4 Printing Paper', 2.50, 4.00, 2, 2, 1, 'ACTIVE'),
    ('FB010', 'Premium roasted coffee beans 1kg', '5554443332221', 'Arabica Coffee 1kg', 15.00, 25.00, 4, 4, 1, 'ACTIVE'),
    ('FB011', 'Premium roasted coffee beans 1kg', '5554443332221', 'Arabica Coffee 1kg', 15.00, 25.00, 4, 4, 1, 'INACTIVE');

INSERT INTO [dbo].[CustomerType] ([type_name])
VALUES
    ('Individual'),
    ('SME'),
    ('Corporate'),
    ('Wholesale');

INSERT INTO [dbo].[Customer]
([customer_code], [name], [address], [type_id], [email], [phone], [tax_code], [district], [province], [created_by])
VALUES
    ('CUST001', 'Tech Solutions Corp', '500 Tech Blvd', 3, 'contact@techsol.com', '0912345678', '0100100100', 'District 1', 'HCMC', 3),
    ('CUST002', 'Green Leaf Store', '700 Green Lane', 2, 'info@greenleaf.vn', '0912345679', '0200200200', 'Ba Dinh', 'Hanoi', 3);

INSERT INTO [dbo].[SupplierCategory] ([s_category_name])
VALUES
    ('Local Manufacturer'),
    ('International Importer'),
    ('Wholesaler'),
    ('Service Provider');

INSERT INTO [dbo].[Supplier]
([supplier_name], [contact_name], [phone], [email], [address], [tax_code], [s_category_id], [created_by], [status])
VALUES
    ('Global Tech Imports', 'Mr. David Lee', '0987654321', 'david.lee@globaltech.com', '10 Overseas Road', '999888777', 2, 4, 'ACTIVE'),
    ('VN Office Supplies Co.', 'Ms. Hoa Tran', '0987654322', 'hoa.tran@vnoffice.vn', '20 Local Street', '111222333', 1, 4, 'ACTIVE');

-- Dữ liệu chèn vào bảng Inventory (Tồn kho)

INSERT INTO [dbo].[Inventory]
([warehouse_id], [product_id], [quantity], [reserved_quantity], [safety_stock], [manufacture_date], [expiry_date], [last_in_at], [last_out_at], [created_at], [updated_at])
VALUES
-- Sản phẩm 1 (Logitech MX Master) tại Kho 1 (Hanoi)
(1, 1, 100, 0, 10, NULL, NULL, GETDATE(), NULL, GETDATE(), GETDATE()),

-- Sản phẩm 2 (A4 Printing Paper) tại Kho 1 (Hanoi)
(1, 2, 500, 0, 50, '2025-01-01', NULL, GETDATE(), NULL, GETDATE(), GETDATE()),

-- Sản phẩm 3 (Arabica Coffee 1kg) tại Kho 2 (HCMC)
-- Có ngày hết hạn
(2, 3, 250, 0, 25, '2025-10-01', '2026-09-30', GETDATE(), NULL, GETDATE(), GETDATE()),

-- Sản phẩm 1 (Logitech MX Master) tại Kho 2 (HCMC)
(2, 1, 75, 5, 10, NULL, NULL, GETDATE(), NULL, GETDATE(), GETDATE());


-- Bước 1: Chèn một đơn hàng bán hàng (SalesOrder)
INSERT INTO [dbo].[SalesOrder]
([saleorder_code], [customer_id], [user_id], [note], [payment_method], [status], [created_by])
VALUES
    ('SO202511002', 1, 3, 'Đơn hàng mua 10 chiếc chuột và 20 thùng giấy.', 'Bank Transfer', 'NEW', 3)
        ,
    ('SO202511001', 1, 3, 'Đơn hàng mua 10 chiếc chuột và 20 thùng giấy.', 'Bank Transfer', 'NEW', 3);

-- Lấy Order ID vừa tạo để sử dụng trong SalesOrderDetail
DECLARE @NewOrderID BIGINT;
SET @NewOrderID = SCOPE_IDENTITY();

-- Bước 2: Chèn chi tiết đơn hàng (SalesOrderDetail)
INSERT INTO [dbo].[SalesOrderDetail]
([order_id], [product_id], [quantity], [unit_price], [discount], [vat_amount], [status], [note])
VALUES
-- Chi tiết 1: Logitech MX Master (ID 1)
(@NewOrderID, 1, 10, 80.00, 0.00, 80.00 * 0.1, 'Draft', 'Chuột không dây'),

-- Chi tiết 2: A4 Printing Paper (ID 2)
(@NewOrderID, 2, 20, 4.00, 5.00, 76.00 * 0.1, 'Draft', 'Giảm giá 5% cho đơn hàng số lượng lớn');

-- Bước 3: Cập nhật tổng số tiền (total_amount) vào bảng SalesOrder
-- Tổng tiền = SUM(total_price) cho tất cả chi tiết của đơn hàng này
UPDATE [dbo].[SalesOrder]
SET
    total_amount = (
        SELECT SUM(total_price)
        FROM [dbo].[SalesOrderDetail]
        WHERE order_id = @NewOrderID
    )
WHERE order_id = @NewOrderID;
