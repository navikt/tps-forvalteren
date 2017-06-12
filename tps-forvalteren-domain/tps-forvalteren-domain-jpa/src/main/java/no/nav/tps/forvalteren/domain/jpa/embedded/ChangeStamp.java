package no.nav.tps.forvalteren.domain.jpa.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ChangeStamp {

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

}
