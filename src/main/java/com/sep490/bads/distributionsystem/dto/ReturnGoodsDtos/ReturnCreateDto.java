package com.sep490.bads.distributionsystem.dto.ReturnGoodsDtos;

import lombok.*;

import java.util.List;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnCreateDto {
    private Long orderId;          // optional
    private Long customerId;       // optional
    private String reason;
    private String reasonDetail;
    private List<Item> items;

    @Data
    public static class Item {
        Long orderDetailId;
        Long quantity;
        String reasonForItem;
    }
}
