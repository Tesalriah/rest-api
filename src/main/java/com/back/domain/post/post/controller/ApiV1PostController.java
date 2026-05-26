package com.back.domain.post.post.controller;

import com.back.domain.post.post.Dto.PostDto;
import com.back.domain.post.post.Dto.PostWriteRequestBody;
import com.back.domain.post.post.Dto.PostWriteResponseBody;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {
    private final PostService postService;

    @GetMapping
    public List<PostDto> getItems(){
        List<Post> items = postService.getList();
        return items.stream().map(PostDto::new).toList();
    }

    @GetMapping("/{id}")
    public PostDto getItem(@PathVariable long id){
        Post item = postService.findById(id);
        return new PostDto(item);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<PostDto> delete(@PathVariable long id){
        Post post = postService.findById(id);

        postService.delete(post);

        return new RsData<>("200-1", "%d번 게시글이 삭제되었습니다.".formatted(id), new PostDto(post));
    }

    @PostMapping
    @Transactional
    public RsData<PostDto> write(@Valid @RequestBody PostWriteRequestBody reqBody) {
        Post post = postService.create(reqBody.title(), reqBody.content());
        return new RsData<>(
                "201-1",
                "%d번 게시글이 생성되었습니다.".formatted(post.getId()),
                new PostDto(post));
    }

    @PutMapping("/{id}")
    @Transactional
    public RsData<PostWriteResponseBody> modify(@PathVariable long id, @Valid @RequestBody PostWriteRequestBody reqBody) {
        Post post = postService.findById(id);
        post.modify(reqBody.title(), reqBody.content());
        postService.modify(post);

        return new RsData<>(
                "200-1",
                "%d번 게시글이 수정되었습니다.".formatted(post.getId()),
                new PostWriteResponseBody(postService.count(), new PostDto(post))
        );
    }
}
