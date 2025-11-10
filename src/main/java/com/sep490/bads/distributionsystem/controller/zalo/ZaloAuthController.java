package com.sep490.bads.distributionsystem.controller.zalo;

import com.sep490.bads.distributionsystem.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping; // <- THÊM IMPORT NÀY

@RestController
@RequestMapping("/v1/public/zalo")
public class ZaloAuthController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ZaloAuthController.class);

    @Value("${zalo.app-id}")
    private String zaloAppId;

    @Value("${zalo.oa-secret}")
    private String zaloOaSecret;

    /**
     * Đường dẫn API mới sẽ là: /v1/public/zalo/callback
     */
    @GetMapping("/callback") // <- SỬA TỪ "/zalo-callback"
    public ResponseEntity<String> handleZaloCallback(
            @RequestParam("code") String code,
            @RequestParam(name = "state", required = false) String state) {

        log.info("Nhận được 'code' từ Zalo: {}", code);

        // --- (Toàn bộ logic bên trong hàm này giữ nguyên) ---
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://oauth.zaloapp.com/v4/access_token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("secret_key", zaloOaSecret);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("app_id", zaloAppId);
        map.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String tokenResponse = response.getBody();
                log.info("-----------------------------------------------------------------");
                log.info("NHẬN ĐƯỢC TOKEN TỪ ZALO THÀNH CÔNG:");
                log.info("{}", tokenResponse);
                log.info("HÃY SAO CHÉP VÀ LƯU VÀO FILE .env CỦA BẠN");
                log.info("-----------------------------------------------------------------");
                return ResponseEntity.ok("Đã lấy token thành công! Vui lòng kiểm tra log trên server (backend).");
            } else {
                log.error("Lấy token thất bại: {}", response.getBody());
                return ResponseEntity.status(response.getStatusCode()).body("Lấy token thất bại: " + response.getBody());
            }
        } catch (Exception e) {
            log.error("Lỗi nghiêm trọng khi gọi Zalo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
}
