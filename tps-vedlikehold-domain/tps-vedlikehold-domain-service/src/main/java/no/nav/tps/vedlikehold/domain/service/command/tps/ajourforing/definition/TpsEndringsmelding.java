package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameter;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

/**
 * Created by f148888 on 29.09.2016.
 */

//TODO If I dont change this, then should just inherit from a parent TpsClass.
public class TpsEndringsmelding {
    private String name;
    private String internalName;
    private String kilde;
    private String brukerID;

    @JsonIgnore
    private Class<? extends TpsRequestEndringsmelding> javaClass;

    private List<TpsParameter> parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public Class<? extends TpsRequestEndringsmelding> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<? extends TpsRequestEndringsmelding> javaClass) {
        this.javaClass = javaClass;
    }

    public List<TpsParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TpsParameter> parameters) {
        this.parameters = parameters;
    }

    public String getKilde() {
        return kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    public String getBrukerID() {
        return brukerID;
    }

    public void setBrukerID(String brukerID) {
        this.brukerID = brukerID;
    }
}

