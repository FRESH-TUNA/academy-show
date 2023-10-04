package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import project.academyshow.controller.request.AcademyInfo;

import javax.persistence.*;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Academy {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Lob
    private String introduce;

    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String subAddress;
    private boolean selectRoadAddress;

    private String subjects;

    private String educations;

    private boolean shuttle;

    private String businessRegistration;
    private String profile;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_likes_id")
    private BatchLikes batchLikes;

    public BatchLikes getBatchLikes() {
        return batchLikes;
    }

    /**
     * academyInfo 데이터를 기반으로 학원정보를 수정한다.
     */
    public void edit(AcademyInfo academyInfo) {
        if (!StringUtils.hasText(profile)) profile = null;

        // 연령, 과목
        String educations = String.join(",", academyInfo.getEducations());

        String subjects = academyInfo.getSubjects().stream()
                .map(Subject::getName)
                .collect(Collectors.joining(","));

        this.educations = educations;
        this.subjects = subjects;

        // 기본정보
        this.name = academyInfo.getAcademyName();
        this.shuttle = academyInfo.isShuttle();
        this.profile = academyInfo.getProfile();
        this.phone = academyInfo.getPhone();
        this.introduce = academyInfo.getIntroduce();

        // 사업자등록증
        this.businessRegistration = academyInfo.getRegistrationFile();

        // 주소
        this.postcode = academyInfo.getAcademyPostcode();
        this.roadAddress = academyInfo.getAcademyRoadAddress();
        this.jibunAddress = academyInfo.getAcademyJibunAddress();
        this.subAddress = academyInfo.getAcademySubAddress();
        this.selectRoadAddress = academyInfo.isAcademySelectRoadAddress();
    }
}
