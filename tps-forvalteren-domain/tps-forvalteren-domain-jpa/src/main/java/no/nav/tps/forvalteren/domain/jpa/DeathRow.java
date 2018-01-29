package no.nav.tps.forvalteren.domain.jpa;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_DEATH_ROW")
public class DeathRow extends ChangeStamp {

    private static final String SEQ = "T_DEATH_ROW_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "DEATH_ROW_ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "IDENT", nullable = false)
    private String ident;

    @Column(name = "ACTION", nullable = false)
    private String action;

    @Column(name = "ENVIRONMENT", nullable = false)
    private String environment;

    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "TILSTAND")
    private String tilstand;

    @Column(name = "DOEDSDATO", nullable = false)
    private LocalDate doedsdato;

}
