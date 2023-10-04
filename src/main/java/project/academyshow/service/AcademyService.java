package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.AcademyInfo;
import project.academyshow.controller.request.SearchRequest;
import project.academyshow.controller.response.ReviewStatistics;
import project.academyshow.entity.Academy;
import project.academyshow.entity.ReferenceType;

import project.academyshow.entity.SearchType;
import project.academyshow.repository.AcademyRepository;
import project.academyshow.repository.ReviewRepository;
import project.academyshow.security.entity.CustomUserDetails;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcademyService {

    private final AcademyRepository academyRepository;
    private final ReviewRepository reviewRepository;

    public Page<Academy> search(SearchRequest searchRequest, Pageable pageable) {
        if (searchRequest.getSearchType() == SearchType.FILTER)
            return academyRepository.findAll(searchRequest, pageable);
        else
            return academyRepository.findByNameContaining(searchRequest.getQ(), pageable);
    }

    public Optional<Academy> findById(Long id) {
        return academyRepository.findById(id);
    }

    @Transactional
    public void edit(Long id, CustomUserDetails user, AcademyInfo academyInfo) {
        Academy academy = this.findById(id)
                .orElseThrow(() -> new RuntimeException("아카데믹 없습니다"));

        // 해당 academy의 소유자만 수정할수 있다.
        if (!user.isSameUser(academy.getMember().getUsername()))
            throw new RuntimeException("소유자만 수정할수 있습니다");

        academy.edit(academyInfo);
    }

    public ReviewStatistics reviewStatistics(Long id) {
        List<Tuple> tuples = reviewRepository.countGroupByRatingForType(ReferenceType.ACADEMY, id);
        return new ReviewStatistics(tuples);
    }
}
