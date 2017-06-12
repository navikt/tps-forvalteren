package no.nav.tps.forvalteren.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.REMOVE)
    private List<Gateadresse> gateadresse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.REMOVE)
    private List<Postadresse> postadresse;

}