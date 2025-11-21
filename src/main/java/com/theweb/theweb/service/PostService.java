package com.theweb.theweb.service;

import com.theweb.theweb.domain.Member;
import com.theweb.theweb.domain.Post;
import com.theweb.theweb.exception.ForbiddenException;
import com.theweb.theweb.exception.UnauthorizedException;
import com.theweb.theweb.repository.MemberRepository;
import com.theweb.theweb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post getById(Long id) {
        return getPostOrThrow(id);
    }

    public void create(String title, String content, Authentication auth) {

        if (auth == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Member writer = memberRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ForbiddenException("작성 권한이 없습니다."));

        Post post = Post.builder()
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .writer(writer)
                .build();

        postRepository.save(post);
    }

    public void update(Long id, String title, String content, Authentication auth) {

        Post post = getPostOrThrow(id);

        // 권한 체크: 작성자 또는 admin인지
        if (!canModify(post, auth)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

    public void delete(Long id, Authentication auth) {

        Post post = getPostOrThrow(id);

        if (!canModify(post, auth)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    private Post getPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    private boolean canModify(Post post, Authentication auth) {

        if (auth == null) return false;

        String username = auth.getName();

        // 작성자면 허용
        if (post.getWriter().getUsername().equals(username)) {
            return true;
        }

        // ADMIN 권한 있으면 허용
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }


}
