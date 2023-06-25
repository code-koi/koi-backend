package codekoi.apiserver.domain.code.comment.controller;

import codekoi.apiserver.domain.code.comment.service.CodeCommentQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code-comments")
@RequiredArgsConstructor
public class CodeCommentController {

    private final CodeCommentQuery codeCommentQuery;
}
