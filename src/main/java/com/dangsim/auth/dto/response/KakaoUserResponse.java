package com.dangsim.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserResponse(
        Long id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public String profileImage() {
        return kakaoAccount.profile.profileImageUrl;
    }
    public record KakaoAccount(
            KakaoProfile profile
    ){}
    public record KakaoProfile(
            @JsonProperty("profile_image_url")
            String profileImageUrl
    ){}
}
