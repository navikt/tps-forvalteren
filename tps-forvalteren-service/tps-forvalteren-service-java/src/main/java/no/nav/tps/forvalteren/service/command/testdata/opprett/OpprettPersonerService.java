package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdent;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentIdenttypeFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Service
public class OpprettPersonerService {

    @Autowired
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Autowired
    private HentDatoFraIdent hentDatoFraIdent;

    @Autowired
    private HentIdenttypeFraIdentService hentIdenttypeFraIdentService;

    @Autowired
    private LandkodeEncoder landkodeEncoder;

    public List<Person> execute(Collection<String> tilgjengeligIdenter) {
        List<Person> personer = new ArrayList<>();
        for (String ident : tilgjengeligIdenter) {
            Person newPerson = new Person();
            newPerson.setIdenttype(hentIdenttypeFraIdentService.execute(ident));
            newPerson.setIdent(ident);
            newPerson.setKjonn(hentKjoennFraIdentService.execute(ident));
            newPerson.setRegdato(LocalDateTime.now());
            newPerson.setSivilstand("0");
            newPerson.setInnvandretFraLand(landkodeEncoder.getRandomLandTla());
            if ("FNR".equals(newPerson.getIdenttype())) {
                newPerson.setStatsborgerskap("NOR");
                newPerson.setStatsborgerskapRegdato(hentDatoFraIdent.extract(ident));
            }
            newPerson.setInnvandretFraLandFlyttedato(hentDatoFraIdent.extract(ident));
            personer.add(newPerson);
        }
        return personer;
    }
}
