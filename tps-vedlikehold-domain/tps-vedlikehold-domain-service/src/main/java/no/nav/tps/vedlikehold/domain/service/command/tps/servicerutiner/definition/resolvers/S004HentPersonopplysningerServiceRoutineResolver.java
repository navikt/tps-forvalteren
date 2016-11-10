package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation.egenAnsattAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisation.readAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsHentPersonRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.hent.TpsHentPersonServiceRoutineRequest;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public class S004HentPersonopplysningerServiceRoutineResolver implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return aTpsServiceRoutine()
                .name("FS03-FDNUMMER-PERSDATA-O")
                .internalName("S004 Hent Personopplysninger")
                .javaClass(TpsHentPersonServiceRoutineRequest.class)
                .parameter()
                    .name("fnr")
                    .required()
                    .type(TpsParameterType.STRING)
                .and()
                .parameter()
                    .name("aksjonsKode")
                    .required()
                    .type(TpsParameterType.STRING)
                    .values("A0", "A2", "B0", "B2", "C0")
                .and()
                .parameter()
                    .name("aksjonsDato")
                    .optional()
                    .type(TpsParameterType.DATE)
                .and()
                .transformer()
                    .preSend(serviceRoutineXmlWrappingAppender())
                .and()

                .securityBuilder()
                    .addRequiredRole("0000-GA-NORG_Skriv")
                    .addRequiredSearchAuthorisationStrategy(diskresjonskodeAuthorisation())
                    .addRequiredSearchAuthorisationStrategy(egenAnsattAuthorisation())
                    .addRequiredSearchAuthorisationStrategy(readAuthorisation())
                    .addSecurity()

                .build();
    }
}
