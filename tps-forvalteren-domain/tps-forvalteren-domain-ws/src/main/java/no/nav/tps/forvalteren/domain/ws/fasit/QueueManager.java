package no.nav.tps.forvalteren.domain.ws.fasit;


public class QueueManager {
    private String name;
    private String hostname;
    private String port;
    private String channel;

    public QueueManager(String name, String hostname, String port) {
        this.name     = name;
        this.hostname = hostname;
        this.port     = port;
    }

    public QueueManager(String name, String hostname, String port, String channel) {
        this.name     = name;
        this.hostname = hostname;
        this.port     = port;
        this.channel  = channel;
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

    public String getChannel() {
        return channel;
    }
}