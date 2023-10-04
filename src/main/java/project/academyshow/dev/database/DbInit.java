
package project.academyshow.dev.database;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.entity.*;
import project.academyshow.entity.Likes;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class DbInit {

    private final ExampleInsert exampleInsert;

    @PostConstruct
    public void init() {
        exampleInsert.insertSubject();
        exampleInsert.insertFile();
        exampleInsert.insertNormalMember();
        exampleInsert.insertAcademyMemberAndAcademy();
        exampleInsert.insertTutorMemberAndTutorInfo();
        exampleInsert.insertAcademyReview();
        exampleInsert.insertAcademyNews();
        exampleInsert.insertUp();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class ExampleInsert {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        private final List<Member> normalMembers = new ArrayList<>();
        private final List<Member> academyMembers = new ArrayList<>();
        private final List<Member> tutorMembers = new ArrayList<>();
        private final List<Academy> academies = new ArrayList<>();
        private final List<Post> posts = new ArrayList<>();

        /** 과목 데이터 */
        public void insertSubject() {
            String[] subjects = {
                    "국어", "영어", "수학", "과학", "사회",
                    "독서/토론/논술", "경시대회", "입시컨설팅", "SW교육/코딩교육",
                    "제2외국어", "유학/SAT/AP/토플", "음악/예술", "미술", "체육",
                    "취업/자격증", "기타"
            };

            for (String subject : subjects)
                em.persist(new Subject(subject));
        }

        /** 파일 데이터 (프로필) */
        public void insertFile() {
            for (int i=10000; i<10009; i++) {
                em.createNativeQuery("insert into file_info(id, ext, path, size) values(" + i + ", '.png', '', 0)")
                        .executeUpdate();
            }
        }

        /** 일반계정 데이터 */
        public void insertNormalMember() {
            String[][] memberInfos = {
                            /* username, name, postcode, roadAddress, jibunAddress, profile */
                    {
                            "user1", "유리", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "http://localhost:8081/api/files/10000"
                    },
                    {
                            "user2", "찬진", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", null
                    },
                    {
                            "user3", "경진", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", null
                    },
                    {
                            "user4", "동원", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", null
                    },
                    {
                            "user5", "현수", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", null
                    }
            };

            Arrays.stream(memberInfos).forEach(this::createNormalMember);
        }

        /** 학원계정 및 학원 데이터 */
        public void insertAcademyMemberAndAcademy() {
            String[][] memberInfos = {
                            /* username, name, postcode, roadAddress, jibunAddress, profile */
                    {
                            "academy1", "철수", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "http://localhost:8081/api/files/10001"
                    },
                    {
                            "academy2", "두목님1", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", ""
                    },
                    {
                            "academy3", "두목님2", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", ""
                    },
                    {
                            "academy4", "두목님3", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", ""
                    },
                    {
                            "academy5", "두목님4", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", ""
                    },
                    {
                            "academy5", "두목님5", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", ""
                    }
            };

            Arrays.stream(memberInfos).forEach(this::createAcademyMember);

            String[][] academyInfos = {
                            /* postcode, roadAddress, jibunAddress, name, introduce, educations, subjects, profile, call */
                    {
                            "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "수학연구소", "최고보다 최선을 다하겠습니다.",
                            "고등학교 1학년,고등학교 2학년,고등학교 3학년", "수학", "http://localhost:8081/api/files/10003",
                            "01000000000"
                    },
                    {
                            "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "축지법 학원", "대한축지법협회 41대 회장 출신 원장 직강",
                            "중학교 1학년,중학교 2학년,중학교 3학년,고등학교 1학년,고등학교 2학년,고등학교 3학년,성인",
                            "체육", "http://localhost:8081/api/files/10004",
                            "01010001000"
                    },
                    {
                            "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "부두술 전문학원", "부두술사관학교 출신 전문교사들이 가르칩니다.",
                            "중학교 1학년,중학교 2학년,중학교 3학년", "기타", "http://localhost:8081/api/files/10005",
                            "01014001420"
                    },
                    {
                            "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "탄탄 비행술", "아직도 걸어다니십니까? 이제는 날아다니는게 대세!",
                            "고등학교 1학년,고등학교 2학년,고등학교 3학년,성인", "체육", "http://localhost:8081/api/files/10006",
                            "01193933838"
                    },
                    {
                            "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "슈퍼 영어학원", "Hello. Nice to meet you, and you?",
                            "고등학교 1학년,고등학교 2학년,고등학교 3학년,성인", "영어", "http://localhost:8081/api/files/10007",
                            "01938382302"
                    },
                    {
                            "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "펑펑 미술학원", "폭발은 예술이다.",
                            "고등학교 1학년,고등학교 2학년,고등학교 3학년,성인", "미술", "http://localhost:8081/api/files/10008",
                            "017104310d0"
                    },
            };

            for (int i=0; i<academyInfos.length; i++)
                createAcademy(academyMembers.get(i), academyInfos[i]);
        }

        /** 과외계정 및 과외 데이터 */
        public void insertTutorMemberAndTutorInfo() {
            String[][] memberInfos = {
                            /* username, name, postcode, roadAddress, jibunAddress, profile */
                    {
                            "tutor1", "수지", "13524", "경기 성남시 분당구 대왕판교로606번길 45 (판교역 프루지오 시티)",
                            "경기 성남시 분당구 삼평동 653", "http://localhost:8081/api/files/10002"
                    },
                    {
                            "tutor2", "아인즈", "13524", "강원 춘천시 남춘천역",
                            "강원 춘천시 남춘천역", null
                    },
                    {
                            "tutor3", "희준", "23411", "강원 강릉시 강릉고등학교",
                            "강원 강릉시 강릉고등학교", null
                    },
                    {
                            "tutor4", "찬진", "11271", "경북 구미시 대학로 61",
                            "경북 구미시 대학로", null
                    },
            };

            Arrays.stream(memberInfos).forEach(this::createTutorMember);

            String[][] tutorInfo = {
                            /* scholarship, educations, subjects, title, introduce, area, call */
                    {
                            "서울대학교", "중학교 1학년,중학교 2학년,중학교 3학년,고등학교 1학년,고등학교 2학년", "영어",
                            "전교 1등 만들어 드립니다!", "강남에서 제일 잘나갑니다", "서울 강남구", "02039392939"
                    },
                    {
                            "강원대학교", "중학교 1학년,중학교 2학년,중학교 3학년", "수학,영어",
                            "전교 10등 만들어 드립니다!", "믿고 맡겨만 주시죠 호호", "강원 춘천시", "01134893838"
                    },
                    {
                            "강릉원주대학교", "중학교 1학년,중학교 2학년,중학교 3학년", "사회",
                            "전교 100등 만들어 드립니다!", "신토불이 입니다.", "강원 강릉시", "01047381209"
                    },
                    {
                            "금오공과대학교", "고등학교 1학년,고등학교 2학년", "SW교육/코딩교육",
                            "뷰 고수 만들어 드립니다.", "뷰 경력 1달", "경북 구미시", "01123982937"
                    }
            };

            for (int i=0; i<tutorInfo.length; i++)
                createTutorInfo(tutorMembers.get(i), tutorInfo[i]);
        }

        /** 아카데미 리뷰 데이터 */
        public void insertAcademyReview() {
            createReview(
                    ReferenceType.ACADEMY, academies.get(0).getId(), normalMembers.get(0),
                    "고등학교 1학년", "참 못 가르쳐요", 1
            );
            createReview(
                    ReferenceType.ACADEMY, academies.get(0).getId(), normalMembers.get(1),
                    "고등학교 2학년", "잘 못 가르치는 것 같아요.", 2
            );
            createReview(
                    ReferenceType.ACADEMY, academies.get(0).getId(), academyMembers.get(1),
                    "중학교 1학년", "선생님이 잘생겼어요", 5
            );
            createReview(
                    ReferenceType.ACADEMY, academies.get(1).getId(), academyMembers.get(2),
                    "중학교 3학년", "이 사람 사기꾼입니다 믿지 마세요", 2
            );
            createReview(
                    ReferenceType.ACADEMY, academies.get(2).getId(), academyMembers.get(3),
                    "고등학교 1학년", "역시 전문가!!", 4
            );
            createReview(
                    ReferenceType.ACADEMY, academies.get(3).getId(), normalMembers.get(4),
                    "고등학교 2학년", "이제 비행기 없어도 해외여행 가능", 1
            );
            createReview(
                    ReferenceType.ACADEMY, academies.get(4).getId(), tutorMembers.get(0),
                    "고등학교 2학년", "I cant speak english", 5
            );
            createReview(
                    ReferenceType.ACADEMY, academies.get(5).getId(), academyMembers.get(5),
                    "성인", "링가르디움 레비오우사", 2
            );
        }


        public void insertUp() {
            /** 학원 데이터 */
            createUp(ReferenceType.ACADEMY, academies.get(0).getId(), normalMembers.get(0));
            createUp(ReferenceType.ACADEMY, academies.get(0).getId(), normalMembers.get(1));
            createUp(ReferenceType.ACADEMY, academies.get(1).getId(), normalMembers.get(1));
            createUp(ReferenceType.ACADEMY, academies.get(2).getId(), normalMembers.get(2));
            createUp(ReferenceType.ACADEMY, academies.get(2).getId(), normalMembers.get(3));
            createUp(ReferenceType.ACADEMY, academies.get(0).getId(), academyMembers.get(0));
            createUp(ReferenceType.ACADEMY, academies.get(1).getId(), academyMembers.get(1));
            createUp(ReferenceType.ACADEMY, academies.get(2).getId(), academyMembers.get(2));
            createUp(ReferenceType.ACADEMY, academies.get(3).getId(), academyMembers.get(3));
            createUp(ReferenceType.ACADEMY, academies.get(4).getId(), academyMembers.get(4));
            createUp(ReferenceType.ACADEMY, academies.get(5).getId(), academyMembers.get(5));
            createUp(ReferenceType.ACADEMY, academies.get(0).getId(), tutorMembers.get(0));
            createUp(ReferenceType.ACADEMY, academies.get(1).getId(), tutorMembers.get(0));
            createUp(ReferenceType.ACADEMY, academies.get(2).getId(), tutorMembers.get(0));

            /** post(게시물) 데이터 */
            createUp(ReferenceType.POST, posts.get(0).getId(), normalMembers.get(0));
            createUp(ReferenceType.POST, posts.get(0).getId(), normalMembers.get(1));
            createUp(ReferenceType.POST, posts.get(1).getId(), normalMembers.get(1));
            createUp(ReferenceType.POST, posts.get(2).getId(), normalMembers.get(2));
            createUp(ReferenceType.POST, posts.get(2).getId(), normalMembers.get(3));
            createUp(ReferenceType.POST, posts.get(0).getId(), academyMembers.get(0));
            createUp(ReferenceType.POST, posts.get(1).getId(), academyMembers.get(1));
            createUp(ReferenceType.POST, posts.get(2).getId(), academyMembers.get(2));
            createUp(ReferenceType.POST, posts.get(3).getId(), academyMembers.get(3));
            createUp(ReferenceType.POST, posts.get(4).getId(), academyMembers.get(4));
            createUp(ReferenceType.POST, posts.get(5).getId(), academyMembers.get(5));
            createUp(ReferenceType.POST, posts.get(0).getId(), tutorMembers.get(0));
            createUp(ReferenceType.POST, posts.get(1).getId(), tutorMembers.get(0));
            createUp(ReferenceType.POST, posts.get(2).getId(), tutorMembers.get(0));
        }

        /** 학원 소식 데이터 */
        public void insertAcademyNews() {
            createAcademyNews(academies.get(0), "안녕하세요. 원장 철수입니다.", "<p>우리학원 좋습니다.</p>");
            createAcademyNews(academies.get(0), "수학은 이렇게 하는겁니다.", "<p>가르쳐드리죠.</p>");
            createAcademyNews(academies.get(0), "수학 공부 합시다", "<p>ㅎㅎ</p>");
            createAcademyNews(academies.get(0), "특강 모집", "<p>빨리 안오면 마감합니다.</p>");
            createAcademyNews(academies.get(1), "축지법", "<p>슈슈슈슉</p>");
            createAcademyNews(academies.get(1), "야외 현장 실습", "<p>내용</p>");
            createAcademyNews(academies.get(2), "오늘도 부두술로...", "<p>오늘도 한 명 보냈습니다.</p>");
            createAcademyNews(academies.get(2), "간절한 것이 있나요?", "<p>간절함을 가집시다.</p>");
            createAcademyNews(academies.get(3), "날아다니는 법", "<p>슈우우웅</p>");
            createAcademyNews(academies.get(3), "날다람쥐 비행술", "<p>슈우우웅</p>");
            createAcademyNews(academies.get(3), "로켓 비행술", "<p>슈우우웅</p>");
            createAcademyNews(academies.get(4), "hello.", "<p>안녕.</p>");
            createAcademyNews(academies.get(4), "Nice to meet you.", "<p>만나서 반갑습니다.</p>");
            createAcademyNews(academies.get(5), "제 1회 원내 작품대회 결과", "<p>김찬진 학생</p>");
            createAcademyNews(academies.get(5), "제 17회 '폭발은 예술이다.' 경연대회", "<p>펑펑</p>");
            createAcademyNews(academies.get(5), "오늘의 우수 원생", "<p>김동원 학생</p>");
        }

        /** ==================================================================================================== */

        private void createAcademyNews(Academy academy, String title, String content) {
            BatchLikes batchLikes = new BatchLikes();
            em.persist(batchLikes);

            Post post = Post.builder()
                    .academy(academy)
                    .member(academy.getMember())
                    .title(title)
                    .content(content)
                    .category(PostCategory.ACADEMY_NEWS)
                    .tutorInfo(null)
                    .batchLikes(batchLikes)
                    .build();

            em.persist(post);
            posts.add(post);
        }

        private void createUp(ReferenceType type, Long referenceId, Member member) {
            switch (type) {
                case TUTOR:
                    em.persist(
                        Likes.createTutorInfoLikes(member, em.getReference(TutorInfo.class, referenceId)));
                    break;
                case ACADEMY:
                    em.persist(
                        Likes.createAcademyLikes(member, em.getReference(Academy.class, referenceId)));
                    break;
                case POST:
                    em.persist(
                        Likes.createPostLikes(member, em.getReference(Post.class, referenceId)));
                    break;
            }
        }

        private void createReview(ReferenceType type, Long referenceId, Member member,
                                  String reviewAge, String comment, int rating) {
            Review review = Review.builder()
                    .type(type)
                    .referenceId(referenceId)
                    .member(member)
                    .reviewAge(reviewAge)
                    .comment(comment)
                    .rating(rating)
                    .build();

            em.persist(review);
        }

        private void createTutorInfo(Member member, String[] info) {
            BatchLikes batchLikes = new BatchLikes();
            em.persist(batchLikes);

            TutorInfo tutorInfo = TutorInfo.builder()
                    .member(member)
                    .scholarship(info[0])
                    .certification("개인과외교습자 등록 파일 경로")
                    .educations(info[1])
                    .subjects(info[2])
                    .title(info[3])
                    .introduce(info[4])
                    .area(info[5])
                    .isExposed(true)
                    .phone(info[6])
                    .batchLikes(batchLikes)
                    .build();

            em.persist(tutorInfo);
        }

        private void createAcademy(Member member, String[] academyInfo) {
            BatchLikes batchLikes = new BatchLikes();
            em.persist(batchLikes);

            Academy academy = Academy.builder()
                    .member(member)
                    .businessRegistration("사업자등록증 파일 경로")
                    .postcode(academyInfo[0])
                    .roadAddress(academyInfo[1])
                    .jibunAddress(academyInfo[2])
                    .selectRoadAddress(false)
                    .name(academyInfo[3])
                    .shuttle(true)
                    .introduce(academyInfo[4])
                    .educations(academyInfo[5])
                    .subjects(academyInfo[6])
                    .profile(academyInfo[7])
                    .phone(academyInfo[8])
                    .batchLikes(batchLikes)
                    .build();

            em.persist(academy);
            academies.add(academy);
        }

        private void createNormalMember(String[] memberInfo) {
            Member member = createMember(memberInfo, RoleType.ROLE_MEMBER);
            em.persist(member);
            normalMembers.add(member);
        }

        private void createAcademyMember(String[] memberInfo) {
            Member member = createMember(memberInfo, RoleType.ROLE_ACADEMY);
            em.persist(member);
            academyMembers.add(member);
        }

        private void createTutorMember(String[] memberInfo) {
            Member member = createMember(memberInfo, RoleType.ROLE_TUTOR);
            em.persist(member);
            tutorMembers.add(member);
        }

        private Member createMember(String[] memberInfo, RoleType role) {
            return Member.builder()
                    .username(memberInfo[0])
                    .password(passwordEncoder.encode(memberInfo[0]))
                    .name(memberInfo[1])
                    .phone("010-0000-0000")
                    .birth(LocalDate.now())
                    .postcode(memberInfo[2])
                    .roadAddress(memberInfo[3])
                    .jibunAddress(memberInfo[4])
                    .selectRoadAddress(true)
                    .profile(memberInfo[5])
                    .role(role)
                    .providerType(ProviderType.LOCAL)
                    .build();
        }
    }
}
