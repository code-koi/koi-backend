package com.codekoi.apiserver.like.controller;

import com.codekoi.apiserver.like.service.LikeService;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeRestController {

    private final LikeService likeService;

    @PostMapping("/code-comments/{commentId}")
    public void like(@AuthenticationPrincipal AuthInfo authInfo, @PathVariable Long commentId) {
        likeService.like(authInfo.getUserId(), commentId);
    }

    @DeleteMapping("/code-comments/{commentId}")
    public void unlike(@AuthenticationPrincipal AuthInfo authInfo, @PathVariable Long commentId) {
        likeService.unlike(authInfo.getUserId(), commentId);
    }
}
