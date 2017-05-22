package no.nav.tps.forvalteren.domain.test.builder;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.joda.time.DateTime;

public final class PersonBuilder extends DomainObjectBuilder<Person> {

    private Long id;
    private String ident;
    private String identtype;
    private Character kjonn;
    private String fornavn;
    private String mellomnavn;
    private String etternavn;
    private String kortnavn;
    private DateTime opprettetDato;
    private String opprettetAv;
    private DateTime endretDato;
    private String endretAv;

    private PersonBuilder() {
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    public PersonBuilder but(){
        return aPerson()
                .withId(id)
                .withIdent(ident)
                .withIdenttype(identtype)
                .withKjonn(kjonn)
                .withFornavn(fornavn)
                .withMellomnavn(mellomnavn)
                .withEtternavn(etternavn)
                .withKortnavn(kortnavn)
                .withOpprettetDato(opprettetDato)
                .withOpprettetAv(opprettetAv)
                .withEndretDato(endretDato)
                .withEndretAv(endretAv);
    }

    public PersonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PersonBuilder withIdent(String ident) {
        this.ident = ident;
        return this;
    }

    public PersonBuilder withIdenttype(String identtype) {
        this.identtype = identtype;
        return this;
    }

    public PersonBuilder withKjonn(Character kjonn) {
        this.kjonn = kjonn;
        return this;
    }

    public PersonBuilder withFornavn(String fornavn) {
        this.fornavn = fornavn;
        return this;
    }

    public PersonBuilder withMellomnavn(String mellomnavn) {
        this.mellomnavn = mellomnavn;
        return this;
    }

    public PersonBuilder withEtternavn(String etternavn) {
        this.etternavn = etternavn;
        return this;
    }

    public PersonBuilder withKortnavn(String kortnavn) {
        this.kortnavn = kortnavn;
        return this;
    }

    public PersonBuilder withOpprettetDato(DateTime opprettetDato) {
        this.opprettetDato = opprettetDato;
        return this;
    }

    public PersonBuilder withOpprettetAv(String opprettetAv) {
        this.opprettetAv = opprettetAv;
        return this;
    }

    public PersonBuilder withEndretDato(DateTime endretDato) {
        this.endretDato = endretDato;
        return this;
    }

    public PersonBuilder withEndretAv(String endretAv) {
        this.endretAv = endretAv;
        return this;
    }

    @Override
    protected Person build() {
        Person instance = new Person();
        instance.setId(id);
        instance.setIdent(ident);
        instance.setIdenttype(identtype);
        instance.setKjonn(kjonn);
        instance.setFornavn(fornavn);
        instance.setMellomnavn(mellomnavn);
        instance.setEtternavn(etternavn);
        instance.setKortnavn(kortnavn);
        instance.setOpprettetDato(opprettetDato);
        instance.setOpprettetAv(opprettetAv);
        instance.setEndretDato(endretDato);
        instance.setEndretAv(endretAv);
        return instance;
    }
}
