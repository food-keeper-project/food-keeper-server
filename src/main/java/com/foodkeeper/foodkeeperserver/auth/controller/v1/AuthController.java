package com.foodkeeper.foodkeeperserver.auth.controller.v1;

import com.foodkeeper.foodkeeperserver.auth.business.LocalAuthService;
import com.foodkeeper.foodkeeperserver.auth.business.OAuthService;
import com.foodkeeper.foodkeeperserver.auth.business.TokenRefreshService;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.request.*;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AccountDuplicationCheckResponse;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailCode;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.LocalAccount;
import com.foodkeeper.foodkeeperserver.auth.domain.Password;
import com.foodkeeper.foodkeeperserver.common.aspect.annotation.SignInLog;
import com.foodkeeper.foodkeeperserver.common.utils.NetworkUtils;
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

    private final OAuthService oauthService;
    private final LocalAuthService localAuthService;
    private final TokenRefreshService tokenRefreshService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API")
    @NullMarked
    @SignInLog
    @PostMapping("/sign-in/kakao")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> signInWithKakao(
            @Valid @RequestBody OauthSignInRequest request, HttpServletRequest httpRequest) {
        Jwt jwt = oauthService.signInByOAuth(request.toContext(OAuthProvider.KAKAO, NetworkUtils.getClientIp(httpRequest)));

        return ResponseEntity.ok(ApiResponse.success(new AuthTokenResponse(jwt.accessToken(), jwt.refreshToken())));
    }

    @Operation(summary = "로컬 로그인", description = "로컬 로그인 API")
    @NullMarked
    @SignInLog
    @PostMapping("/sign-in/local")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> signIn(@Valid @RequestBody LocalSignInRequest request,
                                                                 HttpServletRequest httpRequest) {
        Jwt jwt = localAuthService.signIn(request.toContext(NetworkUtils.getClientIp(httpRequest)));

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

    @Operation(summary = "ID 중복확인", description = "ID 중복확인 API")
    @NullMarked
    @PostMapping("/check/account")
    public ResponseEntity<ApiResponse<AccountDuplicationCheckResponse>> checkAccountDuplication(
            @RequestBody AccountDuplicationCheckRequest request) {
        return ResponseEntity.ok(ApiResponse.success(new AccountDuplicationCheckResponse(
                localAuthService.isDuplicatedAccount(new LocalAccount(request.account())))));
    }

    @Operation(summary = "email 유효성 검사 및 인증코드 발송", description = "email 유효성 검사 및 인증코드 발송 API")
    @NullMarked
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody EmailVerifyRequest request) {
        localAuthService.verifyEmailForSignUp(new Email(request.email()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "email 인증코드 확인", description = "email 인증코드 확인 API")
    @NullMarked
    @PostMapping("/email-code/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmailCode(@Valid @RequestBody EmailCodeVerifyRequest request) {
        localAuthService.verifyEmailCode(EmailCode.of(request.email(), request.code()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "회원가입", description = "회원가입 API")
    @NullMarked
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody LocalSignUpRequest request,
                                                    HttpServletRequest httpRequest) {
        localAuthService.signUp(request.toContext(NetworkUtils.getClientIp(httpRequest)));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "ID 찾기 인증코드 발송", description = "ID 찾기 인증코드 발송 API")
    @NullMarked
    @PostMapping("/account/verify")
    public ResponseEntity<ApiResponse<Void>> sendCodeForRecoverAccount(
            @Valid @RequestBody FindAccountRequest request) {
        localAuthService.verifyEmailForRecoverAccount(new Email(request.email()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "ID 찾기 인증코드 확인 ID 발송", description = "ID 찾기 인증코드 확인 및 ID 발송 API")
    @NullMarked
    @PostMapping("/account-code/verify")
    public ResponseEntity<ApiResponse<Void>> findAccount(
            @Valid @RequestBody EmailCodeVerifyRequest request) {
        localAuthService.recoverAccount(EmailCode.of(request.email(), request.code()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "비밀번호 찾기 인증코드 발송", description = "비밀번호 찾기 인증코드 발송 API")
    @NullMarked
    @PostMapping("/password/verify")
    public ResponseEntity<ApiResponse<Void>> sendCodeForRecoverPassword(
            @Valid @RequestBody FindPasswordRequest request) {
        localAuthService.verifyEmailForRecoverPassword(new Email(request.email()), new LocalAccount(request.account()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "비밀번호 찾기 인증코드 확인", description = "비밀번호 찾기 인증코드 확인 API")
    @NullMarked
    @PostMapping("/password-code/verify")
    public ResponseEntity<ApiResponse<Void>> findPassword(
            @Valid @RequestBody PasswordCodeVerifyRequest request) {
        localAuthService.findPassword(EmailCode.of(request.email(), request.code()), new LocalAccount(request.account()));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "비밀번호 재설정", description = "비밀번호 재설정 API")
    @NullMarked
    @PostMapping("/password/change")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody PasswordChangeRequest request) {
        localAuthService.changePassword(
                new Email(request.email()),
                new LocalAccount(request.account()),
                new Password(request.newPassword()));
        return ResponseEntity.ok(ApiResponse.success());
    }
}