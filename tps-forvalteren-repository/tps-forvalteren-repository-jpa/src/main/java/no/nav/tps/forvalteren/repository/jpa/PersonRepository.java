package no.nav.tps.forvalteren.repository.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

    List<Person> findAllByOrderByIdAsc();

    @Modifying
    int deleteByIdIn(Collection<Long> ids);

    List<Person> findByIdentIn(Collection<String> identListe);

    Set<Person> findByIdIn(Set<Long> ids);

    Person findByIdent(String ident);

    List<Person> findAll();

    void deleteByGruppeId(Long gruppeId);

    @Query("from Person p where p.gruppe is null "
            + "and (:partialIdent is null or :partialIdent is not null and p.ident like %:partialIdent%)"
            + "and (:partialNavn1 is null or :partialNavn1 is not null and (upper(p.etternavn) like %:partialNavn1% or upper(p.fornavn) like %:partialNavn1%))"
            + "and (:partialNavn2 is null or :partialNavn2 is not null and (upper(p.etternavn) like %:partialNavn2% or upper(p.fornavn) like %:partialNavn2%))")
    List<Person> findByWildcardIdent(@Param("partialIdent") String partialIdent,
            @Param("partialNavn1") String partialNavn1,
            @Param("partialNavn2") String partialNavn2,
            Pageable pageable);

    void deleteAll();
}
