package project.academyshow.controller.response;

import lombok.Data;
import project.academyshow.entity.Academy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AcademyResponse {

    private Long id;
    private String name;
    private String profile;
    private String introduce;
    private String postcode;
    private String roadAddress;
    private String jibunAddress;
    private String subAddress;
    private String phone;
    private List<String> subjects;
    private List<String> educations;
    private boolean shuttle;
    private String businessRegistration;
    private ReferenceLikesStatistics upStatistics;

    public AcademyResponse(Academy academy, ReferenceLikesStatistics upStatistics) {
        id = academy.getId();
        name = academy.getName();
        profile = academy.getProfile();
        introduce = academy.getIntroduce();
        roadAddress = academy.getRoadAddress();
        jibunAddress = academy.getJibunAddress();
        subAddress = academy.getSubAddress();
        postcode = academy.getPostcode();
        subjects = Arrays.stream(academy.getSubjects().split(",")).collect(Collectors.toList());
        educations = Arrays.stream(academy.getEducations().split(",")).collect(Collectors.toList());
        shuttle = academy.isShuttle();
        phone = academy.getPhone();
        businessRegistration = academy.getBusinessRegistration();
        this.upStatistics = upStatistics;
    }

    public static AcademyResponse of(Academy academy, ReferenceLikesStatistics upStatistics) {
        return new AcademyResponse(academy, upStatistics);
    }

    public static AcademyResponse of(Academy academy) {
        return of(academy, null);
    }
}
