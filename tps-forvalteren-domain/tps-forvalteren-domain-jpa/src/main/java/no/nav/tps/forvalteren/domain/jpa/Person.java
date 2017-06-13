package no.nav.tps.forvalteren.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PERSON")
public class Person extends ChangeStamp {

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

    @Column(name = "SPESREG", length = 4)
    private String spesreg;

    @Column(name = "SPESREG_DATO")
    private LocalDateTime spesregDato;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL)
    private List<Gateadresse> gateadresse = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL)
    private List<Postadresse> postadresse = new ArrayList<>();

    @NotNull
    @Column(name = "REGDATO", nullable = false)
    private LocalDateTime regdato;

}