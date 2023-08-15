package com.codekoi.domain.comment;

import java.util.List;

interface ReviewCommentRepositoryCustom {
    List<ReviewComment> hotCommentRank();
}
