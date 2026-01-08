package com.foodkeeper.foodkeeperserver.auth.controller.v1;

import com.foodkeeper.foodkeeperserver.auth.business.LocalAuthService;
import com.foodkeeper.foodkeeperserver.auth.business.OauthService;
import com.foodkeeper.foodkeeperserver.auth.business.TokenRefreshService;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.request.*;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AccountDuplicationCheckResponse;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailCode;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.common.aspect.annotation.SignInLog;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
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

    private final OauthService oauthService;
    private final LocalAuthService localAuthService;
    private final TokenRefreshService tokenRefreshService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API")
    @NullMarked
    @SignInLog
    @PostMapping("/sign-in/kakao")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> signInWithKakao(
            @Valid @RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        Jwt jwt = oauthService.signInByOAuth(signInRequest.toContext(OAuthProvider.KAKAO, request.getRemoteAddr()));

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
        localAuthService.signOut(member.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @NullMarked
    @PostMapping("/check/account")
    public ResponseEntity<ApiResponse<AccountDuplicationCheckResponse>> checkAccountDuplication(
            @RequestBody AccountDuplicationCheckRequest request) {
        return ResponseEntity.ok(ApiResponse.success(new AccountDuplicationCheckResponse(
                localAuthService.isDuplicatedAccount(request.account()))));
    }

    @NullMarked
    @PostMapping("/verify/email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestBody EmailVerifyRequest request) {
        localAuthService.verifyEmail(new Email(request.email()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @NullMarked
    @PostMapping("/verify/email-code")
    public ResponseEntity<ApiResponse<Void>> verifyEmailCode(@RequestBody EmailCodeVerifyRequest request) {
        localAuthService.verifyEmailCode(EmailCode.of(request.email(), request.code()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @NullMarked
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody LocalSignUpRequest request,
                                                    HttpServletRequest httpRequest) {
        localAuthService.signUp(request.toContext(httpRequest.getRemoteAddr()));
        return ResponseEntity.ok(ApiResponse.success());
    }
}