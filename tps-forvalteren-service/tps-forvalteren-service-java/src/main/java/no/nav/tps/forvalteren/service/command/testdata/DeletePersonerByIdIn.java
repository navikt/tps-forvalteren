package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@Service
public class DeletePersonerByIdIn {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private DoedsmeldingRepository doedsmeldingRepository;

    public void execute(List<Long> ids) {
        doedsmeldingRepository.deleteByPersonIdIn(ids);
        relasjonRepository.deleteByPersonRelasjonMedIdIn(ids);
        personRepository.deleteByIdIn(ids);
    }
}
