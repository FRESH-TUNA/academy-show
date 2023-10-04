package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.request.TutorRequest;
import project.academyshow.controller.response.ReviewStatistics;
import project.academyshow.entity.ReferenceType;
import project.academyshow.entity.SearchType;
import project.academyshow.entity.TutorInfo;
import project.academyshow.repository.ReviewRepository;
import project.academyshow.repository.TutorInfoRepository;
import project.academyshow.security.entity.CustomUserDetails;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TutorInfoService {
    private final TutorInfoRepository tutorInfoRepository;
    private final ReviewRepository reviewRepository;

    public Page<TutorInfo> search(SearchRequest searchRequest, Pageable pageable) {
        if (searchRequest.getSearchType() == SearchType.FILTER)
            return tutorInfoRepository.findAll(searchRequest, pageable);
        else
            return tutorInfoRepository.findByQuery(searchRequest.getQ(), pageable);
    }

    public Optional<TutorInfo> findById(Long id) {
        return tutorInfoRepository.findById(id);
    }

    @Transactional
    public void edit(Long id, CustomUserDetails user, TutorRequest tutorRequest) {
        TutorInfo tutorInfo = tutorInfoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("과외 선생님을 찾을수 없습니다"));

        // 해당 academy의 소유자만 수정할수 있다.
        if (!user.isSameUser(tutorInfo.getMember().getUsername()))
            throw new RuntimeException("소유자만 수정할수 있습니다");

        tutorInfo.edit(tutorRequest);
    }

    public ReviewStatistics reviewStatistics(Long id) {
        List<Tuple> tuples = reviewRepository.countGroupByRatingForType(ReferenceType.TUTOR, id);
        return new ReviewStatistics(tuples);
    }
}
