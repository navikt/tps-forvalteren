package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.endring.TpsEndreTIADEndringsmeldingRequest;

import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation.egenAnsattAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseDataTransformer.extractDataFromXmlElement;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

/**
 * Created by F148888 on 16.11.2016.
 */
public class EndreTIAD implements ServiceRoutineResolver{
    @Override
    public TpsServiceRoutineDefinition resolve() {
        return aTpsServiceRoutine()
                .name("EndreTIAD")
                .internalName("Endre: TIAD")
                .javaClass(TpsEndreTIADEndringsmeldingRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .transformer()
                    .preSend(endringsmeldingXmlWrappingAppender())
                    .postSend(extractDataFromXmlElement("nyAdresseNavNorge"))
                    .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .parameter()
                    .name("offentligIdent")
                    .required()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("datoTom")
                    .optional()
                    .type(TpsParameterType.DATE)
                .and()

                .parameter()
                    .name("typeAdresseNavNorge")
                    .required()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("typeTilleggslinje")
                    .optional()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("tilleggslinje")
                    .optional()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                    .name("kommunenrTIAD")
                    .optional()
                    .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("gatekode")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("gatenavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("husnr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("postnr")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("husbokstav")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("bolignr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("eiendomsnavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("postboksnr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("postboksAnlegg")
                .optional()
                .type(TpsParameterType.STRING)
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
