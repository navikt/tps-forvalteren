package no.nav.tps.forvalteren.domain.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static javax.persistence.GenerationType.SEQUENCE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_VERGEMAAL")
public class Vergemaal {

    private static final String SEQ = "T_VERGEMAAL_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ)
    @Column(name = "VERGEMAAL_ID", nullable = false, updatable = false)
    private Long id;

    @JoinColumn(name = "IDENT", nullable = false)
    private String ident;

    @Column(name = "SAKS_ID", nullable = false)
    private String saksid;

    @Column(name = "EMBETE", length = 4)
    private String embete;

    @Column(name = "SAKSTYPE", length = 3)
    private String sakstype;

    @Column(name = "VEDTAKSDATO")
    private LocalDateTime vedtaksdato;

    @Column(name = "INTERN_VERGE_ID", nullable = false)
    private String internVergeId;

    @JoinColumn(name = "VERGE_FNR", referencedColumnName = "IDENT")
    private String vergeFnr;

    @Column(name = "VERGETYPE", length = 3)
    private String vergetype;

    @Column(name = "MANDATTYPE", length = 3)
    private String mandattype;

    @Column(name = "MANDATTEKST", length = 100)
    private String mandattekst;

    @Column(name = "VERGEMAAL_SENDT", length= 1)
    private String vergemaalSendt;

}
