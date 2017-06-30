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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_GRUPPE")
public class Gruppe extends ChangeStamp {

    private static final String SEQ = "T_GRUPPE_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "GRUPPE_ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAVN", nullable = false, length = 30)
    private String navn;

    @Column(name = "BESKRIVELSE", length = 200)
    private String beskrivelse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gruppe")
    private List<Person> personer = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "T_GRUPPE_TAG",
            joinColumns = { @JoinColumn(name = "GRUPPE_ID", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "TAG_ID", nullable = false, updatable = false) })
    private List<Tag> tags = new ArrayList<>();

}
