package codekoi.apiserver.domain.code.comment.controller;

import codekoi.apiserver.domain.code.comment.service.CodeCommentQuery;
import codekoi.apiserver.domain.code.like.service.LikeCommand;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.global.token.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code-comments")
@RequiredArgsConstructor
public class CodeCommentController {

    private final CodeCommentQuery codeCommentQuery;
    private final LikeCommand likeCommand;

    @PostMapping("/{commentId}/like")
    public void like(@AuthenticationPrincipal UserToken token, @PathVariable Long commentId) {
        likeCommand.like(token.getUserId(), commentId);
    }

    @PostMapping("/{commentId}/unlike")
    public void unlike(@AuthenticationPrincipal UserToken token, @PathVariable Long commentId) {
        likeCommand.unlike(token.getUserId(), commentId);
    }
}
