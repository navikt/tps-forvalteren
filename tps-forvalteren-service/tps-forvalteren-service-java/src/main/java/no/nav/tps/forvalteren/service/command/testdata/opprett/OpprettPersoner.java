package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class OpprettPersoner {

    @Autowired
    private HentKjoennFraIdent hentKjoennFraIdent;


    public List<Person> execute(Collection<String> tilgjengeligIdenter) {
        List<Person> personer = new ArrayList<>();
        for (String ident : tilgjengeligIdenter) {
            Person newPerson = new Person();
            newPerson.setIdenttype(getIdenttypeFraIdent(ident));
            newPerson.setIdent(ident);
            newPerson.setKjonn(hentKjoennFraIdent.execute(ident));
            newPerson.setRegdato(LocalDateTime.now());
            personer.add(newPerson);
        }
        return personer;
    }

    private String getIdenttypeFraIdent(String ident) {
        if (Integer.parseInt(ident.substring(2, 3)) >= 2) {
            return "BNR";
        } else if (Integer.parseInt(ident.substring(0, 1)) >= 4) {
            return "DNR";
        } else {
            return "FNR";
        }
    }
}
