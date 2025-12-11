package com.foodkeeper.foodkeeperserver.member.controller.v1;

import com.foodkeeper.foodkeeperserver.member.business.MemberService;
import com.foodkeeper.foodkeeperserver.member.controller.v1.response.ProfileResponse;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ApiResponse<ProfileResponse> getProfile(@PathVariable String memberId) {
        Member member = memberService.findMember(memberId);
        return ApiResponse.success(new ProfileResponse(member.nickname(), member.imageUrl()));
    }
}
