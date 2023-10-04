package project.academyshow.controller.request;

import lombok.Data;
import project.academyshow.entity.BatchLikes;
import project.academyshow.entity.Member;
import project.academyshow.entity.Subject;
import project.academyshow.entity.TutorInfo;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TutorRequest {
    private String title;
    private String introduce;
    private List<String> educations;
    private List<Subject> subjects;
    private String scholarship;
    private String certification;
    private String phone;

    public TutorInfo toEntity(Member member, BatchLikes batchLikes) {
        String educations = String.join(",", this.educations);

        String subjects = this.subjects.stream()
                .map(Subject::getName)
                .collect(Collectors.joining(","));

        return TutorInfo.builder()
                .member(member)
                .title(title)
                .introduce(introduce)
                .scholarship(scholarship)
                .certification(certification)
                .educations(educations)
                .subjects(subjects)
                .phone(phone)
                .batchLikes(batchLikes)
                .build();
    }
}
