package codekoi.apiserver.domain.user.dto.response;

import codekoi.apiserver.domain.code.review.dto.UserActivityHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class UserHistoryLogResponse {

    private List<UserActivityHistory> logs;

    public UserHistoryLogResponse(List<UserActivityHistory> logs) {
        this.logs = logs;
    }

    public static UserHistoryLogResponse from(List<UserActivityHistory> histories) {
        return new UserHistoryLogResponse(histories);
    }
}
