package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;

@Service
@RequiredArgsConstructor
public class RelasjonExtractOpprettKriterier extends ExtractOpprettKriterier {

    @Override
    public List<Person> addExtendedKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req,
            List<Person> hovedPersoner,
            List<Person> partnere, List<Person> barn) {

        List<Adresse> adresser = isNull(req.getBoadresse()) || !req.getBoadresse().isValidAdresse() ?
                getAdresser(hovedPersoner.size() + partnere.size(), req.getAdresseNrInfo()) : new ArrayList();

        mapPartner(req, hovedPersoner, partnere, adresser);
        mapBarn(req, hovedPersoner, partnere, barn);

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }
}
