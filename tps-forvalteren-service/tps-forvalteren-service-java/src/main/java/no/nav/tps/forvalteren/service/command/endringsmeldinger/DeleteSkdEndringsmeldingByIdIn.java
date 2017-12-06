package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
public class DeleteSkdEndringsmeldingByIdIn {

    @Autowired
    private SkdEndringsmeldingRepository repository;

    private static final int ORACLE_MAX_SUM_IN_QUERY = 1000;

    public void execute(List<Long> ids) {
        if (ids.size() > ORACLE_MAX_SUM_IN_QUERY) {
            List<List<Long>> partitionsIds = Lists.partition(ids, 1000);
            for (List<Long> partition : partitionsIds) {
                repository.deleteByIdIn(partition);
            }
        } else {
            repository.deleteByIdIn(ids);
        }
    }
}
