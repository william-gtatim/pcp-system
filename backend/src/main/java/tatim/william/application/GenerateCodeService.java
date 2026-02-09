package tatim.william.application;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;

@ApplicationScoped
public class GenerateCodeService {
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generate(){
        long value = Math.abs(RANDOM.nextLong());
        return Long.toString(value, 36).toUpperCase().substring(0, 6);
    }
}
