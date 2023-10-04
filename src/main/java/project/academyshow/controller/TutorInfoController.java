package project.academyshow.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.ReviewRequest;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.request.TutorRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.controller.response.ReferenceLikesStatistics;
import project.academyshow.controller.response.ReviewStatistics;
import project.academyshow.entity.ReferenceType;
import project.academyshow.entity.TutorInfo;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.service.LikeService;
import project.academyshow.service.PostService;
import project.academyshow.service.ReviewService;
import project.academyshow.service.TutorInfoService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TutorInfoController {

    private final TutorInfoService tutorInfoService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final LikeService likeService;

    @GetMapping("/tutors")
    public ApiResponse<?> tutors(@AuthenticationPrincipal CustomUserDetails user,
                                    SearchRequest searchRequest, Pageable pageable) {
        Page<TutorInfoSearchResponse> resources = tutorInfoService.search(searchRequest, pageable)
                .map(a -> {
                    ReferenceLikesStatistics upStatistics =
                            likeService.getLikeInfoByReference(ReferenceType.ACADEMY, a.getId(), user);
                    ReviewStatistics reviewStatistics = tutorInfoService.reviewStatistics(a.getId());
                    return new TutorInfoSearchResponse(a, upStatistics, reviewStatistics);
                });
        return ApiResponse.success(resources);
    }

    @GetMapping("/tutor/{id}")
    public ApiResponse<?> tutor(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<TutorInfo> tutorInfo = tutorInfoService.findById(id);
        if (tutorInfo.isPresent()) {
            TutorInfo a = tutorInfo.get();
            ReferenceLikesStatistics like = likeService.getLikeInfoByReference(ReferenceType.TUTOR, a.getId(), userDetails);
            return ApiResponse.success(new TutorInfoResponse(a, like));
        }
        else
            return ApiResponse.RESOURCE_NOT_FOUND_RESPONSE;
    }

    @PutMapping("/tutor/{id}")
    public ApiResponse<?> tutor(@PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestBody TutorRequest tutorRequest) {
        tutorInfoService.edit(id, userDetails, tutorRequest);
        return ApiResponse.SUCCESS_NO_DATA_RESPONSE;
    }

    @GetMapping("/tutor/{id}/reviews")
    public ApiResponse<?> findAllReview(@PathVariable("id") Long id, Pageable pageable) {
        return ApiResponse.success(reviewService.findAll(pageable, ReferenceType.TUTOR, id));
    }

    @PostMapping("/tutor/{id}/reviews")
    public ApiResponse<?> createReview(@PathVariable("id") Long id,
                                       @RequestBody ReviewRequest request,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.success(reviewService.create(request, ReferenceType.TUTOR, id, userDetails));
    }

    /** 과외 리뷰 별점 통계 */
    @GetMapping("/tutor/{id}/reviews/statistics")
    public ApiResponse<?> reviewStatistics(@PathVariable("id") Long id) {
        return ApiResponse.success(tutorInfoService.reviewStatistics(id));
    }

    @GetMapping("/tutor/{id}/posts")
    public ApiResponse<?> findAllPosts(@PathVariable("id") Long id,
                                       Pageable pageable) {
        return ApiResponse.success(postService.findAllByTutorInfo(id, pageable));
    }


    /**
     * Response DTO
     */
    @Data
    private static class TutorInfoSearchResponse {

        private Long id;
        private String name;
        private String title;
        private String profile;
        private String introduce;
        private String area;
        private String phone;

        private ReferenceLikesStatistics upStatistics;
        private ReviewStatistics reviewStatistics;

        private TutorInfoSearchResponse(TutorInfo tutorInfo,
                                        ReferenceLikesStatistics upStatistics,
                                        ReviewStatistics reviewStatistics) {
            id = tutorInfo.getId();
            name = tutorInfo.getMember().getName();
            title = tutorInfo.getTitle();
            profile = tutorInfo.getMember().getProfile();
            introduce = tutorInfo.getIntroduce();
            area = tutorInfo.getArea();
            phone = tutorInfo.getPhone();

            this.upStatistics = upStatistics;
            this.reviewStatistics = reviewStatistics;
        }
    }

    @Data
    private static class TutorInfoResponse {

        private Long id;
        private String name;
        private String title;
        private String certification;
        private String profile;
        private String introduce;
        private String area;
        private String phone;

        private String scholarship;
        private List<String> subjects;
        private List<String> educations;

        private ReferenceLikesStatistics upStatistics;

        private TutorInfoResponse(TutorInfo tutorInfo,
                                  ReferenceLikesStatistics upStatistics) {
            id = tutorInfo.getId();
            name = tutorInfo.getMember().getName();
            title = tutorInfo.getTitle();
            certification = tutorInfo.getCertification();
            profile = tutorInfo.getMember().getProfile();
            introduce = tutorInfo.getIntroduce();
            area = tutorInfo.getArea();
            phone = tutorInfo.getPhone();
            scholarship = tutorInfo.getScholarship();
            subjects = Arrays.stream(tutorInfo.getSubjects().split(",")).collect(Collectors.toList());
            educations = Arrays.stream(tutorInfo.getEducations().split(",")).collect(Collectors.toList());
            this.upStatistics = upStatistics;
        }
    }
}
