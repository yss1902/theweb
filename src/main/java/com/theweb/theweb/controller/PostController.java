package com.theweb.theweb.controller;

import com.theweb.theweb.domain.Post;
import com.theweb.theweb.repository.MemberRepository;
import com.theweb.theweb.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    // 목록
    @GetMapping("/")
    public String list(Model model, Authentication auth, HttpSession session) {
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts);

        if (auth != null) {
            Map<String, Object> currentUser = new HashMap<>();
            currentUser.put("username", auth.getName());
            currentUser.put("authorities", auth.getAuthorities());
            if (session != null) {
                currentUser.put("sessionId", session.getId());
            }

            model.addAttribute("currentUser", currentUser);
            memberRepository.findByUsername(auth.getName())
                    .ifPresent(member -> model.addAttribute("currentNickname", member.getNickname()));
        } else {
            model.addAttribute("currentUser", "게스트");
        }
        return "post/list";
    }

    //작성 폼
    @GetMapping("/post/new")
    public String writeForm() {
        return "post/write";
    }

    //작성 처리
    @PostMapping("/post/new")
    public String write(@RequestParam String title,
                        @RequestParam String content,
                        Authentication auth) {

        postService.create(title, content, auth);
        return "redirect:/";
    }

    // 상세
    @GetMapping("/post/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.getById(id);
        model.addAttribute("post", post);
        return "post/detail";
    }

    // 수정 폼
    @GetMapping("/post/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.getById(id);
        model.addAttribute("post", post);
        return "post/edit";
    }

    // 수정 처리
    @PostMapping("/post/{id}/edit")
    public String edit(@PathVariable Long id,
                       @RequestParam String title,
                       @RequestParam String content,
                       Authentication auth) {

        postService.update(id, title, content, auth);
        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) {

        postService.delete(id, auth);
        return "redirect:/";
    }

}
