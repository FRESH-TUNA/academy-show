package project.academyshow.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.academyshow.controller.request.AcademyInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AcademyTests {
    @Test
    @DisplayName("학원 도메인 덮어쓰기 테스트")
    public void editTest() {
        /**
         * given
         */
        Academy academy = new Academy();
        AcademyInfo academyInfo = AcademyInfoForEditTest();

        /**
         * when
         */
        academy.edit(academyInfo);


        /**
         * when
         * 모든 값이 정상적으로 덮어쓰기 되었는지 확인한다.
         */
        assertThat(checkEditSuccess(academy, academyInfo)).isTrue();
    }


    private AcademyInfo AcademyInfoForEditTest() {
        AcademyInfo academyInfo = new AcademyInfo();

        List<Subject> newSubjects = Arrays
                .stream(new Subject[]{new Subject("국어"), new Subject("영어"), new Subject("수학")})
                .collect(Collectors.toList());

        List<String> newEducations = Arrays
                .stream(new String[]{"초등학교 1학년", "초등학교 2학년", "초등학교 3학년"})
                .collect(Collectors.toList());

        /**
         * reset all fields
         */
        academyInfo.setAcademyName("부두술학원");

        academyInfo.setEducations(newEducations);
        academyInfo.setSubjects(newSubjects);
        academyInfo.setShuttle(true);

        academyInfo.setAcademyPostcode("22222");
        academyInfo.setAcademyRoadAddress("새로운 도로명주소");
        academyInfo.setAcademyJibunAddress("새로운 지번주소");

        academyInfo.setAcademySubAddress("100동 100호");
        academyInfo.setAcademySelectRoadAddress(true);

        academyInfo.setIntroduce("새로운 학원 입니다");
        academyInfo.setRegistrationFile("새로운 사업자 등록증");

        academyInfo.setProfile("새로운 프로필");
        academyInfo.setPhone("새로운 연락처");

        return academyInfo;
    }

    private boolean checkEditSuccess(Academy academy, AcademyInfo academyInfo) {
        return academy.getName().equals(academyInfo.getAcademyName())
                && academy.getEducations().equals(String.join(",", academyInfo.getEducations()))
                && academy.getSubjects().equals(String.join(",", academyInfo.getSubjects().stream()
                    .map(Subject::getName)
                    .collect(Collectors.joining(","))))
                && academy.isShuttle() == academyInfo.isShuttle()

                && academy.getPostcode().equals(academyInfo.getAcademyPostcode())
                && academy.getRoadAddress().equals(academyInfo.getAcademyRoadAddress())
                && academy.getJibunAddress().equals(academyInfo.getAcademyJibunAddress())
                && academy.getSubAddress().equals(academyInfo.getAcademySubAddress())
                && academy.isSelectRoadAddress() == academyInfo.isAcademySelectRoadAddress()

                && academy.getIntroduce().equals(academyInfo.getIntroduce())
                && academy.getBusinessRegistration().equals(academyInfo.getRegistrationFile())
                && academy.getProfile().equals(academyInfo.getProfile())
                && academy.getPhone().equals(academyInfo.getPhone());
    }
}
