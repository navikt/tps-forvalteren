package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;

import com.google.common.collect.Lists;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.FindVergemaalIdsFromPersonIds;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeletePersonerByIdIn {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private DoedsmeldingRepository doedsmeldingRepository;

    @Autowired
    private VergemaalRepository vergemaalRepository;

    @Autowired
    private FindVergemaalIdsFromPersonIds findVergemaalIdsFromPersonIds;

    public void execute(List<Long> ids) {

        if (ids.size() > ORACLE_MAX_IN_SET_ELEMENTS) {
            List<List<Long>> partitionsIds = Lists.partition(ids, ORACLE_MAX_IN_SET_ELEMENTS);
            for (List<Long> partition : partitionsIds) {
                doedsmeldingRepository.deleteByPersonIdIn(partition);
                relasjonRepository.deleteByPersonRelasjonMedIdIn(partition);
                vergemaalRepository.deleteByIdIn(findVergemaalIdsFromPersonIds.execute(partition));
                personRepository.deleteByIdIn(partition);

            }
        } else {
            doedsmeldingRepository.deleteByPersonIdIn(ids);
            relasjonRepository.deleteByPersonRelasjonMedIdIn(ids);
            vergemaalRepository.deleteByIdIn(findVergemaalIdsFromPersonIds.execute(ids));

            personRepository.deleteByIdIn(ids);
        }

    }
}
