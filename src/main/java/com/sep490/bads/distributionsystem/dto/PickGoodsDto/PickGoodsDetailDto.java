package com.sep490.bads.distributionsystem.dto.PickGoodsDto;

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
