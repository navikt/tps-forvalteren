package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.response;

/**
 * Created by f148888 on 29.09.2016.
 */
public class EndringsmeldingResponse {

    String xml;
    Object data;

    public EndringsmeldingResponse(String xml, Object data) {
        this.xml = xml;
        this.data = data;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
