package no.nav.tps.forvalteren.service.command.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;

@Service
public class SjekkDoedsmeldingSentForPersonId {

    @Autowired
    private DoedsmeldingRepository doedsmeldingRepository;

    public Boolean execute(Long personId) {
        Doedsmelding doedsmelding = doedsmeldingRepository.findDoedsmeldingByPersonId(personId);

        if (doedsmelding != null) {
            return doedsmelding.getMeldingSendt();
        }
        return false;
    }
}
