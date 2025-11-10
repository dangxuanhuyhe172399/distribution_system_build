package com.sep490.bads.distributionsystem.dto.stockDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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