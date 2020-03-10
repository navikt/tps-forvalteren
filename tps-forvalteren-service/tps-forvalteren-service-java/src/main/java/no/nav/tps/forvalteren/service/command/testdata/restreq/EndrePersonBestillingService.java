package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.NIL;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
@Transactional
@RequiredArgsConstructor
public class EndrePersonBestillingService {

    private final PersonRepository personRepository;
    private final RandomAdresseService randomAdresseService;
    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final RelasjonNyePersonerBestillingService relasjonNyePersonerBestillingService;
    private final MapperFacade mapperFacade;
    private final MessageProvider messageProvider;

    public List<Person> execute(String ident, RsPersonBestillingKriteriumRequest request) {

        Person person = personRepository.findByIdent(ident);

        if (isNull(person)) {
            throw new TpsfFunctionalException(format("Person med ident %s ble ikke funnet", ident));
        }
        updateAdresse(request, person);
        updateStatsborgerskap(request, person);
        updateInnvandringUtvandring(request, person);
        List<Person> personer = relasjonNyePersonerBestillingService.makeRelasjoner(request, person);

        personRepository.save(personer.get(0));
        return personer;
    }

    private void updateInnvandringUtvandring(RsPersonBestillingKriteriumRequest request, Person person) {

        Set<InnvandretUtvandret> innvandretUtvandretSet = new TreeSet(Comparator.comparing(InnvandretUtvandret::getFlyttedato));
        if (nonNull(request.getUtvandretTilLandFlyttedato())) {
            if (!FNR.name().equals(person.getIdenttype())) {
                throw new TpsfFunctionalException(messageProvider.get("endre.person.innutvandring.validation.identtype"));
            }
            innvandretUtvandretSet.add(
                    buildInnutvandret(UTVANDRET, request.getUtvandretTilLand(), request.getUtvandretTilLandFlyttedato(), person));
        }
        if (nonNull(request.getInnvandretFraLandFlyttedato())) {
            innvandretUtvandretSet.add(
                    buildInnutvandret(INNVANDRET, request.getInnvandretFraLand(), request.getInnvandretFraLandFlyttedato(), person));
        }

        AtomicReference<InnvandretUtvandret> forrigeInnUtvandret = new AtomicReference(
                person.getInnvandretUtvandret().isEmpty() ?
                        buildInnutvandret(NIL, null, hentDatoFraIdentService.extract(person.getIdent()), null)
                        : person.getInnvandretUtvandret().get(0));

        innvandretUtvandretSet.forEach(innvandretUtvandret -> {
            if (innvandretUtvandret.getFlyttedato().isBefore(forrigeInnUtvandret.get().getFlyttedato())) {
                throw new TpsfFunctionalException(messageProvider.get("endre.person.innutvandring.validation.flyttedato"));
            }
            if (innvandretUtvandret.getInnutvandret() == forrigeInnUtvandret.get().getInnutvandret()) {
                throw new TpsfFunctionalException(messageProvider.get("endre.person.innutvandring.validation.samme.aksjon"));
            }
            person.getInnvandretUtvandret().add(innvandretUtvandret);
            forrigeInnUtvandret.set(innvandretUtvandret);
        });
    }

    private static InnvandretUtvandret buildInnutvandret(InnUtvandret innUtvandret, String landkode, LocalDateTime flyttedato, Person person) {
        return InnvandretUtvandret.builder()
                .innutvandret(innUtvandret)
                .landkode(landkode)
                .flyttedato(flyttedato)
                .person(person)
                .build();
    }

    private void updateStatsborgerskap(RsPersonBestillingKriteriumRequest request, Person person) {

        if (isNotBlank(request.getStatsborgerskap())) {

            person.getStatsborgerskap().add(Statsborgerskap.builder()
                    .statsborgerskap(request.getStatsborgerskap())
                    .statsborgerskapRegdato(nullcheckSetDefaultValue(request.getStatsborgerskapRegdato(), now()))
                    .person(person)
                    .build());
        }
    }

    private void updateAdresse(RsPersonBestillingKriteriumRequest request, Person person) {

        if (nonNull(request.getAdresseNrInfo()) || nonNull(request.getBoadresse())) {

            Adresse adresse = (nonNull(request.getAdresseNrInfo()) ?
                    randomAdresseService.hentRandomAdresse(1, request.getAdresseNrInfo()).get(0) : mapperFacade.map(request.getBoadresse(), Adresse.class)).toUppercase();

            AtomicBoolean found = new AtomicBoolean(false);
            person.getBoadresse().forEach(boadresse -> {
                if (adresse.equals(boadresse)) {
                    found.set(true);
                }
            });
            if (!found.get()) {
                adresse.setPerson(person);
                if (isNull(adresse.getFlyttedato())) {
                    adresse.setFlyttedato(now().minusYears(1));
                }
                person.getBoadresse().add(adresse);
                person.setGtVerdi(null); // Triggers reload of TKNR
            }
        }

        if (!request.getPostadresse().isEmpty()) {

            for (RsPostadresse postadresseRequest : request.getPostadresse()) {

                boolean found = false;
                Postadresse postadresse = mapperFacade.map(postadresseRequest, Postadresse.class).toUppercase();

                for (Postadresse postadressePerson : person.getPostadresse()) {
                    if (postadresse.equals(postadressePerson)) {
                        found = true;
                    }
                }

                if (!found) {
                    postadresse.setPerson(person);
                    person.getPostadresse().add(postadresse);
                }
            }
        }
    }
}
