package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.endring.TpsEndreNorskGironummerEndringsmeldingRequest;

import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation.egenAnsattAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender;

/**
 * Created by F148888 on 16.11.2016.
 */
public class EndreNorskGironummer implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return aTpsServiceRoutine()
                    .name("EndreNorksGironummer")
                    .internalName("Endre: Norsk Gironummer")
                    .javaClass(TpsEndreNorskGironummerEndringsmeldingRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .transformer()
                    .preSend(endringsmeldingXmlWrappingAppender())

                .and()


                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("giroNrNorsk")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("datogiroNrNorsk")
                .required()
                .type(TpsParameterType.DATE)
                .and()

                .parameter()
                .name("kilde")
                .required()
                .type(TpsParameterType.STRING)
                .values("FS22")

                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}
