
--  INSERT ROLE
INSERT INTO Role (role_name) VALUES
                                 (N'admin'),               -- Quản trị toàn hệ thống
                                 (N'warehouseStaff'),     -- Nhân viên kho
                                 (N'saleStaff'),         -- Nhân viên bán hàng
                                 (N'accountant');    -- Nhân viên kế toán

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

--INSERT USER
USE [bads_db];
GO

INSERT INTO [dbo].[User]
           ([user_code]
           ,[username]
           ,[password]
           ,[date_of_birth]
           ,[avatar]
           ,[full_name]
           ,[email]
           ,[phone]
           ,[status]
           ,[role_id]
           ,[gender]
           ,[address]
           ,[created_at]
           ,[updated_at])
VALUES
('U001', 'admin', '123', '2003-08-31', 'avatar1.png', N'Dang Xuan Huy', 'dangxuanhuy3108@gmail.com', '0385474355', 'ACTIVE', 1, 'MALE', N'Hà Nội', GETDATE(), GETDATE()),
('U002', 'linhpham', '123456', '2000-11-22', 'avatar2.png', N'Phạm Thùy Linh', 'linh.pham@example.com', '0912345678', 'ACTIVE', 2, 'FEMALE', N'Hồ Chí Minh', GETDATE(), GETDATE()),
('U003', 'trungkien', '123456', '1999-02-14', 'avatar3.png', N'Đỗ Trung Kiên', 'kien.do@example.com', '0978123456', 'ACTIVE', 2, 'MALE', N'Đà Nẵng', GETDATE(), GETDATE()),
('U004', 'minhchau', '123456', '2001-07-05', 'avatar4.png', N'Lê Minh Châu', 'chau.le@example.com', '0909988776', 'INACTIVE', 3, 'FEMALE', N'Hải Phòng', GETDATE(), GETDATE()),
('U005', 'quanghuy', '123456', '1997-12-01', 'avatar5.png', N'Trần Quang Huy', 'huy.tran@example.com', '0933445566', 'ACTIVE', 1, 'MALE', N'Hưng Yên', GETDATE(), GETDATE());
GO
