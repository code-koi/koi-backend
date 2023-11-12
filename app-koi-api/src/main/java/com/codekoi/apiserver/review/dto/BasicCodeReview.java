package com.codekoi.apiserver.review.dto;

import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.coreweb.formatter.BeforeTimeSerializer;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BasicCodeReview {

    private Long id;

    private String title;

    private CodeReviewStatus status;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    private UserProfileDto user;

    private List<String> skills;

    public BasicCodeReview(Long id, String title, CodeReviewStatus status, LocalDateTime createdAt, UserProfileDto user, List<String> skills) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.user = user;
        this.skills = skills;
    }

    public static List<BasicCodeReview> listFrom(List<CodeReview> reviews) {
        return reviews.stream()
                .map(BasicCodeReview::from)
                .toList();
    }

    public static BasicCodeReview from(CodeReview r) {
        return new BasicCodeReview(
                r.getId(),
                r.getTitle(),
                r.getStatus(),
                r.getCreatedAt(),
                UserProfileDto.from(r.getUser()),
                r.getSkillNames()
        );
    }
}
