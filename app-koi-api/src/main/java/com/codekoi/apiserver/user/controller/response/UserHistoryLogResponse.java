package com.codekoi.apiserver.user.controller.response;

import com.codekoi.apiserver.review.dto.UserActivityHistory;
import lombok.Getter;

import java.util.List;

@Getter
public class UserHistoryLogResponse {

    private List<UserActivityHistory> logs;

    public UserHistoryLogResponse(List<UserActivityHistory> logs) {
        this.logs = logs;
    }

}
