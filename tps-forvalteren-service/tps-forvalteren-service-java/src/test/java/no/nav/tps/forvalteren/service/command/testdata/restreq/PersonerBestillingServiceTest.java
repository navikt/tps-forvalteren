package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.GIFT;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.SKILT;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FOEDSEL;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsBarnRequest;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsSivilstandRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.service.RelasjonType;

@RunWith(MockitoJUnitRunner.class)
public class PersonerBestillingServiceTest {

    private static final LocalDateTime DATO_1 = LocalDateTime.of(2000, 1, 1, 0, 0);
    private static final LocalDateTime DATO_2 = LocalDateTime.of(2005, 1, 1, 0, 0);
    private static final LocalDateTime DATO_3 = LocalDateTime.of(2010, 1, 1, 0, 0);
    private static final LocalDateTime DATO_4 = LocalDateTime.of(2015, 1, 1, 0, 0);

    @InjectMocks
    PersonerBestillingService personerBestillingService;

    private RsPersonBestillingKriteriumRequest personBestillingKriteriumRequest;

    @Before
    public void setup() {

        personBestillingKriteriumRequest = new RsPersonBestillingKriteriumRequest();
        personBestillingKriteriumRequest.getRelasjoner().getPartnere().addAll(
                asList(new RsPartnerRequest(), new RsPartnerRequest(), new RsPartnerRequest(), new RsPartnerRequest()));
        personBestillingKriteriumRequest.getRelasjoner().getBarn().addAll(
                asList(new RsBarnRequest(), new RsBarnRequest(), new RsBarnRequest(), new RsBarnRequest(), new RsBarnRequest(), new RsBarnRequest()));
    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgMannNullBarn() {

        Person mann1 = Person.builder().kjonn("M").isNyPerson(true).build();
        Person mann2 = Person.builder().kjonn("M").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(mann1), asList(mann2), emptyList(), personBestillingKriteriumRequest);

        assertThat(mann1.getRelasjoner().size(), is(1));
        assertThat(mann2.getRelasjoner().size(), is(1));

        assertThat(mann1.getRelasjoner().get(0).getPersonRelasjonMed(), is(mann2));
        assertThat(mann2.getRelasjoner().get(0).getPersonRelasjonMed(), is(mann1));

        assertThat(mann1.getRelasjoner().get(0).getRelasjonTypeNavn(), is(PARTNER.getName()));
        assertThat(mann2.getRelasjoner().get(0).getRelasjonTypeNavn(), is(PARTNER.getName()));
    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgBarnUtenKvinneGirFoedsel() {

        Person mann = Person.builder().kjonn("M").isNyPerson(true).build();
        Person barn = Person.builder().kjonn("K").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(mann), new ArrayList<>(), asList(barn), personBestillingKriteriumRequest);

        assertThat(mann.getRelasjoner().size(), is(1));
        assertThat(barn.getRelasjoner().size(), is(1));

        assertRelasjon(mann, barn, FOEDSEL);
        assertRelasjon(barn, mann, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_KvinneOgBarnUtenMannGirFodsel() {

        Person kvinne = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn = Person.builder().kjonn("K").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(kvinne), new ArrayList<>(), asList(barn), personBestillingKriteriumRequest);

        assertThat(kvinne.getRelasjoner().size(), is(1));
        assertThat(barn.getRelasjoner().size(), is(1));

        assertRelasjon(kvinne, barn, FOEDSEL);
        assertRelasjon(barn, kvinne, MOR);
    }

    @Test
    public void setRelasjonerPaaPersoner_MannKvinneOgBarnGirFoedselRelasjonPaaFarOgMorOgMorFarRelasjonPaaBarn() {

        Person mann = Person.builder().kjonn("M").isNyPerson(true).build();
        Person kvinne = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn = Person.builder().kjonn("K").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(mann), asList(kvinne), asList(barn), personBestillingKriteriumRequest);

        assertThat(mann.getRelasjoner().size(), is(2));
        assertThat(kvinne.getRelasjoner().size(), is(2));
        assertThat(barn.getRelasjoner().size(), is(2));

        assertRelasjon(mann, barn, FOEDSEL);
        assertRelasjon(mann, kvinne, PARTNER);

        assertRelasjon(kvinne, barn, FOEDSEL);
        assertRelasjon(kvinne, mann, PARTNER);

        assertRelasjon(barn, kvinne, MOR);
        assertRelasjon(barn, mann, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_MannOgKvinneOg2Barn() {

        Person mann = Person.builder().kjonn("M").isNyPerson(true).build();
        Person kvinne = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn1 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn2 = Person.builder().kjonn("M").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(mann), asList(kvinne), asList(barn1, barn2), personBestillingKriteriumRequest);

        assertThat(mann.getRelasjoner().size(), is(3));
        assertThat(kvinne.getRelasjoner().size(), is(3));
        assertThat(barn1.getRelasjoner().size(), is(2));
        assertThat(barn2.getRelasjoner().size(), is(2));

        assertRelasjon(mann, barn1, FOEDSEL);
        assertRelasjon(mann, barn2, FOEDSEL);
        assertRelasjon(mann, kvinne, PARTNER);

        assertRelasjon(kvinne, barn1, FOEDSEL);
        assertRelasjon(kvinne, barn2, FOEDSEL);
        assertRelasjon(kvinne, mann, PARTNER);

        assertRelasjon(barn1, kvinne, MOR);
        assertRelasjon(barn1, mann, FAR);

        assertRelasjon(barn2, kvinne, MOR);
        assertRelasjon(barn2, mann, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_MannMannOgBarnGirFarRelasjonPaaBeggeFedre() {
        Person mann1 = Person.builder().kjonn("M").isNyPerson(true).build();;
        Person mann2 = Person.builder().kjonn("M").isNyPerson(true).build();
        Person barn = Person.builder().kjonn("K").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(mann1), asList(mann2), asList(barn), personBestillingKriteriumRequest);

        assertThat(mann1.getRelasjoner().size(), is(2));
        assertThat(mann2.getRelasjoner().size(), is(2));

        assertRelasjon(mann1, barn, FOEDSEL);
        assertRelasjon(mann1, mann2, PARTNER);

        assertRelasjon(mann2, barn, FOEDSEL);
        assertRelasjon(mann2, mann1, PARTNER);

        assertRelasjon(barn, mann1, FAR);
        assertRelasjon(barn, mann2, FAR);
    }

    @Test
    public void setRelasjonerPaaPersoner_PersonerMedFlerePartnereOgMedBarn() {

        Person mann = Person.builder().kjonn("M").isNyPerson(true).build();
        Person kvinne = Person.builder().kjonn("K").isNyPerson(true).build();

        Person partner1 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person partner2 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person partner3 = Person.builder().kjonn("M").isNyPerson(true).build();
        Person partner4 = Person.builder().kjonn("M").isNyPerson(true).build();

        Person barn1 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn2 = Person.builder().kjonn("M").isNyPerson(true).build();
        Person barn3 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn4 = Person.builder().kjonn("M").isNyPerson(true).build();
        Person barn5 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn6 = Person.builder().kjonn("M").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(
                asList(mann, kvinne),
                asList(partner1, partner2, partner3, partner4),
                asList(barn1, barn2, barn3, barn4, barn5, barn6),
                personBestillingKriteriumRequest);

        assertThat(mann.getRelasjoner().size(), is(5));
        assertThat(kvinne.getRelasjoner().size(), is(5));

        assertRelasjon(mann, partner1, PARTNER);
        assertRelasjon(mann, partner2, PARTNER);
        assertRelasjon(mann, barn1, FOEDSEL);
        assertRelasjon(mann, barn2, FOEDSEL);
        assertRelasjon(mann, barn3, FOEDSEL);

        assertRelasjon(kvinne, partner3, PARTNER);
        assertRelasjon(kvinne, partner4, PARTNER);
        assertRelasjon(kvinne, barn4, FOEDSEL);
        assertRelasjon(kvinne, barn5, FOEDSEL);
        assertRelasjon(kvinne, barn6, FOEDSEL);

        assertRelasjon(partner1, mann, PARTNER);
        assertRelasjon(partner1, barn1, FOEDSEL);
        assertRelasjon(partner1, barn3, FOEDSEL);

        assertRelasjon(partner2, mann, PARTNER);
        assertRelasjon(partner2, barn2, FOEDSEL);

        assertRelasjon(partner3, kvinne, PARTNER);
        assertRelasjon(partner3, barn4, FOEDSEL);
        assertRelasjon(partner3, barn6, FOEDSEL);

        assertRelasjon(partner4, kvinne, PARTNER);
        assertRelasjon(partner4, barn5, FOEDSEL);

        assertRelasjon(barn1, mann, FAR);
        assertRelasjon(barn1, partner1, MOR);

        assertRelasjon(barn2, mann, FAR);
        assertRelasjon(barn2, partner2, MOR);

        assertRelasjon(barn3, mann, FAR);
        assertRelasjon(barn3, partner1, MOR);

        assertRelasjon(barn4, kvinne, MOR);
        assertRelasjon(barn4, partner3, FAR);

        assertRelasjon(barn5, kvinne, MOR);
        assertRelasjon(barn5, partner4, FAR);

        assertRelasjon(barn6, kvinne, MOR);
        assertRelasjon(barn6, partner3, FAR);
    }

    @Test
    public void setIdenthistorikkPaaPersonOgPartner() {

        Person person = Person.builder().kjonn("M").isNyPerson(true).build();
        Person partner = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn = Person.builder().kjonn("M").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(person), asList(partner), asList(barn), personBestillingKriteriumRequest);

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(asList(RsSivilstandRequest.builder()
                        .sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(DATO_3)
                        .build(),
                RsSivilstandRequest.builder()
                        .sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(DATO_2)
                        .build()
        ));
        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        personerBestillingService.setSivilstandHistorikkPaaPersoner(request, asList(person));

        assertThat(person.getSivilstander().get(0).getPersonRelasjonMed(), is(equalTo(partner)));
        assertThat(person.getSivilstander().get(0).getSivilstand(), is(equalTo(SKILT.getKodeverkskode())));
        assertThat(person.getSivilstander().get(0).getSivilstandRegdato(), is(equalTo(DATO_3)));

        assertThat(person.getSivilstander().get(1).getPersonRelasjonMed(), is(equalTo(partner)));
        assertThat(person.getSivilstander().get(1).getSivilstand(), is(equalTo(GIFT.getKodeverkskode())));
        assertThat(person.getSivilstander().get(1).getSivilstandRegdato(), is(equalTo(DATO_2)));

        assertThat(partner.getSivilstander().get(0).getPersonRelasjonMed(), is(equalTo(person)));
        assertThat(partner.getSivilstander().get(0).getSivilstand(), is(equalTo(SKILT.getKodeverkskode())));
        assertThat(partner.getSivilstander().get(0).getSivilstandRegdato(), is(equalTo(DATO_3)));

        assertThat(partner.getSivilstander().get(1).getPersonRelasjonMed(), is(equalTo(person)));
        assertThat(partner.getSivilstander().get(1).getSivilstand(), is(equalTo(GIFT.getKodeverkskode())));
        assertThat(partner.getSivilstander().get(1).getSivilstandRegdato(), is(equalTo(DATO_2)));
    }

    @Test
    public void setIdenthistorikkPaaPersonSomHarFlerePartnere() {

        Person person = Person.builder().kjonn("M").isNyPerson(true).build();
        Person partner1 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person partner2 = Person.builder().kjonn("K").isNyPerson(true).build();
        Person barn1 = Person.builder().kjonn("M").isNyPerson(true).build();
        Person barn2 = Person.builder().kjonn("M").isNyPerson(true).build();

        personerBestillingService.setRelasjonerPaaPersoner(asList(person), asList(partner1, partner2), asList(barn1, barn2), personBestillingKriteriumRequest);

        RsPartnerRequest partner1Request = new RsPartnerRequest();
        partner1Request.setSivilstander(asList(RsSivilstandRequest.builder()
                        .sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(DATO_4)
                        .build(),
                RsSivilstandRequest.builder()
                        .sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(DATO_3)
                        .build()
        ));
        RsPartnerRequest partner2Request = new RsPartnerRequest();
        partner2Request.setSivilstander(asList(RsSivilstandRequest.builder()
                        .sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(DATO_2)
                        .build(),
                RsSivilstandRequest.builder()
                        .sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(DATO_1)
                        .build()
        ));
        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().addAll(asList(partner1Request, partner2Request));

        personerBestillingService.setSivilstandHistorikkPaaPersoner(request, asList(person));

        assertThat(person.getSivilstander().get(0).getPersonRelasjonMed(), is(equalTo(partner1)));
        assertThat(person.getSivilstander().get(0).getSivilstand(), is(equalTo(SKILT.getKodeverkskode())));
        assertThat(person.getSivilstander().get(0).getSivilstandRegdato(), is(equalTo(DATO_4)));

        assertThat(person.getSivilstander().get(1).getPersonRelasjonMed(), is(equalTo(partner1)));
        assertThat(person.getSivilstander().get(1).getSivilstand(), is(equalTo(GIFT.getKodeverkskode())));
        assertThat(person.getSivilstander().get(1).getSivilstandRegdato(), is(equalTo(DATO_3)));

        assertThat(person.getSivilstander().get(2).getPersonRelasjonMed(), is(equalTo(partner2)));
        assertThat(person.getSivilstander().get(2).getSivilstand(), is(equalTo(SKILT.getKodeverkskode())));
        assertThat(person.getSivilstander().get(2).getSivilstandRegdato(), is(equalTo(DATO_2)));

        assertThat(person.getSivilstander().get(3).getPersonRelasjonMed(), is(equalTo(partner2)));
        assertThat(person.getSivilstander().get(3).getSivilstand(), is(equalTo(GIFT.getKodeverkskode())));
        assertThat(person.getSivilstander().get(3).getSivilstandRegdato(), is(equalTo(DATO_1)));

        assertThat(partner1.getSivilstander().get(0).getPersonRelasjonMed(), is(equalTo(person)));
        assertThat(partner1.getSivilstander().get(0).getSivilstand(), is(equalTo(SKILT.getKodeverkskode())));
        assertThat(partner1.getSivilstander().get(0).getSivilstandRegdato(), is(equalTo(DATO_4)));

        assertThat(partner1.getSivilstander().get(1).getPersonRelasjonMed(), is(equalTo(person)));
        assertThat(partner1.getSivilstander().get(1).getSivilstand(), is(equalTo(GIFT.getKodeverkskode())));
        assertThat(partner1.getSivilstander().get(1).getSivilstandRegdato(), is(equalTo(DATO_3)));

        assertThat(partner2.getSivilstander().get(0).getPersonRelasjonMed(), is(equalTo(person)));
        assertThat(partner2.getSivilstander().get(0).getSivilstand(), is(equalTo(SKILT.getKodeverkskode())));
        assertThat(partner2.getSivilstander().get(0).getSivilstandRegdato(), is(equalTo(DATO_2)));

        assertThat(partner2.getSivilstander().get(1).getPersonRelasjonMed(), is(equalTo(person)));
        assertThat(partner2.getSivilstander().get(1).getSivilstand(), is(equalTo(GIFT.getKodeverkskode())));
        assertThat(partner2.getSivilstander().get(1).getSivilstandRegdato(), is(equalTo(DATO_1)));
    }

    private static void assertRelasjon(Person relasjon1, Person relasjon2, RelasjonType type) {

        assertThat(relasjon1.getRelasjoner(), hasItem(both(
                hasProperty("personRelasjonMed", equalTo(relasjon2))).and(
                hasProperty("relasjonTypeNavn", equalTo(type.getName())))
        ));
    }
}