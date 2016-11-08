package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsHentPersonRequestServiceRoutine;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public class S004HentPersonopplysningerServiceRoutineResolver implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutine resolve() {
        return aTpsServiceRoutine()
                .name("FS03-FDNUMMER-PERSDATA-O")
                .internalName("S004 Hent Personopplysninger")
                .javaClass(TpsHentPersonRequestServiceRoutine.class)
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

                .securityBuilder()
                    .addRequiredRole("0000-GA-NORG_Skriv")
                    .addRequiredSearchAuthorisationStrategy(new DiskresjonskodeAuthorisationStrategy("fnr"))
                    .addRequiredSearchAuthorisationStrategy(new EgenAnsattAuthorisationStrategy("fnr"))
                    .addRequiredSearchAuthorisationStrategy(new ReadAuthorisationStrategy("environment"))
                    .addSecurity()

                .build();
    }
}
