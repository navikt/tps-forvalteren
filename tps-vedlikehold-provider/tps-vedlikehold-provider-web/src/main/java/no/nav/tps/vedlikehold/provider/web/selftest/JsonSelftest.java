package no.nav.tps.vedlikehold.provider.web.selftest;

import no.nav.tps.vedlikehold.provider.web.model.SelftestResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Component
public class JsonSelftest {
    private String applicationName;
    private String applicationVersion;
    private String timestamp;
    private  SelftestResult.Status aggregateStatus;
    private List<SelftestResult> checks;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public SelftestResult.Status getAggregateStatus() {
        return aggregateStatus;
    }

    public void setAggregateStatus( SelftestResult.Status aggregateStatus) {
        this.aggregateStatus = aggregateStatus;
    }

    public List<SelftestResult> getChecks() {
        return checks;
    }

    public void setChecks(List<SelftestResult> checks) {
        this.checks = checks;
    }
}
