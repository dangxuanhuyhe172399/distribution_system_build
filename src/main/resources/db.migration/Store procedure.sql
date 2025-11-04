USE bads_db;
GO

-- 1️Nhập kho: cập nhật hoặc thêm vào Inventory
CREATE OR ALTER PROCEDURE sp_PostGoodsReceipt
    @receipt_id BIGINT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @warehouse_id BIGINT;
    SELECT @warehouse_id = warehouse_id
    FROM GoodsReceipt
    WHERE receipt_id = @receipt_id;

    MERGE Inventory AS target
    USING (
        SELECT grd.product_id, grd.quantity
        FROM GoodsReceiptDetail grd
        WHERE grd.receipt_id = @receipt_id
    ) AS src
    ON target.product_id = src.product_id AND target.warehouse_id = @warehouse_id
    WHEN MATCHED THEN
        UPDATE SET
            target.quantity = target.quantity + src.quantity,
            target.last_in_at = GETDATE()
    WHEN NOT MATCHED THEN
        INSERT (warehouse_id, product_id, quantity, last_in_at)
        VALUES (@warehouse_id, src.product_id, src.quantity, GETDATE());
END;
GO

-- 2️Xuất kho: giảm tồn kho
CREATE OR ALTER PROCEDURE sp_PostGoodsIssue
    @issue_id BIGINT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @warehouse_id BIGINT;
    SELECT @warehouse_id = warehouse_id
    FROM GoodsIssues
    WHERE issue_id = @issue_id;

    UPDATE inv
    SET inv.quantity = inv.quantity - gid.quantity,
        inv.last_out_at = GETDATE()
    FROM Inventory inv
    JOIN GoodsIssuesDetail gid
        ON inv.product_id = gid.product_id
    WHERE gid.issue_id = @issue_id
      AND inv.warehouse_id = @warehouse_id;

    -- Cảnh báo nếu tồn âm
    IF EXISTS (SELECT 1 FROM Inventory WHERE quantity < 0)
        RAISERROR(N'Số lượng tồn kho không hợp lệ (âm).', 16, 1);
END;
GO

-- 3️⃣ Tính tổng hóa đơn dựa theo SalesOrderDetail
CREATE OR ALTER PROCEDURE sp_UpdateInvoiceTotal
    @order_id BIGINT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @total_amount DECIMAL(18,2);
    DECLARE @vat DECIMAL(18,2);

    SELECT 
        @total_amount = SUM(total_price),
        @vat = SUM(ISNULL(vat_amount,0))
    FROM SalesOrderDetail
    WHERE order_id = @order_id;

    UPDATE Invoice
    SET grand_total = @total_amount + @vat,
        vat_amount = @vat
    WHERE order_id = @order_id;
END;
GO
