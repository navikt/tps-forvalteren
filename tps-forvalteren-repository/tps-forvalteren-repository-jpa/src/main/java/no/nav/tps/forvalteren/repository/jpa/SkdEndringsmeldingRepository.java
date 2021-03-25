package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;

public interface SkdEndringsmeldingRepository extends PagingAndSortingRepository<SkdEndringsmelding, Long> {

    List<SkdEndringsmelding> findByIdIn(List<Long> ids);

    SkdEndringsmelding save(SkdEndringsmelding skdEndringsmelding);

    void deleteByIdIn(List<Long> ids);

    Page<SkdEndringsmelding> findAllByGruppeIdOrderByIdAsc(Long gruppeId, Pageable pageable);

    @Query(value = "SELECT count(*) FROM T_SKD_ENDRINGSMELDING WHERE SKD_ENDRINGSMELDING_GRUPPE_ID=:gruppeId", nativeQuery = true)
    int countMeldingerInGruppe(@Param("gruppeId") Long gruppeId);

    void deleteAll();

    @Query("SELECT s.id FROM SkdEndringsmelding s where s.gruppe=:gruppe")
    List<Long> findAllIdsBy(@Param("gruppe") SkdEndringsmeldingGruppe gruppe);

    @Query("SELECT s.id FROM SkdEndringsmelding s where s.gruppe=:gruppe AND s.foedselsnummer IN :foedselsnummer")
    List<Long> findAllIdsBy(
            @Param("gruppe") SkdEndringsmeldingGruppe gruppe,
            @Param("foedselsnummer") List<String> foedselsnummer);

    @Query("SELECT foedselsnummer FROM SkdEndringsmelding "
            + "WHERE aarsakskode in :aarsakskoder AND transaksjonstype=:transaksjonstype AND gruppe=:gruppe")
    List<String> findFoedselsnummerBy(
            @Param("aarsakskoder") List<String> aarsakskoder,
            @Param("transaksjonstype") String transaksjonstype,
            @Param("gruppe") SkdEndringsmeldingGruppe gruppe);

    @Query(value = "SELECT COUNT(*) ANTALL, SKD_ENDRINGSMELDING_GRUPPE_ID GRUPPE "
            + "FROM T_SKD_ENDRINGSMELDING "
            + "GROUP BY SKD_ENDRINGSMELDING_GRUPPE_ID",
            nativeQuery = true)
    List<AntallForekomster> countAllBySkdEndringsmeldingGruppeId();

    interface AntallForekomster {

        Long getAntall();

        Long getGruppe();
    }
}
