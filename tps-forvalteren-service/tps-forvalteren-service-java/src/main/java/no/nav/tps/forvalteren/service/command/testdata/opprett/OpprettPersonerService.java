package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
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
                    .opprettetDato(now())
                    .opprettetAv(nonNull(SecurityContextHolder.getContext().getAuthentication()) ?
                            SecurityContextHolder.getContext().getAuthentication().getName() : null)
                    .build();

            personer.add(person);
        });

        return personer;
    }
}
