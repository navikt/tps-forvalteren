package no.nav.tps.vedlikehold.provider.web.selftest.model;

import static no.nav.tps.vedlikehold.provider.web.selftest.model.SelftestResult.Status.FEILET;
import static no.nav.tps.vedlikehold.provider.web.selftest.model.SelftestResult.Status.OK;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class SelftestResult {
    private Status status;
    private String name;
    private long responseTime;
    private String errorMessage;

    public SelftestResult(String name) {
        this(name, OK, "");
    }

    public SelftestResult(String name, String errorMessage) {
        this(name, FEILET, errorMessage);
    }

    private SelftestResult(String name, Status status, String errorMessage) {
        this.name = name;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public enum Status {
        OK,
        FEILET
    }
}
