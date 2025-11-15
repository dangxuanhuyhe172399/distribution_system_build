package com.sep490.bads.distributionsystem.dto.salesOrderDtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderSummaryDraffDto {
    // Card 1: Đã chốt (tháng này)
    private long closedThisMonth;
    private long closedLastMonth;
    private BigDecimal closedChangePercent; // % so với tháng trước

    // Card 2: Đã huỷ (tháng này)
    private long cancelledThisMonth;
    private long cancelledLastMonth;
    private BigDecimal cancelledChangePercent; // % so với tháng trước

    // Card 3: Tỷ lệ chốt (tháng này)
    private BigDecimal closeRateThisMonth;        // vd: 83.3 (%)
    private BigDecimal closeRateLastMonth;        // vd: 80.2 (%)
    private BigDecimal closeRateChangePercent;    // % so với tháng trước (vd: +3.1)

    // Card 4: Chờ xác nhận (so với ngày hôm qua)
    private long waitingConfirmToday;       // số đơn PENDING hôm nay
    private long waitingConfirmYesterday;   // số đơn PENDING hôm qua
    private long waitingConfirmDiff;        // hôm nay - hôm qua (vd: +5)
}
