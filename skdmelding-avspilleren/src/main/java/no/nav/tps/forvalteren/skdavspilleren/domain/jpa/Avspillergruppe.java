package no.nav.tps.forvalteren.skdavspilleren.domain.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.embedded.ChangeStamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_AVSPILLERGRUPPE")
public class Avspillergruppe extends ChangeStamp {
    
    private static final String SEQ = "T_GRUPPE_SEQ";
    
    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "GRUPPE_ID", nullable = false, updatable = false)
    private Long id;
    
    @Column(name = "NAVN", unique = true, nullable = false, length = 50)
    private String navn;
    
    @Column(name = "BESKRIVELSE", length = 200)
    private String beskrivelse;
    
    @OrderBy("sekvensnummer")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "avspillergruppe")
    private List<SkdmeldingAvspillerdata> skdmeldinger = new ArrayList<>();
}
