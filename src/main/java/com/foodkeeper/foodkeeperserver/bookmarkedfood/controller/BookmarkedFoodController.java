package com.foodkeeper.foodkeeperserver.bookmarkedfood.controller;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.business.BookmarkedFoodService;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookmarkedFoodController {

    private final BookmarkedFoodService bookmarkedFoodService;

    @PostMapping("/foods/{foodId}/bookmark")
    public ResponseEntity<Void> bookmarkFood(@PathVariable Long foodId, String memberKey) {
        Long bookmarkedFoodId = bookmarkedFoodService.bookmarkFood(foodId, memberKey);

        return ResponseEntity.created(URI.create("/api/v1/bookmarked-foods/" + bookmarkedFoodId)).build();
    }
}
