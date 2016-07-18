package no.nav.tps.vedlikehold.provider.web.selftest;

import no.nav.tps.vedlikehold.provider.web.model.SelftestResult;

import java.util.List;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class JsonSelftest {
    private String applicationName;
    private String applicationVersion;
    private String timestamp;
    private  SelftestResult.Status aggregate_status;
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

    public SelftestResult.Status getAggregate_status() {
        return aggregate_status;
    }

    public void setAggregate_status( SelftestResult.Status aggregate_status) {
        this.aggregate_status = aggregate_status;
    }

    public List<SelftestResult> getChecks() {
        return checks;
    }

    public void setChecks(List<SelftestResult> checks) {
        this.checks = checks;
    }
}
