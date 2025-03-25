package unv.upb.safi.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class SearchNormalizerUtil {

    public String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
