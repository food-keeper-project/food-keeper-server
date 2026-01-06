package com.foodkeeper.foodkeeperserver.bookmarkedfood.e2e;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.repository.BookmarkedFoodRepository;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.fixture.BookmarkedFoodEntityFixture;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.integration.E2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookmarkedFoodE2ETest extends E2ETest {

    @Autowired MemberRepository memberRepository;
    @Autowired BookmarkedFoodRepository bookmarkedFoodRepository;

    @Test
    @DisplayName("즐겨찾기 된 식재료를 조회한다.")
    void findBookmarkedFood() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());
        BookmarkedFoodEntity bookmarkedFood = bookmarkedFoodRepository.save(
                BookmarkedFoodEntityFixture.DEFAULT.get(member.getMemberKey()));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/bookmarked-foods")
                        .queryParam("cursor", 11)
                        .queryParam("limit", 10)
                        .build())
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.content[0].name").isEqualTo(bookmarkedFood.getName())
                .jsonPath("$.data.content[0].imageUrl").isEqualTo(bookmarkedFood.getImageUrl())
                .jsonPath("$.data.content[0].storageMethod").isEqualTo(bookmarkedFood.getStorageMethod().getValue());
    }
}