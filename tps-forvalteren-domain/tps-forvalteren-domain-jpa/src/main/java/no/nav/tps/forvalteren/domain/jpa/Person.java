package no.nav.tps.forvalteren.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
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

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PERSON")
@Entity
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

    @NotNull
    @Column(name = "FORNAVN", nullable = false, length = 50)
    private String fornavn;

    @Column(name = "MELLOMNAVN", length = 50)
    private String mellomnavn;

    @NotNull
    @Column(name = "ETTERNAVN", nullable = false, length = 50)
    private String etternavn;

    @Column(name = "STATSBORGERSKAP", length = 3)
    private String statsborgerskap;

    @NotNull
    @Column(name = "REGDATO", nullable = false)
    private LocalDateTime regdato;

    @NotNull
    @CreatedDate
    @Column(name = "OPPRETTET_DATO", nullable = false, updatable = false)
    private LocalDateTime opprettetDato;

    @NotBlank
    @CreatedBy
    @Column(name = "OPPRETTET_AV", nullable = false, updatable = false)
    private String opprettetAv;

    @NotNull
    @LastModifiedDate
    @Column(name = "ENDRET_DATO", nullable = false)
    private LocalDateTime endretDato;

    @NotBlank
    @LastModifiedBy
    @Column(name = "ENDRET_AV", nullable = false)
    private String endretAv;

    @Column(name = "SPESREG", length = 4)
    private String spesreg;

    @Column(name = "SPESREG_DATO")
    private LocalDateTime spesregDato;

    @Column(name = "BO_GATEADRESSE", length = 50)
    private String boGateadresse;

    @Column(name = "BO_HUSNUMMER", length = 4)
    private String boHusnummer;

    @Column(name = "BO_GATEKODE", length = 5)
    private String boGatekode;

    @Column(name = "BO_POSTNUMMER", length = 4)
    private String boPostnummer;

    @Column(name = "BO_KOMMUNENR", length = 4)
    private String boKommunenr;

    @Column(name = "BO_FLYTTE_DATO")
    private LocalDateTime boFlytteDato;

    @Column(name = "POST_LINJE_1", length = 30)
    private String postLinje1;

    @Column(name = "POST_LINJE_2", length = 30)
    private String postLinje2;

    @Column(name = "POST_LINJE_3", length = 30)
    private String postLinje3;

    @Column(name = "POST_LAND", length = 3)
    private String postLand;

}