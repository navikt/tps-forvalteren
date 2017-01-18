package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.endring.TpsEndreUtenlandskGironummerEndringsmeldingRequest;

import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation.egenAnsattAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseDataTransformer.extractDataFromXmlElement;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

/**
 * Created by F148888 on 17.11.2016.
 */
public class EndreUtenlandskGironummer implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return aTpsServiceRoutine()
                .name("EndreUtenlandskGironummer")
                .internalName("Endre: Utenlandsk Gironummer")
                .javaClass(TpsEndreUtenlandskGironummerEndringsmeldingRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .transformer()
                .preSend(endringsmeldingXmlWrappingAppender())
                .postSend(extractDataFromXmlElement("endreGironrUtl"))
                .postSend(extractStatusFromXmlElement("svarStatus"))

                .and()

                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("giroNrUtland")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("kodeSwift")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("kodeLand")
                .required()
                .type(TpsParameterType.STRING)
                .and()


                .parameter()
                .name("bankNavn")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("bankKode")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("valuta")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("bankAdresse1")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("bankAdresse2")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("bankAdresse3")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("datoGiroNr")
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

