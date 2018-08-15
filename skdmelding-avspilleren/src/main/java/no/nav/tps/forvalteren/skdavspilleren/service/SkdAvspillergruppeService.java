package no.nav.tps.forvalteren.skdavspilleren.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.repository.AvspillergruppeRepository;

@Service
public class SkdAvspillergruppeService {
    
    @Autowired
    private AvspillergruppeRepository avspillergruppeRepository;
    
    public Iterable<Avspillergruppe> getAllAvspillergrupper() {
        return avspillergruppeRepository.findAll();
    }
    
    public void opprettGruppe(Avspillergruppe avspillergruppe) {
        avspillergruppeRepository.save(avspillergruppe);
    }
}
