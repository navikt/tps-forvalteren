package no.nav.tps.forvalteren.domain.jpa;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "T_PERSON")
@EntityListeners(AuditingEntityListener.class)
public class Person {

    private static final String SEQ = "T_PERSON_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "PERSON_ID", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Column(name = "IDENT", nullable = false, length = 11)
    private String ident;

    @NotNull
    @Column(name = "IDENTTYPE", nullable = false, length = 3)
    private String identtype;

    @NotNull
    @Column(name = "KJONN", nullable = false)
    private Character kjonn;

    @Column(name = "FORNAVN", length = 50)
    private String fornavn;

    @Column(name = "MELLOMNAVN", length = 50)
    private String mellomnavn;

    @Column(name = "ETTERNAVN", length = 50)
    private String etternavn;

    @Column(name = "KORTNAVN", length = 50)
    private String kortnavn;

    @NotNull
    @CreatedDate
    @Column(name = "OPPRETTET_DATO", nullable = false, updatable = false)
    private DateTime opprettetDato;

    @NotBlank
    @CreatedBy
    @Column(name = "OPPRETTET_AV", nullable = false, updatable = false)
    private String opprettetAv;

    @NotNull
    @LastModifiedDate
    @Column(name = "ENDRET_DATO", nullable = false)
    private DateTime endretDato;

    @NotBlank
    @LastModifiedBy
    @Column(name = "ENDRET_AV", nullable = false)
    private String endretAv;

}
