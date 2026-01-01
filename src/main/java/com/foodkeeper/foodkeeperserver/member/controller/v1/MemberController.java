package com.foodkeeper.foodkeeperserver.member.controller.v1;

import com.foodkeeper.foodkeeperserver.member.business.MemberService;
import com.foodkeeper.foodkeeperserver.member.controller.v1.response.ProfileResponse;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @NullMarked
    @Operation(summary = "프로필 조회", description = "프로필 조회 API")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@AuthMember Member authMember) {
        Member member = memberService.findMember(authMember.memberKey());
        return ResponseEntity.ok(ApiResponse.success(new ProfileResponse(member.nickname(), member.imageUrl())));
    }

    @NullMarked
    @DeleteMapping("/me/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@AuthMember Member member) {
        memberService.withdrawMember(member.memberKey());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
