package com.foodkeeper.foodkeeperserver.food.e2e;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.e2e.E2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;

public class FoodE2ETest extends E2ETest {

    @Autowired MemberRepository memberRepository;
    @Autowired FoodCategoryRepository foodCategoryRepository;

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
}
