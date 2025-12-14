package com.foodkeeper.foodkeeperserver.member.controller.v1;

import com.foodkeeper.foodkeeperserver.member.business.SignInService;
import com.foodkeeper.foodkeeperserver.member.controller.v1.request.SignInRequest;
import com.foodkeeper.foodkeeperserver.member.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.member.domain.Jwt;
import com.foodkeeper.foodkeeperserver.member.domain.MemberRegister;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sign-in")
@RequiredArgsConstructor
public class SignInController {

    private final SignInService signinService;

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
