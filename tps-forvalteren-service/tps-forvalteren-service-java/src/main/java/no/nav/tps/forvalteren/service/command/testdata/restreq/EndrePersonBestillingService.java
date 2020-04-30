package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.dolly.RsOppdaterPersonResponse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
@RequiredArgsConstructor
public class EndrePersonBestillingService {

    private final PersonRepository personRepository;
    private final RandomAdresseService randomAdresseService;
    private final RelasjonNyePersonerBestillingService relasjonNyePersonerBestillingService;
    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final MapperFacade mapperFacade;
    private final MessageProvider messageProvider;

    public RsOppdaterPersonResponse execute(String ident, RsPersonBestillingKriteriumRequest request) {

        Person person = personRepository.findByIdent(ident);

        if (isNull(person)) {
            throw new TpsfFunctionalException(format("Person med ident %s ble ikke funnet", ident));
        }

        validateUpdateRequest(request, person);

        mapperFacade.map(request, person);

        updateAdresse(request, person);
        updateStatsborgerskap(request, person);

        List<String> nyeIdenter = relasjonNyePersonerBestillingService.makeRelasjoner(request, person)
                .stream().map(Person::getIdent).collect(Collectors.toList());

        personRepository.save(person);

        List<RsOppdaterPersonResponse.IdentTuple> identer = person.getRelasjoner().stream()
                .map(Relasjon::getPersonRelasjonMed)
                .map(Person::getIdent)
                .map(ident1 -> RsOppdaterPersonResponse.IdentTuple.builder()
                        .ident(ident1)
                        .lagtTil(nyeIdenter.contains(ident1))
                        .build())
                .collect(Collectors.toList());

        identer.add(0, RsOppdaterPersonResponse.IdentTuple.builder().ident(person.getIdent()).build());

        return RsOppdaterPersonResponse.builder().identTupler(identer).build();
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

    private static void updateStatsborgerskap(RsPersonBestillingKriteriumRequest request, Person person) {

        if (isNotBlank(request.getStatsborgerskap()) && isNull(request.getStatsborgerskapRegdato())) {

            person.getStatsborgerskap().stream()
                    .reduce(((statsborgerskap, statsborgerskap2) -> statsborgerskap2)).get()
                    .setStatsborgerskapRegdato(now());
        }
    }

    private void updateAdresse(RsPersonBestillingKriteriumRequest request, Person person) {

        if (nonNull(request.getAdresseNrInfo()) || nonNull(request.getBoadresse())) {

            if (nonNull(request.getAdresseNrInfo())) {
                Adresse adresse = randomAdresseService.hentRandomAdresse(1, request.getAdresseNrInfo()).get(0);
                adresse.setFlyttedato(nonNull(request.getBoadresse()) ? request.getBoadresse().getFlyttedato() : null);
                setAdressePaaPerson(person, adresse);
            }

            Adresse adresse = person.getBoadresse().stream().reduce((adresse1, adresse2) -> adresse2).orElse(null);

            if (nonNull(adresse) && (isNull(request.getBoadresse()) || isNull(request.getBoadresse().getFlyttedato()))) {

                adresse.setFlyttedato(now().minusYears(1));
            }
        }

        if (person.getBoadresse().isEmpty() && !person.isForsvunnet()) {
            Adresse adresse = randomAdresseService.hentRandomAdresse(1, null).get(0);
            adresse.setFlyttedato(hentDatoFraIdentService.extract(person.getIdent()));
            setAdressePaaPerson(person, adresse);
        }

        person.setGtVerdi(null); // Triggers reload of TKNR
    }

    private void setAdressePaaPerson(Person person, Adresse adresse) {
        adresse.setPerson(person);
        person.getBoadresse().add(adresse);
    }
}
