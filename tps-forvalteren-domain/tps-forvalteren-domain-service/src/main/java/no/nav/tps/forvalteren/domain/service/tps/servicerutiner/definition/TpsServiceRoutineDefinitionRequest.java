package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.REQUIRED;

import java.util.List;
import java.util.stream.Collectors;
import org.codehaus.jackson.annotate.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.TpsParameter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;

@Getter
@Setter
@NoArgsConstructor
public class TpsServiceRoutineDefinitionRequest extends TpsRequestMeldingDefinition {

    private String internalName;    // (DisplayName)

    @JsonIgnore
    private Class<?> javaClass;

    private List<TpsParameter> parameters;

    @JsonIgnore
    private List<Transformer> transformers;
    
    @JsonIgnore
    public List<String> getRequiredParameterNameList() {
        return parameters.stream().filter(parameter -> REQUIRED.equals(parameter.getUse())).map(parameter -> parameter.getName()).collect(Collectors.toList());
    }
}
