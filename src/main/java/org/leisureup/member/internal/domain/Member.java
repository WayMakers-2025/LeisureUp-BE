package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String email;

    private Member(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public static Member of(String nickname, String email) {
        return new Member(nickname, email);
    }
}
