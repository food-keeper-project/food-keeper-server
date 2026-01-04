package com.foodkeeper.foodkeeperserver.recipe.e2e;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.response.RecipeResponse;
import com.foodkeeper.foodkeeperserver.support.e2e.E2ETest;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeE2ETest extends E2ETest {

    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("ai를 통해 레시피 추천받는다")
    void aiRecommendRecipe() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());
        ParameterizedTypeReference<ApiResponse<RecipeResponse>> responseType =
                new ParameterizedTypeReference<>() {};

        ApiResponse<RecipeResponse> response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/recipes/recommend")
                        .queryParam("ingredients", "돼지고기", " 푸드소울")
                        .queryParam("excludedMenus", ", 간단 돼지고기 볶음", " 간단 돼지고기 구이")
                        .build())
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(responseType)
                .consumeWith(System.out::println)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.data()).isNotNull();


        System.out.println(response.data());
        System.out.println(response.data().ingredients().getFirst().name());
        System.out.println(response.data().ingredients().getFirst().quantity());

    }
}
