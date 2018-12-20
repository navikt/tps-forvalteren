package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
public class SkdEndringsmeldingService {

    private static final int ANTALL_MELDINGER_PER_PAGE = 10;

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private SkdEndringsmeldingGruppeRepository gruppeRepository;

    public int countMeldingerByGruppe(SkdEndringsmeldingGruppe gruppe) {
        return skdEndringsmeldingRepository.countByGruppe(gruppe);
    }

    public List<SkdEndringsmelding> findSkdEndringsmeldingerOnPage(Long gruppeId, int pageNumber) {
        SkdEndringsmeldingGruppe gruppe = gruppeRepository.findById(gruppeId);
        return skdEndringsmeldingRepository.findAllByGruppe(gruppe, new PageRequest(pageNumber, ANTALL_MELDINGER_PER_PAGE)).getContent();
    }

    public List<RsMeldingstype> convertSkdEndringsmeldingerToRsMeldingstyper(List<SkdEndringsmelding> skdEndringsmeldinger) throws IOException {
        List<RsMeldingstype> rsMeldingstypeMeldinger = new ArrayList<>(skdEndringsmeldinger.size());
        ObjectMapper objectMapper = new ObjectMapper();

        for (SkdEndringsmelding skdEndringsmelding : skdEndringsmeldinger) {
            rsMeldingstypeMeldinger.add(objectMapper.readValue(skdEndringsmelding.getEndringsmelding(), RsMeldingstype.class));
        }

        return rsMeldingstypeMeldinger;
    }

    public Set<String> filtrerIdenterPaaAarsakskodeOgTransaksjonstype(Long gruppeId, List<String> aarsakskoder, String transaksjonstype) {
        SkdEndringsmeldingGruppe gruppe = gruppeRepository.findById(gruppeId);
        List<String> foedselsnumre = skdEndringsmeldingRepository.findFoedselsnummerBy(aarsakskoder, transaksjonstype, gruppe);
        return new LinkedHashSet<>(foedselsnumre);
    }

    public void deleteById(List<Long> ids) {
        if (ids.size() > ORACLE_MAX_IN_SET_ELEMENTS) {
            List<List<Long>> partitionsIds = Lists.partition(ids, 1000);
            for (List<Long> partition : partitionsIds) {
                skdEndringsmeldingRepository.deleteByIdIn(partition);
            }
        } else {
            skdEndringsmeldingRepository.deleteByIdIn(ids);
        }
    }
}
