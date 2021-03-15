package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.BorHos;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsBarnRelasjonRequest;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.BARN;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Sivilstand;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsForeldreRelasjonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsPartnerRelasjonRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;

/**
 * Denne klasse benyttes for å knytte sammen eksisterende personer <br>
 * partner og barn eksisterer alle fra før og nyttes sammen i relasjon ved denne bestilling
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RelasjonEksisterendePersonerBestillingService {

    private final PersonRepository personRepository;
    private final RandomAdresseService randomAdresseService;
    private final MapperFacade mapperFacade;
    private final ValidateRelasjonerService validateRelasjonerService;

    public List<String> makeRelasjon(String hovedperson, RsPersonBestillingRelasjonRequest request) {

        List<String> idents = Stream.of(newArrayList(hovedperson),
                request.getRelasjoner().getPartnere().stream()
                        .map(RsPartnerRelasjonRequest::getIdent).collect(toList()),
                request.getRelasjoner().getBarn().stream()
                        .map(RsBarnRelasjonRequest::getIdent).collect(toList()),
                request.getRelasjoner().getForeldre().stream()
                        .map(RsForeldreRelasjonRequest::getIdent).collect(toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Person> personerliste = personRepository.findByIdentIn(idents);
        Map<String, Person> personer = personerliste.stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        validateRelasjonerService.isGyldig(hovedperson, request, personer);
        setRelasjonerPaaPersoner(hovedperson, request, personer);
        setAdresserPaaPersoner(hovedperson, request, personer);
        setSivilstandHistorikkPaaPersoner(hovedperson, request.getRelasjoner().getPartnere(), personer);

        personRepository.save(personer.get(hovedperson));

        return idents;
    }

    private void setAdresserPaaPersoner(String hovedperson, RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        request.getRelasjoner().getPartnere().forEach(partner -> {

            if ((isNull(partner.getHarFellesAdresse()) || isFalse(partner.getHarFellesAdresse())) &&
                    personer.get(hovedperson).getBoadresse().equals(personer.get(partner.getIdent()).getBoadresse())) {
                personer.put(partner.getIdent(), randomAdresseService.execute(newArrayList(personer.get(partner.getIdent())), null).get(0));

            } else if (isTrue(partner.getHarFellesAdresse()) &&
                    !personer.get(hovedperson).getBoadresse().equals(personer.get(partner.getIdent()).getBoadresse())) {
                kopierAdresse(personer.get(hovedperson), personer.get(partner.getIdent()));
            }
        });

        request.getRelasjoner().getBarn().forEach(barnet -> {

            if ((isNull(barnet.getBorHos()) || BorHos.MEG == barnet.getBorHos() || BorHos.OSS == barnet.getBorHos()) &&
                    !personer.get(barnet.getIdent()).getBoadresse().equals(personer.get(hovedperson).getBoadresse())) {
                kopierAdresse(personer.get(hovedperson), personer.get(barnet.getIdent()));

            } else if (nonNull(barnet.getBorHos()) && BorHos.DEG == barnet.getBorHos() && isNotBlank(barnet.getPartnerIdent())) {

                kopierAdresse(personer.get(barnet.getPartnerIdent()), personer.get(barnet.getIdent()));
            }
        });
    }

    private void kopierAdresse(Person personAdresseFra, Person personAdresseTil) {

        Adresse adresse = personAdresseFra.getBoadresse().stream().findFirst().orElse(null);
        if (nonNull(adresse) && personAdresseTil.getBoadresse().stream().noneMatch(adr -> adr.equals(adresse))) {
            adresse.setPerson(null);
            Adresse kopiertAdresse = mapperFacade.map(adresse, Gateadresse.class);
            kopiertAdresse.setPerson(personAdresseTil);
            kopiertAdresse.setId(null);
            adresse.setPerson(personAdresseFra);
            personAdresseTil.getBoadresse().add(kopiertAdresse);
        }
        Postadresse postadresse = personAdresseFra.getPostadresse().stream().findFirst().orElse(null);
        if (nonNull(postadresse) && personAdresseTil.getPostadresse().stream().noneMatch(adr -> adr.equals(postadresse))) {
            postadresse.setPerson(null);
            Postadresse kopiertAdresse = mapperFacade.map(postadresse, Postadresse.class);
            kopiertAdresse.setPerson(personAdresseTil);
            kopiertAdresse.setId(null);
            postadresse.setPerson(personAdresseFra);
            personAdresseTil.getPostadresse().add(kopiertAdresse);
        }
    }

    private static void setSivilstandHistorikkPaaPersoner(String hovedperson,
            List<RsPartnerRelasjonRequest> partnere,
            Map<String, Person> personer) {

        partnere.forEach(partner ->

                partner.getSivilstander().forEach(sivilstand ->

                        setSivilstandHistory(personer.get(hovedperson), personer.get(partner.getIdent()),
                                sivilstand.getSivilstand(), sivilstand.getSivilstandRegdato())
                ));
    }

    private static void setSivilstandHistory(Person person, Person partner, String sivilstand, LocalDateTime regdato) {

        person.getSivilstander().add(Sivilstand.builder()
                .person(person)
                .personRelasjonMed(partner)
                .sivilstand(sivilstand)
                .sivilstandRegdato(regdato)
                .build());

        partner.getSivilstander()
                .add(Sivilstand.builder()
                        .person(partner)
                        .personRelasjonMed(person)
                        .sivilstand(sivilstand)
                        .sivilstandRegdato(regdato)
                        .build());
    }

    private static void setRelasjonerPaaPersoner(String hovedperson,
            RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        request.getRelasjoner().getPartnere().forEach(partner ->
                setPartnerRelasjon(personer.get(hovedperson), personer.get(partner.getIdent())));

        request.getRelasjoner().getBarn().forEach(barnet ->
                setBarnRelasjon(personer.get(hovedperson), personer.get(barnet.getPartnerIdent()),
                        personer.get(barnet.getIdent())));

        request.getRelasjoner().getForeldre().forEach(foreldre ->
                setForeldreRelasjon(personer.get(hovedperson), personer.get(foreldre.getIdent()), foreldre));
    }

    private static void setForeldreRelasjon(Person hovedperson, Person foreldre, RsForeldreRelasjonRequest request) {

        hovedperson.getRelasjoner().add(Relasjon.builder()
                .person(hovedperson)
                .personRelasjonMed(foreldre)
                .relasjonTypeNavn(nullcheckSetDefaultValue(request.getForeldreType(),
                        foreldre.isKvinne() ? MOR : FAR).name())
                .build());
        foreldre.getRelasjoner().add(Relasjon.builder()
                .person(foreldre)
                .personRelasjonMed(hovedperson)
                .relasjonTypeNavn(BARN.getName())
                .build());
    }

    private static void setBarnRelasjon(Person hovedPerson, Person partner, Person barn) {

        setRelasjonForBarn(hovedPerson, barn);
        setRelasjonForBarn(partner, barn);
    }

    private static void setRelasjonForBarn(Person forelder, Person barn) {

        if (nonNull(forelder)) {
            forelder.getRelasjoner().add(Relasjon.builder().person(forelder).personRelasjonMed(barn).relasjonTypeNavn(BARN.name()).build());
            barn.getRelasjoner().add(Relasjon.builder()
                    .person(barn)
                    .personRelasjonMed(forelder)
                    .relasjonTypeNavn(forelder.isKvinne() ? MOR.name() : FAR.name())
                    .build());
        }
    }

    private static void setPartnerRelasjon(Person person, Person partner) {
        person.getRelasjoner().add(Relasjon.builder().person(person).personRelasjonMed(partner).relasjonTypeNavn(PARTNER.getName()).build());
        partner.getRelasjoner().add(Relasjon.builder().person(partner).personRelasjonMed(person).relasjonTypeNavn(PARTNER.getName()).build());
    }
}
