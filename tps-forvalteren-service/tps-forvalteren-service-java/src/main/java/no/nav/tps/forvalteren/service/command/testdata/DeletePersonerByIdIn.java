package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.util.List;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.FindVergemaalIdsFromPersonIds;

@Service
@RequiredArgsConstructor
public class DeletePersonerByIdIn {

    private final PersonRepository personRepository;
    private final RelasjonRepository relasjonRepository;
    private final DoedsmeldingRepository doedsmeldingRepository;
    private final VergemaalRepository vergemaalRepository;
    private final FindVergemaalIdsFromPersonIds findVergemaalIdsFromPersonIds;

    public void execute(List<Long> ids) {

        List<List<Long>> partitionsIds = Lists.partition(ids, ORACLE_MAX_IN_SET_ELEMENTS);
        for (List<Long> partition : partitionsIds) {
            doedsmeldingRepository.deleteByPersonIdIn(partition);
            relasjonRepository.deleteByPersonRelasjonMedIdIn(partition);
            vergemaalRepository.deleteByIdIn(findVergemaalIdsFromPersonIds.execute(partition));
            personRepository.deleteByIdIn(partition);
        }
    }
}
