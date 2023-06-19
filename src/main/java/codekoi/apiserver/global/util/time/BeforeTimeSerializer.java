package codekoi.apiserver.global.util.time;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

public class BeforeTimeSerializer extends StdSerializer<LocalDateTime> {
    protected BeforeTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    protected BeforeTimeSerializer() {
        this(null);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(CustomDateTimeFormatter.beforeTimeFormat(value));
    }
}
