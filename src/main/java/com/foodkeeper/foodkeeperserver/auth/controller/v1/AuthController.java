package com.foodkeeper.foodkeeperserver.auth.controller.v1;

import com.foodkeeper.foodkeeperserver.auth.business.TokenRefreshService;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenRefreshService tokenRefreshService;

    @Operation(summary = "토큰 Refresh", description = "토큰 Refresh API")
    @NullMarked
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> refreshToken(String refreshToken) {
        Jwt jwt = tokenRefreshService.refresh(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(new AuthTokenResponse(jwt.accessToken(), jwt.refreshToken())));
    }
}