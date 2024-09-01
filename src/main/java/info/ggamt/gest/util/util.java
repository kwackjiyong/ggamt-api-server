package info.ggamt.gest.util;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
public class util {
    public static String buildQueryString(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("Encoding not supported", e);
        }
    }
}