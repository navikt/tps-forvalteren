package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ServiceRutineResponse {

    /** The raw response from TPS */
    private String xml;

    /** A formatted response */
    private Object data;


    public ServiceRutineResponse(String xml, Object data) {
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
