package com.dangsim.auth.service;

import com.dangsim.auth.dto.response.KakaoUserResponse;
import com.dangsim.auth.entity.Auth;
import com.dangsim.auth.entity.AuthProvider;
import com.dangsim.auth.repository.AuthRepository;
import com.dangsim.jwt.JwtProvider;
import com.dangsim.token.entity.Token;
import com.dangsim.token.repository.TokenRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final WebClient webClient;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final TokenRepository tokenRepository;

    @Value("${oauth.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${oauth.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private static final long REFRESH_TOKEN_EXPIRE_DAYS = 1L;

    public Map<String, String> handleKakaoLogin(String code) {
        String kakaoAccessToken = getKakaoAccessToken(code);
        KakaoUserResponse kakaoUser = getKakaoUserInfo(kakaoAccessToken);

        String oauthId = "kakao_"+ kakaoUser.id();
        AuthProvider provider = AuthProvider.KAKAO;

        Optional<Auth> authOpt = authRepository.findByProviderAndProviderId(provider, oauthId);

        User user = authOpt
                .map(Auth::getUser)
                .orElseGet(() -> {
                    User newUser = userRepository.save(User.of(kakaoUser.profileImage()));
                    authRepository.save(Auth.of(provider, oauthId, newUser));
                    return newUser;
                });

        //JWT 발급
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken();

        Token token = tokenRepository.findByUser(user)
                .map(t -> {
                    t.updateRefreshToken(refreshToken, LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAYS));
                    return t;
                })
                .orElse(Token.builder()
                        .refrestToken(refreshToken)
                        .expiryTime(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAYS))
                        .user(user)
                        .build());

        tokenRepository.save(token);

        return Map.of(
                "access_token", accessToken,
                "refresh_token", refreshToken,
                "role", user.getRole().name()
        );
    }

    private String getKakaoAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("client_secret", kakaoClientSecret);
        params.add("code", code);

        return webClient.post()
                .uri(kakaoTokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(Map.class)
                .map(body -> (String) body.get("access_token"))
                .block();
    }

    private KakaoUserResponse getKakaoUserInfo(String accessToken){
        return webClient.get()
                .uri(kakaoUserInfoUri)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();
    }

    @Transactional
    public void logout(Long userId, String accessToken) {
        //토큰 테이블 삭제
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        tokenRepository.deleteByUser(user);
    }

    @Transactional
    public Map<String, String> reissue(Long userId, String oldRefreshToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Token token = tokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("RefreshToken 없음. 다시 로그인하세요."));

        if (!token.getRefreshToken().equals(oldRefreshToken)) {
            throw new IllegalStateException("RefreshToken 불일치. 다시 로그인 필요.");
        }

        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new IllegalStateException("RefreshToken 만료. 다시 로그인하세요.");
        }

        // 새 access/refresh 발급
        String newAccessToken = jwtProvider.createAccessToken(user.getId());
        String newRefreshToken = jwtProvider.createRefreshToken();

        token.updateRefreshToken(newRefreshToken, LocalDateTime.now().plusDays(1));
        tokenRepository.save(token);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }

}
