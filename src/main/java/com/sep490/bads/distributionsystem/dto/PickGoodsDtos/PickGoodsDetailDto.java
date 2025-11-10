package com.sep490.bads.distributionsystem.dto.PickGoodsDtos;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickGoodsDetailDto {
    private PickGoodsHeaderDto header;
    private List<PickGoodsLineDto> lines;
}
