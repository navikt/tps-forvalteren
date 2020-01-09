package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.BarnType;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.BorHos;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsBarnRelasjonRequest;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsPartnerRelasjonRequest;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.BARN;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FOEDSEL;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.assertj.core.util.Lists.newArrayList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Sivilstand;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;

@Service
@Transactional
@RequiredArgsConstructor
public class RelasjonPersonBestillingService {

    private final PersonRepository personRepository;
    private final RandomAdresseService randomAdresseService;
    private final MapperFacade mapperFacade;
    private final ValidateRelasjonerService validateRelasjonerService;

    public Person makeRelasjon(String hovedperson, RsPersonBestillingRelasjonRequest request) {

        List<String> idents = new ArrayList();
        Stream.of(newArrayList(hovedperson),
                request.getRelasjoner().getPartner().stream()
                        .map(RsPartnerRelasjonRequest::getIdent).collect(toList()),
                request.getRelasjoner().getBarn().stream()
                        .map(RsBarnRelasjonRequest::getIdent).collect(toList())
        ).forEach(idents::addAll);

        List<Person> personerliste = personRepository.findByIdentIn(idents);
        Map<String, Person> personer = personerliste.stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        validateRelasjonerService.isGyldig(hovedperson, request, personer);
        setRelasjonerPaaPersoner(hovedperson, request, personer);
        setAdresserPaaPersoner(hovedperson, request, personer);
        setSivilstandHistorikkPaaPersoner(hovedperson, request.getRelasjoner().getPartner(), personer);

        return personRepository.save(personer.get(hovedperson));
    }

    private void setAdresserPaaPersoner(String hovedperson, RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        request.getRelasjoner().getPartner().forEach(partner -> {

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

            } else if (nonNull(barnet.getBorHos()) && BorHos.DEG == barnet.getBorHos() &&
                    !personer.get(barnet.getIdent()).getBoadresse().equals(personer.get(barnet.getPartnerIdent()).getBoadresse())) {

                kopierAdresse(personer.get(barnet.getPartnerIdent()), personer.get(barnet.getIdent()));
            }
        });
    }

    private void kopierAdresse(Person personAdresseFra, Person personAdresseTil) {

        Adresse adresse = personAdresseFra.getBoadresse().get(personAdresseFra.getBoadresse().size() - 1);
        adresse.setPerson(null);
        Adresse kopiertAdresse = mapperFacade.map(adresse, Gateadresse.class);
        kopiertAdresse.setPerson(personAdresseTil);
        adresse.setPerson(personAdresseFra);
        personAdresseTil.getBoadresse().add(kopiertAdresse);
    }

    private static void setSivilstandHistorikkPaaPersoner(String hovedperson,
            List<RsPartnerRelasjonRequest> partnere,
            Map<String, Person> personer) {

        Lists.reverse(partnere).forEach(partner ->

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

    private void setRelasjonerPaaPersoner(String hovedperson,
            RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        request.getRelasjoner().getPartner().forEach(partner ->
                setPartnerRelasjon(personer.get(hovedperson), personer.get(partner.getIdent())));

        request.getRelasjoner().getBarn().forEach(barnet ->
                setBarnRelasjon(personer.get(hovedperson), personer.get(barnet.getPartnerIdent()),
                        personer.get(barnet.getIdent()), barnet));
    }

    private static void setBarnRelasjon(Person hovedPerson, Person partner, Person barn,
            RsBarnRelasjonRequest request) {

        if (isNull(request.getBarnType()) || BarnType.FELLES == request.getBarnType()) {

            setRelasjonForBarn(hovedPerson, barn, isTrue(request.getErAdoptert()));
            setRelasjonForBarn(partner, barn, isTrue(request.getErAdoptert()));

        } else if (BarnType.MITT == request.getBarnType()) {

            setRelasjonForBarn(hovedPerson, barn, isTrue(request.getErAdoptert()));
            setRelasjonForBarn(partner, barn, true);

        } else if (BarnType.DITT == request.getBarnType()) {

            setRelasjonForBarn(partner, barn, isTrue(request.getErAdoptert()));
            setRelasjonForBarn(hovedPerson, barn, true);
        }
    }

    private static void setRelasjonForBarn(Person forelder, Person barn, boolean isAdopted) {

        if (nonNull(forelder)) {
            forelder.getRelasjoner().add(Relasjon.builder().person(forelder).personRelasjonMed(barn).relasjonTypeNavn((isAdopted ? BARN : FOEDSEL).name()).build());
            barn.getRelasjoner().add(Relasjon.builder().person(barn).personRelasjonMed(forelder).relasjonTypeNavn((isKvinne(forelder) ? MOR : FAR).name()).build());
        }
    }

    private static boolean isKvinne(Person person) {
        return "K".equals(person.getKjonn());
    }

    private static void setPartnerRelasjon(Person person, Person partner) {
        person.getRelasjoner().add(Relasjon.builder().person(person).personRelasjonMed(partner).relasjonTypeNavn(PARTNER.getName()).build());
        partner.getRelasjoner().add(Relasjon.builder().person(partner).personRelasjonMed(person).relasjonTypeNavn(PARTNER.getName()).build());
    }
}
