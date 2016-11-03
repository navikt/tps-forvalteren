package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsMessage;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameter;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

/**
 * Created by f148888 on 29.09.2016.
 */

//TODO If I dont change this, then should just inherit from a parent TpsClass.
public class TpsEndringsmelding extends TpsMessage {

    @JsonIgnore
    private Class<? extends TpsRequestEndringsmelding> javaClass;

    public Class<? extends TpsRequestEndringsmelding> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<? extends TpsRequestEndringsmelding> javaClass) {
        this.javaClass = javaClass;
    }

}

