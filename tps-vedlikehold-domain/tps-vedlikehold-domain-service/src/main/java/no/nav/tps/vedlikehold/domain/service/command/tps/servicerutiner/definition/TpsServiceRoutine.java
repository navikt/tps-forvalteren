package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import java.util.List;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsMessage;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class TpsServiceRoutine extends TpsMessage {

    @JsonIgnore
    private Class<? extends TpsRequestServiceRoutine> javaClass;

    public Class<? extends TpsRequestServiceRoutine> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<? extends TpsRequestServiceRoutine> javaClass) {
        this.javaClass = javaClass;
    }

}
