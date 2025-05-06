package com.dangsim.token.entity;

import com.dangsim.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name="token")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(nullable = false, length = 500)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private Token(String refrestToken, LocalDateTime expiryTime, User user) {
        this.refreshToken = refrestToken;
        this.expiryTime = expiryTime;
        this.user = user;
    }

    public void updateRefreshToken(String newToken, LocalDateTime newExpiry) {
        this.refreshToken = newToken;
        this.expiryTime = newExpiry;
    }
}
