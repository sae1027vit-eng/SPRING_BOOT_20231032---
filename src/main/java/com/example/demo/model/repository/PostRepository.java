package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.domain.Member; // 두 파일 모두
import com.example.demo.model.domain.Post;

@Repository // 게시글 리포지토리 : save, findAll, findById, delete 는 이미 있다
public interface PostRepository extends JpaRepository<Post, Long> {
    // [9주차 연습문제 ②] 작성자(author)의 username 으로 찾고, 번호 큰 순서(최신 글)로
    List<Post> findByAuthor_UsernameOrderByIdDesc(String username);

    // [10주차 연습문제 ①] 내가 쓴 글 페이징 : 마지막에 Pageable 만 더하면 Page 가 된다 (정렬은 Pageable 이 담당)
    Page<Post> findByAuthor_Username(String username, Pageable pageable);

    // [10주차] 단계 2 : 제목 또는 내용에 검색어가 들어간 글 (JPQL)
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> search(@Param("keyword") String keyword, Pageable pageable);

    // [10주차 연습문제 ②] 작성자 이름으로 검색 (글 → 작성자 → 이름, JOIN 은 JPA 가 자동으로)
    @Query("SELECT p FROM Post p WHERE p.author.name LIKE %:name%")
    Page<Post> searchByWriter(@Param("name") String name, Pageable pageable);

    boolean existsByAuthor(Member author); // 이 회원이 쓴 글이 있는지

}
