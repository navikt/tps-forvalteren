package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.temp;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 12.07.2016.
 */
public class ServiceResponse {

    private String xml;

    private Object object;

    public Object getObject() {
        return object;
    }

    public String getXml() {
        return xml;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
