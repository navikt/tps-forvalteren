package no.nav.tps.vedlikehold.domain.rs;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class QueueManager {

    private String name;
    private String hostname;
    private String port;

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
