package com.sep490.bads.distributionsystem.dto.stockDto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
public class CreateIssueDto {
    @NotNull private Long warehouseId;
    @NotEmpty private List<Line> lines;
    private String note;

    @Getter @Setter
    public static class Line {
        @NotNull private Long productId;
        @NotNull @Min(1) private Long quantity;
        private String note;
    }
}