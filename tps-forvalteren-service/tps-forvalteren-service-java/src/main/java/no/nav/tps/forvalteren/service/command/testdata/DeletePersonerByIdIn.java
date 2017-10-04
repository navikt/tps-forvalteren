package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeletePersonerByIdIn {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    public void execute(List<Long> ids){
        relasjonRepository.deleteByPersonRelasjonMedIdIn(ids);
        personRepository.deleteByIdIn(ids);
    }

}
