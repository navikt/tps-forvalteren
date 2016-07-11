package no.nav.tps.vedlikehold.domain.ws.fasit;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class QueueManager {
    private String name;
    private String hostname;
    private String port;

    public QueueManager(String name, String hostname, String port) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }
}