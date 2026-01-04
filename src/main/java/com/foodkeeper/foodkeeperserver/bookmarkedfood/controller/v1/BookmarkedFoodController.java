package com.foodkeeper.foodkeeperserver.bookmarkedfood.controller.v1;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.business.BookmarkedFoodService;
import com.foodkeeper.foodkeeperserver.bookmarkedfood.controller.v1.response.BookmarkedFoodsResponse;
import com.foodkeeper.foodkeeperserver.common.controller.CursorDefault;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.security.auth.AuthMember;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import com.foodkeeper.foodkeeperserver.support.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BookmarkedFood", description = "즐겨찾기 된 식재료 관련 API")
@RestController
@RequestMapping("/api/v1/bookmarked-foods")
@RequiredArgsConstructor
public class BookmarkedFoodController {

    private final BookmarkedFoodService bookmarkedFoodService;

    @Operation(summary = "즐겨찾기 된 식재료 조회", description = "즐겨찾기 된 식재료 조회 API")
    @NullMarked
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookmarkedFoodsResponse>>> findBookmarkedFoods(
            @CursorDefault Cursorable<Long> cursorable,
            @AuthMember Member member) {
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(
                bookmarkedFoodService.findBookmarkedFoods(cursorable, member.memberKey())
                        .map(BookmarkedFoodsResponse::from))));
    }
}
