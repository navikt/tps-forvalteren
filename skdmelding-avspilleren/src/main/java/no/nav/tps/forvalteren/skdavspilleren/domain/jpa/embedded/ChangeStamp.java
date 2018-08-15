package no.nav.tps.forvalteren.skdavspilleren.domain.jpa.embedded;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ChangeStamp {

    @CreatedDate
    @Column(name = "OPPRETTET_DATO", nullable = false, updatable = false)
    private LocalDateTime opprettetDato;

    @CreatedBy
    @Column(name = "OPPRETTET_AV", nullable = false, updatable = false)
    private String opprettetAv;

    @LastModifiedDate
    @Column(name = "ENDRET_DATO", nullable = false)
    private LocalDateTime endretDato;

    @LastModifiedBy
    @Column(name = "ENDRET_AV", nullable = false)
    private String endretAv;

}
