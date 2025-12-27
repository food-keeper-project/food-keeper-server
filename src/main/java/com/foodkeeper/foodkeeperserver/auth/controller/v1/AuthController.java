package com.foodkeeper.foodkeeperserver.auth.controller.v1;

import com.foodkeeper.foodkeeperserver.auth.business.AuthService;
import com.foodkeeper.foodkeeperserver.auth.business.TokenRefreshService;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.request.SignInRequest;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.request.TokenRefreshRequest;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.common.aspect.annotation.SignInLog;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenRefreshService tokenRefreshService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API")
    @NullMarked
    @SignInLog
    @PostMapping("/sign-in/kakao")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> signInWithKakao(
            @Valid @RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        Jwt jwt = authService.signInByOAuth(signInRequest.toContext(OAuthProvider.KAKAO, request.getRemoteAddr()));

        return ResponseEntity.ok(ApiResponse.success(new AuthTokenResponse(jwt.accessToken(), jwt.refreshToken())));
    }

    @Operation(summary = "토큰 Refresh", description = "토큰 Refresh API")
    @NullMarked
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request,
                                                                       @AuthMember Member member) {
        Jwt jwt = tokenRefreshService.refresh(request.refreshToken(), member.memberKey());
        return ResponseEntity.ok(ApiResponse.success(new AuthTokenResponse(jwt.accessToken(), jwt.refreshToken())));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API")
    @NullMarked
    @DeleteMapping("/sign-out")
    public ResponseEntity<ApiResponse<Void>> signOut(@AuthMember Member member) {
        authService.signOut(member.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }
}