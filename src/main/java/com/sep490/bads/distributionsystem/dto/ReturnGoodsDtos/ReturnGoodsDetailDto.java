package com.sep490.bads.distributionsystem.dto.ReturnGoodsDtos;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnGoodsDetailDto {
    private ReturnGoodsHeaderDto header;
    private List<ReturnGoodsLineDto> lines;
    private List<String> mediaUrls;   // ảnh/video bằng chứng nếu có
}
