package codekoi.apiserver.utils;

import org.springframework.test.util.ReflectionTestUtils;

public class TestEntityReflection {

    public static <T> void setId(T t, Long id) {
        ReflectionTestUtils.setField(t, "id", id);
    }
}
