package com.foodkeeper.foodkeeperserver.test.controller;

import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.Nickname;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import com.foodkeeper.foodkeeperserver.notification.business.FoodNotificationService;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Profile("!prod")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final MemberRegistrar memberRegistrar;
    private final JwtGenerator jwtGenerator;
    private final FoodNotificationService foodNotificationService;

    @NullMarked
    @PostMapping("/trigger/expiry-alarm")
    public ResponseEntity<ApiResponse<Void>> triggerExpiryAlarm() {
        foodNotificationService.sendExpiryAlarm();
        return ResponseEntity.ok(ApiResponse.success());
    }

    @NullMarked
    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> testSignIn(@RequestBody TestSingInRequest request) {
        NewMember newMember = NewMember.builder()
                .email(new Email(request.email()))
                .nickname(new Nickname("test"))
                .signUpType(SignUpType.OAUTH)
                .ipAddress(new IpAddress("127.0.0.1"))
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build();

        String memberKey = memberRegistrar.register(newMember);
        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        return ResponseEntity.ok(ApiResponse.success(new AuthTokenResponse(jwt.accessToken(), jwt.refreshToken())));
    }
}
