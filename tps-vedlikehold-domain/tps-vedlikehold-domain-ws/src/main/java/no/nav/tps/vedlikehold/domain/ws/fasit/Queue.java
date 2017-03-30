package no.nav.tps.vedlikehold.domain.ws.fasit;



public class Queue {
    private String name;
    private String manager;

    public Queue(String name, String manager) {
        this.name    = name;
        this.manager = manager;
    }

    //TODO Fjern senere. Bare for testing av SKD melding
    public void setName(String qName){
        this.name = qName;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }
}