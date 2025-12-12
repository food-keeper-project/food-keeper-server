package com.foodkeeper.foodkeeperserver.member.controller.v1;

import com.foodkeeper.foodkeeperserver.member.business.MemberService;
import com.foodkeeper.foodkeeperserver.member.controller.v1.response.ProfileResponse;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "프로필 조회", description = "프로필 조회 API")
    @GetMapping("/{memberKey}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@PathVariable String memberKey) {
        Member member = memberService.findMember(memberKey);
        return ResponseEntity.ok(ApiResponse.success(new ProfileResponse(member.nickname(), member.imageUrl())));
    }
}
