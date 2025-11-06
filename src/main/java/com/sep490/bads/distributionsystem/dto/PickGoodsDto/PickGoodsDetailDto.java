package com.sep490.bads.distributionsystem.dto.PickGoodsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickGoodsDetailDto {
    private PickGoodsHeaderDto header;
    private List<PickGoodsLineDto> lines;
}
