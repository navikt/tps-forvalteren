package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers.ServiceRoutineResolver;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsEndreNavnRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.EndringsmeldingRequestXmlTransform;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.RemoveUnauthorizedPersonsFromResponseTransformer;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.SetAuthorizedResultCountInXmlTransformer;

import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;

/**
 * Created by f148888 on 29.09.2016.
 */

public class EndreNavn implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutine resolve() {
        return aTpsServiceRoutine()
                .name("EndreNavn")
                .internalName("Endre: Navn")
                .javaClass(TpsEndreNavnRequestEndringsmelding.class)

                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .transformer()
                    .preSend(new EndringsmeldingRequestXmlTransform())
                    .postSend(new RemoveUnauthorizedPersonsFromResponseTransformer())
                    .postSend(new SetAuthorizedResultCountInXmlTransformer())
                .and()

                .parameter()
                .name("fornavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("mellomnavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("etternavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("kortnavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("tidligerenavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                .name("datoNyttNavn")
                .required()
                .type(TpsParameterType.DATE)

                .and()
                .parameter()
                .name("kilde")
                .required()
                .type(TpsParameterType.STRING)
                .values("FS22")

                .and()
                .build();
    }
}
