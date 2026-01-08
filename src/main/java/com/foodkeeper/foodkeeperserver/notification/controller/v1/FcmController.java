package com.foodkeeper.foodkeeperserver.notification.controller.v1;

import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.notification.business.FcmTokenService;
import com.foodkeeper.foodkeeperserver.notification.controller.v1.request.RefreshFcmTokenRequest;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmMessage;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {

    private final FcmTokenService fcmTokenService;

    @NullMarked
    @Operation(summary = "FcmToken 재발급 (앱 켜질때마다 호출)", description = "FcmToken 재발급 API")
    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> refreshFcmToken(@AuthMember Member authMember,
                                                             @Valid @RequestBody RefreshFcmTokenRequest request
    ) {
        fcmTokenService.update(request.fcmToken(), authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "FcmMessage 요청 타입", description = "FcmMessage 타입 명시")
    @GetMapping("/fcm-format")
    public FcmMessage getFcmFormat() {
        return null;
    }

}
// 추후에 updatedAt 으로 오랫동안 사용하지 않은 토큰 삭제 구현