package com.sep490.bads.distributionsystem.dto.stockDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReceiptDto {
    @NotNull private Long warehouseId;
    @NotEmpty private List<Line> lines;
    private String note;

    @Getter @Setter
    public static class Line {
        @NotNull private Long productId;
        @NotNull @Min(1) private Long quantity;
        private Integer qrId;                // optional
        private LocalDate manufactureDate;   // optional
        private LocalDate expiryDate;        // optional
        private String note;
    }
}
