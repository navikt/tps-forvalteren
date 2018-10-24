package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentIdenttypeFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;

@Service
public class OpprettPersonerService {

    @Autowired
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private HentIdenttypeFraIdentService hentIdenttypeFraIdentService;

    public List<Person> execute(Collection<String> tilgjengeligIdenter) {
        List<Person> personer = Lists.newArrayListWithExpectedSize(tilgjengeligIdenter.size());

        for (String ident : tilgjengeligIdenter) {
            Person newPerson = new Person();
            newPerson.setIdenttype(hentIdenttypeFraIdentService.execute(ident));
            newPerson.setIdent(ident);
            newPerson.setKjonn(hentKjoennFraIdentService.execute(ident));
            newPerson.setRegdato(LocalDateTime.now());
            newPerson.setSivilstand("0");
            if ("FNR".equals(newPerson.getIdenttype())) {
                newPerson.setStatsborgerskap("NOR");
                newPerson.setStatsborgerskapRegdato(hentDatoFraIdentService.extract(ident));
            }
            newPerson.setOpprettetDato(LocalDateTime.now());
            newPerson.setOpprettetAv(SecurityContextHolder.getContext().getAuthentication() != null ?
                    SecurityContextHolder.getContext().getAuthentication().getName() : null);
            personer.add(newPerson);
        }
        return personer;
    }
}
