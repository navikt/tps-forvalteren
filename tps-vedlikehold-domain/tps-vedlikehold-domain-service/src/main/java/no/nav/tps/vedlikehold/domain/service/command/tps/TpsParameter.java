package no.nav.tps.vedlikehold.domain.service.command.tps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Getter
@Setter
@NoArgsConstructor
public class TpsParameter {

    private String name;
    private TpsParameterType type;
    private String use;
    private List<?> values;

}
