package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_SUM_IN_QUERY;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

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
        if (ids.size() > ORACLE_MAX_SUM_IN_QUERY) {
            List<List<Long>> partitionsIds = Lists.partition(ids, ORACLE_MAX_SUM_IN_QUERY);
            for (List<Long> partition : partitionsIds) {
                doedsmeldingRepository.deleteByPersonIdIn(partition);
                relasjonRepository.deleteByPersonRelasjonMedIdIn(partition);
                personRepository.deleteByIdIn(partition);
            }
        } else {
            doedsmeldingRepository.deleteByPersonIdIn(ids);
            relasjonRepository.deleteByPersonRelasjonMedIdIn(ids);
            personRepository.deleteByIdIn(ids);
        }

    }
}
