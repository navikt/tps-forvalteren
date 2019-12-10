package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentIdenttypeFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Service
@RequiredArgsConstructor
public class OpprettPersonerService {

    private final HentKjoennFraIdentService hentKjoennFraIdentService;
    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final HentIdenttypeFraIdentService hentIdenttypeFraIdentService;
    private final LandkodeEncoder landkodeEncoder;

    public List<Person> execute(Collection<String> tilgjengeligIdenter) {

        List<Person> personer = Lists.newArrayListWithExpectedSize(tilgjengeligIdenter.size());

        tilgjengeligIdenter.forEach(ident -> {
            Person person = Person.builder()
                    .ident(ident)
                    .identtype(hentIdenttypeFraIdentService.execute(ident))
                    .kjonn(hentKjoennFraIdentService.execute(ident))
                    .regdato(now())
                    .statsborgerskap(asList(Statsborgerskap.builder()
                            .statsborgerskap(FNR.name().equals(hentIdenttypeFraIdentService.execute(ident)) ? "NOR" :
                                    landkodeEncoder.getRandomLandTla())
                            .statsborgerskapRegdato(hentDatoFraIdentService.extract(ident))
                            .build()))
                    .opprettetDato(now())
                    .opprettetAv(nonNull(SecurityContextHolder.getContext().getAuthentication()) ?
                            SecurityContextHolder.getContext().getAuthentication().getName() : null)
                    .build();

            person.setStatsborgerskap(asList(Statsborgerskap.builder()
                    .statsborgerskap(FNR.name().equals(hentIdenttypeFraIdentService.execute(ident)) ? "NOR" :
                            landkodeEncoder.getRandomLandTla())
                    .statsborgerskapRegdato(hentDatoFraIdentService.extract(ident))
                    .person(person)
                    .build()));

            personer.add(person);
        });

        return personer;
    }
}
