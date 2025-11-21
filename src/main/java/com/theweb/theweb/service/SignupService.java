package com.theweb.theweb.service;

import com.theweb.theweb.domain.Member;
import com.theweb.theweb.domain.MemberRole;
import com.theweb.theweb.domain.Role;
import com.theweb.theweb.dto.SignupForm;
import com.theweb.theweb.repository.MemberRepository;
import com.theweb.theweb.repository.MemberRoleRepository;
import com.theweb.theweb.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(SignupForm form) {

        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setPassword(passwordEncoder.encode(form.getPassword()));
        member.setNickname(form.getNickname());

        memberRepository.save(member);

        Role userRole = roleRepository.findByRoleName("ROLE_USER");

        MemberRole mr = MemberRole.builder()
                .member(member)
                .role(userRole)
                .build();

        memberRoleRepository.save(mr);
    }

}