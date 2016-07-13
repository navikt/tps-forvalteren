package no.nav.tps.vedlikehold.provider.web.selftest;

import no.nav.tps.vedlikehold.service.command.tpsws.PingDiskresjonskode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class DiskresjonskodeSelftest extends SubSystemSelftest {

    @Autowired
    private PingDiskresjonskode pingDiskresjonskode;

    @Override
    protected String getSubSystemName() {
        return "TPSWS - Diskresjonskode";
    }

    @Override
    protected boolean performCheck() throws Exception {
        pingDiskresjonskode.execute();
        return true;
    }
}
