package codekoi.apiserver.domain.user.dto;


import codekoi.apiserver.domain.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profileImageUrl;

    private String nickname;
    private Long id;

    public UserProfileDto(String profileImageUrl, String nickname, Long id) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.id = id;
    }

    public static UserProfileDto from(User user) {
        return new UserProfileDto(user.getProfileImageUrl(), user.getNickname(), user.getId());
    }
}
