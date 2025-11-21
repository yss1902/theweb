package com.theweb.theweb.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String nickname;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Set<MemberRole> roles = new HashSet<>();
}
