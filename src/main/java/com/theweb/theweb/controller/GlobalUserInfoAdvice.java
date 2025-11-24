package com.theweb.theweb.controller;

import com.theweb.theweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class GlobalUserInfoAdvice {

    private final MemberRepository memberRepository;

    @ModelAttribute
    public void addUserInfo(Model model, Authentication authentication) {
        if (isLoggedIn(authentication)) {
            String username = authentication.getName();
            model.addAttribute("currentUser", username);
            memberRepository.findByUsername(username)
                    .ifPresent(member -> model.addAttribute("currentNickname", member.getNickname()));
        }
    }

    private boolean isLoggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
