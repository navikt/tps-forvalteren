package no.nav.tps.forvalteren.domain.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_TAG")
public class Tag {

    private static final String SEQ = "T_TAG_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "TAG_ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAVN", nullable = false, length = 25)
    private String navn;

    @Getter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private List<Gruppe> grupper = new ArrayList<>();

}
