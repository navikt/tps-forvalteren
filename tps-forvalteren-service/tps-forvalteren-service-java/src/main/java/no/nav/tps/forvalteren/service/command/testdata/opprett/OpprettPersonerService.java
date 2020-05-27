package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService.IdentKjonnTuple;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentIdenttypeFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;

@Service
@RequiredArgsConstructor
public class OpprettPersonerService {

    private final HentKjoennFraIdentService hentKjoennFraIdentService;
    private final HentIdenttypeFraIdentService hentIdenttypeFraIdentService;

    public List<Person> execute(Collection<String> tilgjengeligIdenter) {
        return opprettMedEksplisittKjoenn(tilgjengeligIdenter.stream()
                .map(ident -> IdentKjonnTuple.builder()
                        .ident(ident)
                        .build())
                .collect(Collectors.toList()));
    }

    public List<Person> opprettMedEksplisittKjoenn(List<IdentKjonnTuple> tilgjengeligIdenter) {

        List<Person> personer = Lists.newArrayListWithExpectedSize(tilgjengeligIdenter.size());

        tilgjengeligIdenter.forEach(tuple -> {
            Person person = Person.builder()
                    .ident(tuple.getIdent())
                    .identtype(hentIdenttypeFraIdentService.execute(tuple.getIdent()))
                    .kjonn(getKjonn(tuple))
                    .regdato(now())
                    .opprettetDato(now())
                    .opprettetAv(nonNull(SecurityContextHolder.getContext().getAuthentication()) ?
                            SecurityContextHolder.getContext().getAuthentication().getName() : null)
                    .build();

            personer.add(person);
        });

        return personer;
    }

    private String getKjonn(IdentKjonnTuple tuple) {
        return KjoennType.K == tuple.getKjonn() || KjoennType.M == tuple.getKjonn() ?
                tuple.getKjonn().name() :
                hentKjoennFraIdentService.execute(tuple.getIdent()).name();
    }
}
