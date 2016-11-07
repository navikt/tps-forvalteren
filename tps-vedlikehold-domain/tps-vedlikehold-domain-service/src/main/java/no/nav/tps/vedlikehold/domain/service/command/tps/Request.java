package no.nav.tps.vedlikehold.domain.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;

public class Request {

    private String Xml;
    private TpsRequestServiceRoutine routineRequest;

    public String getXml() {
        return Xml;
    }

    public void setXml(String xml) {
        Xml = xml;
    }

    public TpsRequestServiceRoutine getRoutineRequest() {
        return routineRequest;
    }

    public void setRoutineRequest(TpsRequestServiceRoutine routineRequest) {
        this.routineRequest = routineRequest;
    }
}
