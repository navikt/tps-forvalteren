package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TpsPacemaker {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpsPacemaker.class);
    private static final String DELAY_TIMEOUT_ERROR = "TpsPacemaker delay error";
    private static final long MAX_NO_OF_MSG_WITHOUT_DELAY = 1000L;
    private static final long DEFAULT_DELAY = 100L;

    public void iteration(long iteration) {
        
        if (iteration > MAX_NO_OF_MSG_WITHOUT_DELAY) {
            try {
                Thread.sleep(DEFAULT_DELAY);
            } catch (InterruptedException e) {
                LOGGER.error(String.format("%s: %s", DELAY_TIMEOUT_ERROR, e.getMessage()), e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
