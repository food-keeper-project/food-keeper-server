package com.foodkeeper.foodkeeperserver.notification.controller.v1;

import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.notification.business.FcmService;
import com.foodkeeper.foodkeeperserver.notification.controller.v1.request.RefreshFcmTokenRequest;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {

    private final FcmService fcmService;

    @NullMarked
    @Operation(summary = "FcmToken 재발급", description = "FcmToken 재발급 API")
    @PatchMapping("/token")
    public ResponseEntity<ApiResponse<Void>> refreshFcmToken(@AuthMember Member authMember, @RequestBody RefreshFcmTokenRequest request
    ) {
        fcmService.update(request.token(), authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
