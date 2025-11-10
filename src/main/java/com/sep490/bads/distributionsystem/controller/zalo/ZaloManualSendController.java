package com.sep490.bads.distributionsystem.controller.zalo;

import com.sep490.bads.distributionsystem.config.zalo.ZaloApiClient;
import com.sep490.bads.distributionsystem.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/public/zalo")
@Tag(name = "Zalo", description = "xử lý tin nhắn lấy thông tin khách hàng")
@RequiredArgsConstructor
public class ZaloManualSendController extends BaseController {
    private final ZaloApiClient zalo;

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
}