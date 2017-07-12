package no.nav.tps.forvalteren.domain.test.builder;

import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;

import java.time.LocalDateTime;

public final class MatrikkeladresseBuilder {
    private String mellomnavn;
    private String gardsnr;
    private String bruksnr;
    private String festenr;
    private String undernr;
    private Long id;
    private Person person;
    private String kommunenr;
    private LocalDateTime flyttedato;
    private String postnr;

    private MatrikkeladresseBuilder() {
    }

    public static MatrikkeladresseBuilder aMatrikkeladresse() {
        return new MatrikkeladresseBuilder();
    }

    public MatrikkeladresseBuilder withMellomnavn(String mellomnavn) {
        this.mellomnavn = mellomnavn;
        return this;
    }

    public MatrikkeladresseBuilder withGardsnr(String gardsnr) {
        this.gardsnr = gardsnr;
        return this;
    }

    public MatrikkeladresseBuilder withBruksnr(String bruksnr) {
        this.bruksnr = bruksnr;
        return this;
    }

    public MatrikkeladresseBuilder withFestenr(String festenr) {
        this.festenr = festenr;
        return this;
    }

    public MatrikkeladresseBuilder withUndernr(String undernr) {
        this.undernr = undernr;
        return this;
    }

    public MatrikkeladresseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MatrikkeladresseBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    public MatrikkeladresseBuilder withKommunenr(String kommunenr) {
        this.kommunenr = kommunenr;
        return this;
    }

    public MatrikkeladresseBuilder withFlyttedato(LocalDateTime flyttedato) {
        this.flyttedato = flyttedato;
        return this;
    }

    public MatrikkeladresseBuilder withPostnr(String postnr) {
        this.postnr = postnr;
        return this;
    }

    public Matrikkeladresse build() {
        Matrikkeladresse matrikkeladresse = new Matrikkeladresse();
        matrikkeladresse.setMellomnavn(mellomnavn);
        matrikkeladresse.setGardsnr(gardsnr);
        matrikkeladresse.setBruksnr(bruksnr);
        matrikkeladresse.setFestenr(festenr);
        matrikkeladresse.setUndernr(undernr);
        matrikkeladresse.setId(id);
        matrikkeladresse.setPerson(person);
        matrikkeladresse.setKommunenr(kommunenr);
        matrikkeladresse.setFlyttedato(flyttedato);
        matrikkeladresse.setPostnr(postnr);
        return matrikkeladresse;
    }
}
