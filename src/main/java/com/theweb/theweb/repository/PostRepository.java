package com.theweb.theweb.repository;

import com.theweb.theweb.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
