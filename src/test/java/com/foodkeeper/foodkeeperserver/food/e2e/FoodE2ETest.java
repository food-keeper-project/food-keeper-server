package com.foodkeeper.foodkeeperserver.food.e2e;

import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodResponse;
import com.foodkeeper.foodkeeperserver.food.controller.v1.response.FoodResponses;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.integration.E2ETest;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import com.foodkeeper.foodkeeperserver.support.response.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FoodE2ETest extends E2ETest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FoodCategoryRepository foodCategoryRepository;
    @Autowired
    SelectedFoodCategoryRepository selectedFoodCategoryRepository;
    @Autowired
    FoodRepository foodRepository;

    @Test
    @DisplayName("식재료를 저장한다.")
    void saveFood() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());
        FoodCategoryEntity category = foodCategoryRepository.save(
                FoodCategoryEntity.builder().name("test").memberKey(member.getMemberKey()).build());


        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        String foodRegisterRequest = """
                {
                    "name": "ㅇㅇ",
                    "categoryIds": [%d],
                    "storageMethod": "냉장",
                    "expiryDate": "2026-01-16T00:00:00.000Z",
                    "expiryAlarm": 14,
                    "memo": "ㅇㅇㅇㅇㅇ"
                }
                """.formatted(category.getId());
        builder.part("request", foodRegisterRequest, MediaType.APPLICATION_JSON);

        client.post()
                .uri("/api/v1/foods")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .body(builder.build())
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("유통기한 알람이 14일보다 크면 validation 예외가 발생한다.")
    void throwValidationExceptionIfExpiryAlarmOver14() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());
        FoodCategoryEntity category = foodCategoryRepository.save(
                FoodCategoryEntity.builder().name("test").memberKey(member.getMemberKey()).build());


        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        String foodRegisterRequest = """
                {
                    "name": "ㅇㅇ",
                    "categoryIds": [%d],
                    "storageMethod": "냉장",
                    "expiryDate": "2026-01-16T00:00:00.000Z",
                    "expiryAlarm": 15,
                    "memo": "ㅇㅇㅇㅇㅇ"
                }
                """.formatted(category.getId());
        builder.part("request", foodRegisterRequest, MediaType.APPLICATION_JSON);

        client.post()
                .uri("/api/v1/foods")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .body(builder.build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("카테고리 개수가 1개 미만이면 Validation 예외가 발생한다.")
    void throwValidationExceptionIfCategoryCountIsUnder1() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        String foodRegisterRequest = """
                {
                    "name": "ㅇㅇ",
                    "categoryIds": [],
                    "storageMethod": "냉장",
                    "expiryDate": "2026-01-16T00:00:00.000Z",
                    "expiryAlarm": 14,
                    "memo": "ㅇㅇㅇㅇㅇ"
                }
                """;
        builder.part("request", foodRegisterRequest, MediaType.APPLICATION_JSON);

        client.post()
                .uri("/api/v1/foods")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .body(builder.build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("카테고리 개수가 3개 초과면 Validation 예외가 발생한다.")
    void throwValidationExceptionIfCategoryCountIsOver3() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        String foodRegisterRequest = """
                {
                    "name": "ㅇㅇ",
                    "categoryIds": [1, 2, 3, 4],
                    "storageMethod": "냉장",
                    "expiryDate": "2026-01-16T00:00:00.000Z",
                    "expiryAlarm": 14,
                    "memo": "ㅇㅇㅇㅇㅇ"
                }
                """;
        builder.part("request", foodRegisterRequest, MediaType.APPLICATION_JSON);

        client.post()
                .uri("/api/v1/foods")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .body(builder.build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("식재료 리스트 페이지를 조회한다.")
    void findFoodPage() {
        // given
        ParameterizedTypeReference<ApiResponse<PageResponse<FoodResponse>>> responseType =
                new ParameterizedTypeReference<>() {};
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());
        FoodCategoryEntity category = foodCategoryRepository.save(
                FoodCategoryEntity.builder().name("test").memberKey(member.getMemberKey()).build());
        List<LocalDate> expiryDates = List.of(
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 5),
                LocalDate.of(2026, 1, 2),
                LocalDate.of(2026, 1, 7),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 5),
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 7),
                LocalDate.of(2026, 1, 9),
                LocalDate.of(2026, 1, 1)
        );

        expiryDates.forEach(expiryDate -> {
            FoodRegister foodRegister = new FoodRegister("test", List.of(category.getId()), StorageMethod.FROZEN, expiryDate, 2, "");
            FoodEntity food = foodRepository.save(FoodEntity.from(foodRegister, "https://test.com/image.jpg", member.getMemberKey()));
            selectedFoodCategoryRepository.save(SelectedFoodCategoryEntity.from(new SelectedFoodCategory(null, food.getId(), category.getId())));
        });

        // when
        ApiResponse<PageResponse<FoodResponse>> response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/foods")
                        .queryParam("categoryId", category.getId())
                        .queryParam("limit", 10)
                        .build())
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(responseType)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.data()).isNotNull();
        assertThat(response.data().content()).hasSize(10);
        assertThat(response.data().content().stream().map(FoodResponse::expiryDate)).isSorted();
    }

    @Test
    @DisplayName("유통기한 임박 식재료를 조회한다.")
    void findImminentFoods() {
        ParameterizedTypeReference<ApiResponse<FoodResponses>> responseType =
                new ParameterizedTypeReference<>() {};

        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());
        FoodCategoryEntity category = foodCategoryRepository.save(
                FoodCategoryEntity.builder().name("test").memberKey(member.getMemberKey()).build());
        List<LocalDate> expiryDates = List.of(
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2),
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(7),
                LocalDate.now().plusDays(7)
        );

        expiryDates.forEach(expiryDate -> {
            FoodRegister foodRegister = new FoodRegister("test", List.of(category.getId()), StorageMethod.FROZEN, expiryDate, 2, "");
            FoodEntity food = foodRepository.save(FoodEntity.from(foodRegister, "https://test.com/image.jpg", member.getMemberKey()));
            selectedFoodCategoryRepository.save(SelectedFoodCategoryEntity.from(new SelectedFoodCategory(null, food.getId(), category.getId())));
        });

        ApiResponse<FoodResponses> response = client.get()
                .uri("/api/v1/foods/imminent")
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(responseType)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.data()).isNotNull();
        assertThat(response.data().foods()).hasSize(9);
    }
}
