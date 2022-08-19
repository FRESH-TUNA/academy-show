package project.academyshow.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.academyshow.controller.request.AcademySignUpRequest;
import project.academyshow.controller.request.LoginRequest;
import project.academyshow.controller.request.UserSignUpRequest;
import project.academyshow.controller.response.ApiResponse;
import project.academyshow.security.token.AuthToken;
import project.academyshow.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /** 일반 회원가입 */
    @PostMapping("/sign-up/user")
    public ApiResponse<?> userSignUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        authService.userSignUp(userSignUpRequest);
        return ApiResponse.success(null);
    }

    /** 학원 회원가입 */
    @PostMapping("/sign-up/academy")
    public ApiResponse<?> academySignUp(@RequestBody UserSignUpRequest userSignUpRequest,
                                        @RequestBody AcademySignUpRequest academySignUpRequest) {
        authService.academySignUp(userSignUpRequest, academySignUpRequest);
        return ApiResponse.success(null);
    }

    /** username 중복 확인 */
    @PostMapping("/sign-up/username-check")
    public ApiResponse<?> usernameCheck(@RequestBody SimpleUsernameRequest request) {
        return ApiResponse.success(authService.usernameCheck(request.getUsername()));
    }

    @Data
    private static class SimpleUsernameRequest {
        private String username;
    }

    /** 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody LoginRequest loginRequest) {
        AuthToken accessToken = authService.login(request, response, loginRequest);

        return accessToken == null ? ResponseEntity.ok(ApiResponse.authenticateFailed()) :
                ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .body(ApiResponse.success(null));
    }

    /** Access Token 재발급 */
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                          HttpServletResponse response) {
        AuthToken accessToken = authService.refresh(request, response);
        return accessToken == null ? ResponseEntity.ok(ApiResponse.authenticateFailed()) :
                ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .body(ApiResponse.success(null));
    }
}
