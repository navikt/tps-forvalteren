package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
@RequiredArgsConstructor
public class SkdEndringsmeldingService {

    protected static final int ANTALL_MELDINGER_PER_PAGE = 10;
    private static final int PARTITION_SIZE = 1000;

    private final SkdEndringsmeldingRepository skdEndringsmeldingRepository;
    private final SkdEndringsmeldingGruppeRepository gruppeRepository;
    private final ObjectMapper objectMapper;

    public int countMeldingerInGruppe(Long gruppeId) {
        return skdEndringsmeldingRepository.countMeldingerInGruppe(gruppeId);
    }

    public int getAntallSiderIGruppe(int antallMeldingerIGruppe) {
        return (int) Math.ceil(antallMeldingerIGruppe / (double) ANTALL_MELDINGER_PER_PAGE);
    }

    public List<SkdEndringsmelding> findSkdEndringsmeldingerOnPage(Long gruppeId, int pageNumber) {
        return skdEndringsmeldingRepository.findAllByGruppeIdOrderByIdAsc(gruppeId, PageRequest.of(pageNumber, ANTALL_MELDINGER_PER_PAGE)).getContent();
    }

    public List<RsMeldingstype> convertSkdEndringsmeldingerToRsMeldingstyper(List<SkdEndringsmelding> skdEndringsmeldinger) throws IOException {
        List<RsMeldingstype> rsMeldingstypeMeldinger = new ArrayList<>(skdEndringsmeldinger.size());

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
            List<List<Long>> partitionsIds = Lists.partition(ids, PARTITION_SIZE);
            for (List<Long> partition : partitionsIds) {
                skdEndringsmeldingRepository.deleteByIdIn(partition);
            }
        } else {
            skdEndringsmeldingRepository.deleteByIdIn(ids);
        }
    }

    public RsMeldingstype findEndringsmeldingById(Long id) throws JsonProcessingException {
        Optional<SkdEndringsmelding> skdEndringsmelding = skdEndringsmeldingRepository.findById(id);
        return skdEndringsmelding.isPresent() ?
                objectMapper.readValue(skdEndringsmelding.get().getEndringsmelding(), RsMeldingstype.class) : null;
    }

    public List<RsMeldingstype> findAllEndringsmeldingerByIds(List<Long> ids) throws JsonProcessingException {
        List<SkdEndringsmelding> skdEndringsmeldinger = skdEndringsmeldingRepository.findByIdIn(ids);
        List<RsMeldingstype> rsMeldingstyper = new ArrayList<>(skdEndringsmeldinger.size());
        for (SkdEndringsmelding skdEndringsmelding : skdEndringsmeldinger) {
            RsMeldingstype rsMeldingstype = objectMapper.readValue(skdEndringsmelding.getEndringsmelding(), RsMeldingstype.class);
            if (rsMeldingstype.getId() == null) {
                rsMeldingstype.setId(skdEndringsmelding.getId());
            }
            rsMeldingstyper.add(rsMeldingstype);
        }
        return rsMeldingstyper;
    }

    public List<Long> findAllMeldingIdsInGruppe(Long gruppeId) {
        SkdEndringsmeldingGruppe skdEndringsmeldingGruppe = gruppeRepository.findById(gruppeId);
        return skdEndringsmeldingRepository.findAllIdsBy(skdEndringsmeldingGruppe);
    }

    public List<Long> finnAlleMeldingIderMedFoedselsnummer(Long gruppeId, List<String> identer) {
        SkdEndringsmeldingGruppe skdEndringsmeldingGruppe = gruppeRepository.findById(gruppeId);
        List<Long> meldingIder = new ArrayList<>(identer.size());
        for (List<String> partisjonerteIdenter : Lists.partition(identer, PARTITION_SIZE)) {
            meldingIder.addAll(skdEndringsmeldingRepository.findAllIdsBy(skdEndringsmeldingGruppe, partisjonerteIdenter));
        }
        return meldingIder;
    }
}
