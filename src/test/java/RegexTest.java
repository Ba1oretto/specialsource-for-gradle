import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegexTest {
    @Test
    void minecraftVersionRegex() {
        // String version = "1.19-R0.1-SNAPSHOT";
        String version = "1.15-R0.1-SNAPSHOT";
        boolean result = version.matches("^1.(?:1(?:[1-5]|[7-9])|[8-9])-R0.1-SNAPSHOT$");
        assertTrue(result);
    }

    @Test
    void jarFilePathRegex() {
        // String version = "1.19-R0.1-SNAPSHOT";
        String path = "P:\\Projects\\homeward-plugin\\homewardlib\\build\\libs\\HomewardLib-obf.jar";
        path = path.replaceAll("(.*)-obf(.jar)$", "$1$2");
        assertEquals("P:\\Projects\\homeward-plugin\\homewardlib\\build\\libs\\HomewardLib.jar", path);
    }
}