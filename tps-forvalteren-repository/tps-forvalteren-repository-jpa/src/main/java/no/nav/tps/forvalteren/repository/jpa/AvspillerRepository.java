package no.nav.tps.forvalteren.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;

@Component
public interface AvspillerRepository extends CrudRepository<TpsAvspiller, Long> {

}
