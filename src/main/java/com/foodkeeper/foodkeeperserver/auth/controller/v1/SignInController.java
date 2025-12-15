package com.foodkeeper.foodkeeperserver.auth.controller.v1;

import com.foodkeeper.foodkeeperserver.auth.business.SignInService;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.request.SignInRequest;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRegister;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/sign-in")
@RequiredArgsConstructor
public class SignInController {

    private final SignInService signinService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API")
    @NullMarked
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> signInWithKakao(
            @Valid @RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        MemberRegister register = MemberRegister.builder()
                .accessToken(signInRequest.accessToken())
                .oAuthProvider(OAuthProvider.KAKAO)
                .fcmToken(signInRequest.fcmToken())
                .ipAddress(request.getRemoteAddr())
                .build();

        Jwt jwt = signinService.signInByOAuth(register);

        return ResponseEntity.ok(ApiResponse.success(new AuthTokenResponse(jwt.accessToken(), jwt.refreshToken())));
    }

}
