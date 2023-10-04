package project.academyshow.security.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.academyshow.entity.Member;
import project.academyshow.entity.ProviderType;
import project.academyshow.entity.RoleType;
import project.academyshow.repository.MemberRepository;
import project.academyshow.security.entity.CustomUserDetails;
import project.academyshow.security.oauth.entity.OAuth2UserInfo;
import project.academyshow.security.oauth.entity.OAuth2UserInfoFactory;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    /** 기존 member 조회 및 업데이트, 신규 member 생성 */
    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(
                userRequest.getClientRegistration().getRegistrationId().toUpperCase()
        );

        OAuth2UserInfo oAuth2User = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Optional<Member> savedMember = memberRepository.findByUsernameAndProviderType(oAuth2User.getId(), providerType);

        /* 기존 member 여부에 따라 업데이트/생성 */
        if (savedMember.isPresent()) {
            updateUser(savedMember.get(), oAuth2User);
        } else {
            savedMember = Optional.of(createUser(oAuth2User, providerType));
        }

        return CustomUserDetails.builder()
                .providerType(providerType)
                .username(savedMember.get().getUsername())
                .attributes(oAuth2User.getAttributes())
                .role(savedMember.get().getRole())
                .build();
    }

    private Member createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        Member member = Member.builder()
                .username(userInfo.getId())
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .profile(userInfo.getImageUrl())
                .providerType(providerType)
                .role(RoleType.ROLE_MEMBER)
                .build();
        return memberRepository.saveAndFlush(member);
    }

    private void updateUser(Member member, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !member.getName().equals(userInfo.getName())) {
            member.setName(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && !member.getProfile().equals(userInfo.getImageUrl())) {
            member.setProfile(userInfo.getImageUrl());
        }
    }
}
