package no.nav.tps.vedlikehold.domain.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;

public class Response {

    private String xml;
    private ServiceRoutineResponse routineResponse;

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public ServiceRoutineResponse getRoutineResponse() {
        return routineResponse;
    }

    public void setRoutineResponse(ServiceRoutineResponse routineResponse) {
        this.routineResponse = routineResponse;
    }

}
