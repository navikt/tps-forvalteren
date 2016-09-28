package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ServiceRoutineResponse {

    /** The raw response from TPS */
    private String xml;

    /** A formatted response */
    private Object data;


    public ServiceRoutineResponse(String xml, Object data) {
        this.xml = xml;
        this.data = data;
    }


    public Object getData() {
        return data;
    }

    public String getXml() {
        return xml;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
