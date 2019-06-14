package no.nav.tps.forvalteren.service.command.avspiller;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspillerProgress;
import no.nav.tps.forvalteren.repository.jpa.AvspillerProgressRepository;
import no.nav.tps.forvalteren.repository.jpa.AvspillerRepository;

@Service
@Transactional
public class AvspillerDaoService {

    @Autowired
    private AvspillerRepository avspillerRepository;

    @Autowired
    private AvspillerProgressRepository avspillerProgressRepository;

    public TpsAvspiller save(TpsAvspiller avspiller) {

        return avspillerRepository.save(avspiller);
    }

    public TpsAvspillerProgress save(TpsAvspillerProgress progress) {

        return avspillerProgressRepository.save(progress);
    }
}
