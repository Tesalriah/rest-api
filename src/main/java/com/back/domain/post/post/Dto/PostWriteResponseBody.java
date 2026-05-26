package com.back.domain.post.post.Dto;

public record PostWriteResponseBody (
        long totalCount,
        PostDto post
) {
}