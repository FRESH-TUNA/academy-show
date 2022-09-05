package project.academyshow.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.ReviewRequest;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.response.AcademyResponse;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.controller.response.ReferenceUpStatistics;
import project.academyshow.controller.response.PostResponse;
import project.academyshow.entity.Academy;

import project.academyshow.entity.ReferenceType;

import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.AcademyService;
import project.academyshow.service.LikeService;
import project.academyshow.service.PostService;
import project.academyshow.service.ReviewService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AcademyController {

    private final AcademyService academyService;
    private final ReviewService reviewService;
    private final PostService postService;
    private final LikeService likeService;

    /** 학원 검색 (Pageable: page(페이지), size(페이지 당 개수), sort(정렬 기준 필드명과 정렬방법) */
    @GetMapping("/academies")
    public ApiResponse<?> academies(SearchRequest searchRequest, Pageable pageable) {
        Page<AcademySearchResponse> resources = academyService.search(searchRequest, pageable)
                .map(AcademySearchResponse::new);
        return ApiResponse.success(resources);
    }

    @Data
    private static class AcademySearchResponse {
        private Long id;
        private String name;
        private String profile;
        private String introduce;
        private String roadAddress;
        private String subAddress;

        private AcademySearchResponse(Academy academy) {
            id = academy.getId();
            name = academy.getName();
            profile = academy.getProfile();
            introduce = academy.getIntroduce();
            roadAddress = academy.getRoadAddress();
            subAddress = academy.getSubAddress();
        }
    }

    @GetMapping("/academy/{id}")
    public ApiResponse<?> academy(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<Academy> academy = academyService.findById(id);
        if (academy.isPresent()) {
            Academy a = academy.get();
            ReferenceUpStatistics like = likeService.getLikeInfoByReference(ReferenceType.ACADEMY, a.getId(), userDetails);
            return ApiResponse.success(AcademyResponse.of(a, like));
        }
        else
            return ApiResponse.RESOURCE_NOT_FOUND_RESPONSE;
    }

    @GetMapping("/academy/{id}/reviews")
    public ApiResponse<?> findAllReview(@PathVariable("id") Long id, Pageable pageable) {
        return ApiResponse.success(reviewService.findAll(pageable, ReferenceType.ACADEMY, id));
    }

    @PostMapping("/academy/{id}/reviews")
    public ApiResponse<?> createReview(@PathVariable("id") Long id, @RequestBody ReviewRequest request) {
        return ApiResponse.success(reviewService.create(request, ReferenceType.ACADEMY, id));
    }

    /** 학원 리뷰 별점 통계 */
    @GetMapping("/academy/{id}/reviews/statistics")
    public ApiResponse<?> reviewStatistics(@PathVariable("id") Long id) {
        return ApiResponse.success(academyService.reviewStatistics(id));
    }

    @GetMapping("/academy/{id}/posts")
    public ApiResponse<?> findAllPosts(@PathVariable("id") Long id, Pageable pageable) {
        return ApiResponse.success(postService.findAllByAcademy(id, pageable).map(PostResponse::ofList));
    }
}
