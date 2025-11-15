package com.sep490.bads.distributionsystem.controller.zalo;

import com.sep490.bads.distributionsystem.config.zalo.ZaloApiClient;
import com.sep490.bads.distributionsystem.controller.BaseController;
import com.sep490.bads.distributionsystem.entity.Product;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloCustomerLink;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/public/zalo")
@Tag(name = "Zalo", description = "xử lý tin nhắn lấy thông tin khách hàng")
@RequiredArgsConstructor
public class ZaloManualSendController extends BaseController {
    private final ZaloApiClient zalo;
    private final ProductService productService;

    /** GET list recent conversations -> fill combobox chọn customer Zalo */
    @Operation(summary = "list recent conversations -> fill combobox chọn customer Zalo")
    @GetMapping("/conversations")
    public Map<String,Object> recent(@RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "20") int count) {
        return zalo.getRecentConversations(offset, count).block();
    }

    /** POST gửi nút Chia sẻ thông tin cho một user_id */
    @Operation(summary = "Chia sẻ thông tin cho một user_id")
    @PostMapping("/users/{userId}/request-info")
    public Map<String,Object> askInfo(@PathVariable String userId,
                                      @RequestParam(defaultValue = "Nhờ anh/chị chia sẻ thông tin liên hệ để tạo đơn nhanh ạ!") String title,
                                      @RequestParam(defaultValue = "Bấm nút bên dưới để chia sẻ Họ tên / SĐT / Địa chỉ") String subtitle) {
        return zalo.sendRequestShareInfo(userId, title, subtitle).block();
    }

    /** POST gửi text thường (tiện debug) */
    @Operation(summary = "gửi text thường")
    @PostMapping("/users/{userId}/text")
    public Map<String,Object> sendText(@PathVariable String userId, @RequestParam String text) {
        return zalo.sendText(userId, text).block();
    }

    @Operation(summary = "Lấy profile từ sender_id")
    @GetMapping("/profile")
    public Map<String,Object> profile(@RequestParam String userId) {
        return zalo.getUserProfile(userId).block();
    }

//    @Operation(summary = "Gửi danh sách sản phẩm cho 1 customer (đã link Zalo)")
//    @PostMapping("/customers/{customerId}/products")
//    public Map<String, Object> sendProductsToCustomer(
//            @PathVariable Long customerId,
//            @RequestParam(defaultValue = "5") int limit
//    ) {
//        // 1) Tìm link Zalo của customer
//        ZaloCustomerLink link = zalo.findFirstByCustomer_Id(customerId)
//                .orElseThrow(() -> new NotFoundException("Khách hàng chưa liên kết Zalo"));
//
//        String zaloUserId = link.getZaloUserId();
//
//        // 2) Lấy list product – tùy bạn implement, ví dụ: top N sản phẩm đang active
//        List<Product> products = productRepository
//                .findTopNActiveProducts(limit); // TODO: đổi thành method thật của bạn
//
//        // 3) Build nội dung text
//        String text = buildProductListMessage(products);
//
//        // 4) Gửi qua OA
//        return zalo.sendText(zaloUserId, text).block();
//    }
//
//    /** Helper: format danh sách sản phẩm thành 1 text message */
//    private String buildProductListMessage(List<Product> products) {
//        if (products.isEmpty()) {
//            return "Hiện tại chưa có sản phẩm nào khả dụng.";
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Danh sách sản phẩm hôm nay:\n");
//
//        int index = 1;
//        for (Product p : products) {
//            BigDecimal price = p.getSellingPrice(); // hoặc getPrice tùy entity
//            sb.append(index++)
//                    .append(". ")
//                    .append(p.getName());
//
//            if (price != null) {
//                sb.append(" - ").append(price).append(" đ");
//            }
//            sb.append("\n");
//        }
//
//        sb.append("\nAnh/chị muốn đặt sản phẩm nào ạ?");
//        return sb.toString();
//    }

}