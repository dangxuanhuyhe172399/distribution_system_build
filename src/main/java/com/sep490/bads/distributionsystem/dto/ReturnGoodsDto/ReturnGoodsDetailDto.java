package com.sep490.bads.distributionsystem.dto.ReturnGoodsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnGoodsDetailDto {
    private ReturnGoodsHeaderDto header;
    private List<ReturnGoodsLineDto> lines;
    private List<String> mediaUrls;   // ảnh/video bằng chứng nếu có
}
