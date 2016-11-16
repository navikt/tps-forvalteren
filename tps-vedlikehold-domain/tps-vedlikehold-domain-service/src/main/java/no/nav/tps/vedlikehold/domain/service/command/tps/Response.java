package no.nav.tps.vedlikehold.domain.service.command.tps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response {

    private ResponseStatus status;
    private String rawXml;
    private Integer totalHits;
    private List<String> dataXmls;
    private TpsRequestContext context;
    private TpsServiceRoutineDefinition serviceRoutine;

    public void addDataXml(String xml) {
        if (dataXmls == null) {
            dataXmls = new ArrayList<>();
        }
        dataXmls.add(xml);
    }

}
