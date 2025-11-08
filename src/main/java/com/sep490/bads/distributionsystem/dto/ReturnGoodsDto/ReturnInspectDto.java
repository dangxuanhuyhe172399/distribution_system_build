package com.sep490.bads.distributionsystem.dto.ReturnGoodsDto;

import com.sep490.bads.distributionsystem.entity.type.InspectionResultStatus;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnInspectDto {

    private List<Row> rows;

    @Data
    public static class Row {
        Long requestDetailId;
        Long inspectedQty;
        InspectionResultStatus result;
        String note;
    }
}
