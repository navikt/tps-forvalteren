package no.nav.tps.forvalteren.skdavspilleren.repository;

import java.util.List;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;

public interface SkdmeldingAvspillerdataRepository extends Repository<SkdmeldingAvspillerdata, Long> {

    List<SkdmeldingAvspillerdata> findByAvspillergruppeAndOrderBySekvensnummerAsc(Long gruppeId);
}
