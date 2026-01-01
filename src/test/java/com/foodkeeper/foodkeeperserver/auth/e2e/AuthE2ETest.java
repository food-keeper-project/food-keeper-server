package com.foodkeeper.foodkeeperserver.auth.e2e;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.e2e.E2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthE2ETest extends E2ETest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("자신의 프로필을 조회한다.")
    void getMyProfile() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());

        client.get()
                .uri("/api/v1/members/me")
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.nickname").isEqualTo(member.getNickname())
                .jsonPath("$.data.imageUrl").isEqualTo(member.getImageUrl());
    }
}
