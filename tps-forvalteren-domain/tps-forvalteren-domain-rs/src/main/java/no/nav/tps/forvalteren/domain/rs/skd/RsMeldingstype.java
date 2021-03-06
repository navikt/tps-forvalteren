package no.nav.tps.forvalteren.domain.rs.skd;

import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "meldingstype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RsMeldingstype1Felter.class, name = "t1"),
        @JsonSubTypes.Type(value = RsMeldingstype2Felter.class, name = "t2")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class RsMeldingstype {
    
    private Long id;

    private String beskrivelse;
    @Size(max = 1)
    private String transtype;
    @Size(max = 8)
    private String maskindato;
    @Size(max = 6)
    private String maskintid;
    @Size(max = 2)
    private String aarsakskode;
    @Size(max = 6)
    private String sekvensnr;
}