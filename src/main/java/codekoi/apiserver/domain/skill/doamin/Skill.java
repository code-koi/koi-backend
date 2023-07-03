package codekoi.apiserver.domain.skill.doamin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Skill  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Integer searchCount;

    @Builder
    private Skill(Long id, String name, Integer searchCount) {
        this.id = id;
        this.name = name;
        this.searchCount = searchCount;
    }
}
