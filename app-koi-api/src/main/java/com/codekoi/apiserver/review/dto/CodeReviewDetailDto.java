package com.codekoi.apiserver.review.dto;

import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.coreweb.formatter.BeforeTimeSerializer;
import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CodeReviewDetailDto {

    private UserProfileDto user;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    private String title;

    private List<String> skills = new ArrayList<>();

    private CodeReviewStatus status;

    private Boolean isFavorite;

    private Boolean me;

    public CodeReviewDetailDto(UserProfileDto user, LocalDateTime createdAt, String title, List<String> skills, CodeReviewStatus status, Boolean isFavorite, Boolean me) {
        this.user = user;
        this.createdAt = createdAt;
        this.title = title;
        this.skills = skills;
        this.status = status;
        this.isFavorite = isFavorite;
        this.me = me;
    }

    public static CodeReviewDetailDto of(CodeReview codeReview, boolean isFavorite, boolean me) {
        final UserProfileDto profileDto = UserProfileDto.from(codeReview.getUser());
        final List<String> skillNames = codeReview.getSkillNames();

        return new CodeReviewDetailDto(profileDto, codeReview.getCreatedAt(), codeReview.getTitle(), skillNames,
                codeReview.getStatus(), isFavorite, me);
    }
}