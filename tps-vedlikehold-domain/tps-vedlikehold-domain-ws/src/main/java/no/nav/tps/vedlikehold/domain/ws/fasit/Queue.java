package no.nav.tps.vedlikehold.domain.ws.fasit;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class Queue {
    private String name;
    private String manager;

    public Queue(String name, String manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }
}