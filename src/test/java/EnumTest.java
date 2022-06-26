import dev.barroit.specialsource.enumerate.MinecraftVersion;

import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

public class EnumTest {

    @Test
    void testGetVersion() {
        MinecraftVersion version = MinecraftVersion.getVersion("1.8-R0.1-SNAPSHOT");
        assertSame(MinecraftVersion.v1_8, version);
    }
}
