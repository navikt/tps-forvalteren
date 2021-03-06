package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsRequestAdresse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Service
public class RelasjonExtractOpprettKriterier extends ExtractOpprettKriterier {

    private final MidlertidigAdresseMappingService midlertidigAdresseMappingService;
    private final MapperFacade mapperFacade;

    public RelasjonExtractOpprettKriterier(MapperFacade mapperFacade, RandomAdresseService randomAdresseService, HentDatoFraIdentService hentDatoFraIdentService,
            LandkodeEncoder landkodeEncoder, DummyAdresseService dummyAdresseService, MidlertidigAdresseMappingService midlertidigAdresseMappingService) {
        super(mapperFacade, randomAdresseService, hentDatoFraIdentService, landkodeEncoder, dummyAdresseService, midlertidigAdresseMappingService);
        this.midlertidigAdresseMappingService = midlertidigAdresseMappingService;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public List<Person> addExtendedKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req,
            Person hovedPerson,
            List<Person> partnere, List<Person> barn, List<Person> foreldre) {

        List<Adresse> adresser = isNull(req.getBoadresse()) || !req.getBoadresse().isValidAdresse() ?
                getAdresser(1 + partnere.size() + foreldre.size(), req.getAdresseNrInfo()) :
                getAdresse(1 + partnere.size() + foreldre.size(), req.getBoadresse());

        mapPartner(req, hovedPerson, partnere, adresser);
        mapBarn(req, hovedPerson, partnere, barn);
        mapForeldre(req, hovedPerson, foreldre, adresser.subList((1 + partnere.size()) % adresser.size(), adresser.size()));
        this.midlertidigAdresseMappingService.mapAdresse(req, hovedPerson, partnere, barn, foreldre);

        return Stream.of(singletonList(hovedPerson), partnere, barn, foreldre)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Adresse> getAdresse(int antall, RsRequestAdresse requestAdresse) {

        return IntStream.range(0, antall).boxed()
                .map(idx -> mapperFacade.map(requestAdresse, Adresse.class))
                .collect(Collectors.toList());
    }
}