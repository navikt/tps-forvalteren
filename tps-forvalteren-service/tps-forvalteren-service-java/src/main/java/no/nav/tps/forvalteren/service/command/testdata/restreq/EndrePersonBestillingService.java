package no.nav.tps.forvalteren.service.command.testdata.restreq;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsOppdaterPersonResponse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.OpprettPersonUtil.extractMainPerson;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class EndrePersonBestillingService {

    private final PersonRepository personRepository;
    private final RandomAdresseService randomAdresseService;
    private final RelasjonNyePersonerBestillingService relasjonNyePersonerBestillingService;
    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final MapperFacade mapperFacade;
    private final MessageProvider messageProvider;
    private final OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;

    public RsOppdaterPersonResponse execute(String ident, RsPersonBestillingKriteriumRequest request) {

        Person person = personRepository.findByIdent(ident);

        if (isNull(person)) {
            throw new TpsfFunctionalException(format("Person med ident %s ble ikke funnet", ident));
        }

        validateUpdateRequest(request, person);

        if (isNotBlank(request.getIdenttype())) {
            person = swapIdent(request, person);
        }
        mapperFacade.map(request, person);

        updateAdresse(request, person);
        updateStatsborgerskap(request, person);

        relasjonNyePersonerBestillingService.makeRelasjoner(request, person)
                .stream().map(Person::getIdent).collect(Collectors.toList());

        personRepository.save(person);

        List<RsOppdaterPersonResponse.IdentTuple> identer = person.getRelasjoner().stream()
                .map(Relasjon::getPersonRelasjonMed)
                .map(pers -> RsOppdaterPersonResponse.IdentTuple.builder()
                        .ident(pers.getIdent())
                        .lagtTil(pers.isNyPerson())
                        .build())
                .collect(Collectors.toList());

        identer.add(0, RsOppdaterPersonResponse.IdentTuple.builder().ident(person.getIdent()).build());

        return RsOppdaterPersonResponse.builder().identTupler(identer).build();
    }

    private Person swapIdent(RsPersonBestillingKriteriumRequest request, Person person) {

        RsPersonKriteriumRequest personKriterier = extractMainPerson(request);
        try {
            Person nyPerson = opprettPersonerOgSjekkMiljoeService.createNyeIdenter(personKriterier).get(0);
            nyPerson.getIdentHistorikk().add(IdentHistorikk.builder()
                    .person(nyPerson)
                    .aliasPerson(person)
                    .historicIdentOrder(1)
                    .regdato(LocalDateTime.now())
                    .build());

            return nyPerson;
        } catch (RuntimeException e) {
            throw new TpsfTechnicalException("Feilet å opprette person med identtype " + request.getIdenttype(), e);
        }
    }

    private void validateUpdateRequest(RsPersonBestillingKriteriumRequest request, Person person) {

        validateStatsborgerskap(request, person);
        validateInnvandretUtvandret(request, person);
    }

    private void validateStatsborgerskap(RsPersonBestillingKriteriumRequest request, Person person) {

        if (nonNull(request.getStatsborgerskap()) &&
                person.getStatsborgerskap().stream().map(Statsborgerskap::getStatsborgerskap)
                        .anyMatch(stsbs -> stsbs.equals(request.getStatsborgerskap()))) {
            throw new TpsfFunctionalException(messageProvider.get("endre.person.statsborgerskap.validation.eksisterer.allerede",
                    request.getStatsborgerskap()));
        }
    }

    private void validateInnvandretUtvandret(RsPersonBestillingKriteriumRequest request, Person person) {

        if (nonNull(request.getUtvandretTilLand()) && !FNR.name().equals(person.getIdenttype())) {
            throw new TpsfFunctionalException(messageProvider.get("endre.person.innutvandring.validation.identtype"));
        }

        if (isNotBlank(request.getInnvandretFraLand()) && INNVANDRET == person.getInnvandretUtvandret().get(0).getInnutvandret() ||
                isNotBlank(request.getUtvandretTilLand()) && UTVANDRET == person.getInnvandretUtvandret().get(0).getInnutvandret()) {
            throw new TpsfFunctionalException(messageProvider.get("endre.person.innutvandring.validation.samme.aksjon"));
        }

        if (nonNull(request.getInnvandretFraLandFlyttedato()) &&
                request.getInnvandretFraLandFlyttedato().isBefore(person.getInnvandretUtvandret().get(0).getFlyttedato()) ||
                nonNull(request.getUtvandretTilLandFlyttedato()) &&
                        request.getUtvandretTilLandFlyttedato().isBefore(person.getInnvandretUtvandret().get(0).getFlyttedato())) {

            throw new TpsfFunctionalException(messageProvider.get("endre.person.innutvandring.validation.flyttedato"));
        }
    }

    private void updateAdresse(RsPersonBestillingKriteriumRequest request, Person person) {

        person.setGtVerdi(null); // Triggers reload of TKNR

        if (isNull(request.getAdresseNrInfo()) && isNull(request.getBoadresse())) {
            return;
        }
        if (nonNull(request.getAdresseNrInfo())) {
            Adresse randomAdresse = randomAdresseService.hentRandomAdresse(1, request.getAdresseNrInfo()).get(0);
            randomAdresse.setFlyttedato(nonNull(request.getBoadresse()) ? request.getBoadresse().getFlyttedato() : null);
            setAdressePaaPerson(person, randomAdresse);
        }

        Adresse boAdresse = person.getBoadresse().stream().reduce((adresse1, adresse2) -> adresse2).orElse(null);

        if (nonNull(boAdresse) && (isNull(request.getBoadresse()) || isNull(request.getBoadresse().getFlyttedato()))) {

            boAdresse.setFlyttedato(now().minusYears(1));
            setAdresseGyldigTilDato(request, boAdresse);
        }

        if (!person.isForsvunnet()) {

            Adresse adresse = person.getBoadresse().isEmpty()
                    ? randomAdresseService.hentRandomAdresse(1, null).get(0)
                    : person.getBoadresse().get(0);
            adresse.setFlyttedato(hentDatoFraIdentService.extract(person.getIdent()));
            setAdresseGyldigTilDato(request, adresse);
            if (person.getBoadresse().isEmpty()) {
                setAdressePaaPerson(person, adresse);
            }
        }
    }

    private void setAdresseGyldigTilDato(RsPersonBestillingKriteriumRequest request, Adresse adresse) {
        if (nonNull(request.getUtvandretTilLandFlyttedato())) {
            adresse.setGyldigTilDato(request.getUtvandretTilLandFlyttedato().minusDays(1));
        } else {
            adresse.setGyldigTilDato(nonNull(request.getBoadresse()) ? request.getBoadresse().getGyldigTilDato() : null);
        }
    }

    private void setAdressePaaPerson(Person person, Adresse adresse) {
        adresse.setPerson(person);
        person.getBoadresse().add(adresse);
    }

    private static void updateStatsborgerskap(RsPersonBestillingKriteriumRequest request, Person person) {

        if (isNotBlank(request.getStatsborgerskap()) && isNull(request.getStatsborgerskapRegdato())) {

            person.getStatsborgerskap().stream()
                    .reduce(((statsborgerskap, statsborgerskap2) -> statsborgerskap2)).get()
                    .setStatsborgerskapRegdato(now());
        }
    }
}
