package no.nav.tps.forvalteren.domain.test.builder;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;

import java.time.LocalDateTime;

public final class GateadresseBuilder {
    private String adresse;
    private String husnummer;
    private String gatekode;
    private Long id;
    private Person person;
    private String kommunenr;
    private LocalDateTime flyttedato;
    private String postnr;

    private GateadresseBuilder() {
    }

    public static GateadresseBuilder aGateadresse() {
        return new GateadresseBuilder();
    }

    public GateadresseBuilder withAdresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public GateadresseBuilder withHusnummer(String husnummer) {
        this.husnummer = husnummer;
        return this;
    }

    public GateadresseBuilder withGatekode(String gatekode) {
        this.gatekode = gatekode;
        return this;
    }

    public GateadresseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public GateadresseBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    public GateadresseBuilder withKommunenr(String kommunenr) {
        this.kommunenr = kommunenr;
        return this;
    }

    public GateadresseBuilder withFlyttedato(LocalDateTime flyttedato) {
        this.flyttedato = flyttedato;
        return this;
    }

    public GateadresseBuilder withPostnr(String postnr) {
        this.postnr = postnr;
        return this;
    }

    public GateadresseBuilder but() {
        return aGateadresse().withAdresse(adresse).withHusnummer(husnummer).withGatekode(gatekode).withId(id).withPerson(person).withKommunenr(kommunenr).withFlyttedato(flyttedato).withPostnr(postnr);
    }

    public Gateadresse build() {
        Gateadresse gateadresse = new Gateadresse();
        gateadresse.setAdresse(adresse);
        gateadresse.setHusnummer(husnummer);
        gateadresse.setGatekode(gatekode);
        gateadresse.setId(id);
        gateadresse.setPerson(person);
        gateadresse.setKommunenr(kommunenr);
        gateadresse.setFlyttedato(flyttedato);
        gateadresse.setPostnr(postnr);
        return gateadresse;
    }
}
