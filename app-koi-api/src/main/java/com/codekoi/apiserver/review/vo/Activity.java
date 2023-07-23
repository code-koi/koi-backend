package com.codekoi.apiserver.review.vo;

import java.time.LocalDateTime;

public record Activity(ActivityHistories.Type type, Long id, String targetText, LocalDateTime createdAt) {
}