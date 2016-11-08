package no.nav.tps.vedlikehold.domain.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;

public class Response {

    private String xml;
    private ServiceRoutineResponse routineResponse;
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
