package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Service
public class RelasjonExtractOpprettKriterier extends ExtractOpprettKriterier {

    private TillegsadresseMappingService tillegsadresseMappingService;

    public RelasjonExtractOpprettKriterier(MapperFacade mapperFacade, RandomAdresseService randomAdresseService, HentDatoFraIdentService hentDatoFraIdentService,
            LandkodeEncoder landkodeEncoder, DummyAdresseService dummyAdresseService, TillegsadresseMappingService tillegsadresseMappingService) {
        super(mapperFacade, randomAdresseService, hentDatoFraIdentService, landkodeEncoder, dummyAdresseService, tillegsadresseMappingService);
        this.tillegsadresseMappingService = tillegsadresseMappingService;
    }

    @Override
    public List<Person> addExtendedKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req,
            List<Person> hovedPersoner,
            List<Person> partnere, List<Person> barn) {

        List<Adresse> adresser = isNull(req.getBoadresse()) || !req.getBoadresse().isValidAdresse() ?
                getAdresser(hovedPersoner.size() + partnere.size(), req.getAdresseNrInfo()) : new ArrayList<>();

        mapPartner(req, hovedPersoner, partnere, adresser);
        mapBarn(req, hovedPersoner, partnere, barn);
        this.tillegsadresseMappingService.mapAdresse(req, hovedPersoner, partnere, barn);

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }
}