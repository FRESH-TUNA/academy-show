package project.academyshow.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.ReviewRequest;
import project.academyshow.controller.response.ApiResponse;

import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    @PatchMapping("/review/{id}")
    public ApiResponse<?> patchReview(@PathVariable("id") Long id,
                                      @RequestBody ReviewRequest request,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.success(reviewService.update(request, id, userDetails));
    }

    @DeleteMapping("/review/{id}")
    public ApiResponse<?> deleteReview(@PathVariable("id") Long id,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.delete(id, userDetails);
        return ApiResponse.DELETE_SUCCESS_RESPONSE;
    }
}
