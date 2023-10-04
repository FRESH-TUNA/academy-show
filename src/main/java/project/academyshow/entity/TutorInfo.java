package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.academyshow.controller.request.TutorRequest;

import javax.persistence.*;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TutorInfo {

    @Id @GeneratedValue
    private Long id;

    private String title;

    // 대학
    private String scholarship;

    private String certification;

    private String subjects;

    private String educations;

    private String introduce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 검색 노출 여부
    private Boolean isExposed;

    // 대략적인 주소 정보 (강원도 강릉시)
    private String area;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_likes_id")
    private BatchLikes batchLikes;

    public BatchLikes getBatchLikes() {
        return batchLikes;
    }

    public void edit(TutorRequest tutorRequest) {
        // 연령, 과목
        String educations = String.join(",", tutorRequest.getEducations());

        String subjects = tutorRequest.getSubjects().stream()
                .map(Subject::getName)
                .collect(Collectors.joining(","));

        this.educations = educations;
        this.subjects = subjects;

        // 기본정보
        this.title = tutorRequest.getTitle();
        this.phone = tutorRequest.getPhone();
        this.introduce = tutorRequest.getIntroduce();
        this.scholarship = tutorRequest.getScholarship();
        this.certification = tutorRequest.getCertification();
    }
}
