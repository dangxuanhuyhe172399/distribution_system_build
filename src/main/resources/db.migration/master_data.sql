
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


INSERT INTO [dbo].[SalesOrder]
([saleorder_code]
,[customer_id]
,[user_id]
,[total_amount]
,[note]
,[payment_method]
,[status]
,[created_at]
,[created_by]
,[updated_at])
VALUES
    ('SO2025001'           -- Mã đơn hàng
    ,1                     -- customer_id (Khách hàng 1)
    ,3                     -- user_id (Nhân viên Sale 3)
    ,53000000.00           -- total_amount (Tổng tiền 50M + 3M)
    ,N'Khách hàng yêu cầu giao hàng vào buổi tối.' -- Ghi chú
    ,N'Chuyển khoản'      -- payment_method
    ,'PENDING'             -- Trạng thái đơn hàng (Chờ xử lý)
    ,GETDATE()             -- created_at
    ,3                     -- created_by (user_id của người tạo)
    ,GETDATE())            -- updated_at
GO

INSERT INTO [dbo].[SalesOrderDetail]
 ([order_id]
 ,[product_id]
 ,[quantity]
 ,[unit_price]
 ,[discount]
 ,[vat_amount]
 ,[status]
 ,[note]
 ,[created_at]
 ,[updated_at])
 VALUES
     (1                     -- order_id (ID của đơn hàng SO2025001)
     ,101                   -- product_id (Điện thoại A)
     ,5                     -- quantity
     ,10000000.00           -- unit_price
     ,0.00                  -- discount (Không giảm giá)
     ,5000000.00            -- vat_amount (VAT 10%)
     ,'COMPLETED'           -- Trạng thái chi tiết (Đã hoàn thành)
     ,NULL                  -- note
     ,GETDATE()
     ,GETDATE())
GO
-- 1. Người dùng Admin (role_id = 1)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('AD001', 'admin_viet', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1990-05-15', NULL, N'Nguyễn Văn An', 'an.nguyen@company.com', '0901112222', 'ACTIVE', 1, N'Nam', N'123 Đường Láng, Hà Nội', GETDATE(), GETDATE())
GO

-- 2. Người dùng Kho (warehouseStaff - role_id = 2)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('WH002', 'kho_thanh', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1995-08-20', NULL, N'Trần Văn Thanh', 'thanh.tran@company.com', '0913334444', 'ACTIVE', 2, N'Nam', N'456 Trần Hưng Đạo, TP.HCM', GETDATE(), GETDATE())
GO

-- 3. Người dùng Bán hàng (saleStaff - role_id = 3)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('SL003', 'sale_huong', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1998-03-10', NULL, N'Lê Thị Hương', 'huong.le@company.com', '0975556666', 'ACTIVE', 3, N'Nữ', N'789 Nguyễn Văn Linh, Đà Nẵng', GETDATE(), GETDATE())
GO

-- 4. Người dùng Kế toán (accountstaff - role_id = 4)
INSERT INTO [dbo].[User]
([user_code], [username], [password], [date_of_birth], [avatar], [full_name], [email], [phone], [status], [role_id], [gender], [address], [created_at], [updated_at])
VALUES
    ('AC004', 'ketoan_minh', '$2a$12$V0VQqb.zVdzg35bwcNFsVuJAFfuZzA9ozNlb9RGk1qVbRaBMbhEci', '1992-11-25', NULL, N'Phạm Minh Tú', 'minh.pham@company.com', '0987778888', 'ACTIVE', 4, N'Nam', N'101 Phan Chu Trinh, Cần Thơ', GETDATE(), GETDATE())
GO