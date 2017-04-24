package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import no.nav.tps.forvalteren.service.command.tps.transformation.TransformStrategy;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;

public interface ResponseTransformStrategy extends TransformStrategy {

    void execute(Response response, Transformer transformer);

}
