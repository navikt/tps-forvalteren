package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@Service
public class DeleteRelasjonerByIdIn {

    @Autowired
    private RelasjonRepository relasjonRepository;

    public void execute(List<Long> personIds) {
        if (personIds.size() > ORACLE_MAX_IN_SET_ELEMENTS) {
            List<List<Long>> partitionsIds = Lists.partition(personIds, ORACLE_MAX_IN_SET_ELEMENTS);
            for (List<Long> partition : partitionsIds) {
                relasjonRepository.deleteByPersonRelasjonMedIdIn(partition);
            }
        } else {
            relasjonRepository.deleteByPersonRelasjonMedIdIn(personIds);
        }
    }
}
