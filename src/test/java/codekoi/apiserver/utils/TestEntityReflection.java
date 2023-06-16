package codekoi.apiserver.utils;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class TestEntityReflection {

    public static <T> void setId(T t, Long id) {
        ReflectionTestUtils.setField(t, "id", id);
    }

    public static <T> void setCreatedAt(T t, LocalDateTime localDateTime) {
        ReflectionTestUtils.setField(t, "createdAt", localDateTime);
    }
}
