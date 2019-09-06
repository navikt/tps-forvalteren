package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.security.SecureRandom;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class DummyLanguageService {

    private static final String[] DEFAULT_LANGUAGES = { "BO", "BS", "CS", "DE", "EN", "EO", "FI", "FR", "HI", "JA", "PL", "RU", "ZH" };
    private Random random = new SecureRandom();

    public String getRandomLanguage() {
        return DEFAULT_LANGUAGES[random.nextInt(DEFAULT_LANGUAGES.length - 1)];
    }
}
