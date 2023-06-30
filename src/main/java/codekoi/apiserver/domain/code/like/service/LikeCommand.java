package codekoi.apiserver.domain.code.like.service;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.like.domain.Like;
import codekoi.apiserver.domain.code.like.exception.LikeNotFoundException;
import codekoi.apiserver.domain.code.like.repository.LikeRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeCommand {
    private final LikeRepository likeRepository;
    private final CodeReviewCommentRepository commentRepository;
    private final UserRepository userRepository;

    public void like(Long userId, Long commentId) {
        final User user = userRepository.findByUserId(userId);
        final CodeReviewComment comment = commentRepository.findByCommentId(commentId);

        comment.addLikeCount();

        final Like like = Like.of(user, comment);
        likeRepository.save(like);
    }

    public void unlike(Long userId, Long commentId) {
        final CodeReviewComment comment = commentRepository.findByCommentId(commentId);
        final Like like = likeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(LikeNotFoundException::new);

        comment.minusLikeCount();
        likeRepository.delete(like);
    }
}
