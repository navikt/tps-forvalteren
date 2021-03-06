package no.nav.tps.forvalteren.service.kodeverk;

import org.apache.cxf.staxutils.StaxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KodeverkSynchronizer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(KodeverkSynchronizer.class);

    private static final String MINUTE = " 00 ";
    private static final String HOUR = " 03 ";
    private static final String CRON_EXPRESSION = "0" + MINUTE + HOUR + "* * *";

    @Autowired
    private KodeverkUpdater kodeverkUpdater;

    private boolean initialUpdateComplete = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!initialUpdateComplete) {
            initialUpdateComplete = true;
            StaxUtils.setInnerElementCountThreshold(-1);    //TODO Ellers faar man InneTreshold max 5000. Er dette greit??
            updateKodeverk();
        }
    }

    @Scheduled(cron = CRON_EXPRESSION)
    public void updateKodeverk() {
        try {
            kodeverkUpdater.updateTpsfKodeverkCache();
        } catch (Exception e) {
            LOG.error("Failed to update Kodeverk", e);
        }
    }
}
