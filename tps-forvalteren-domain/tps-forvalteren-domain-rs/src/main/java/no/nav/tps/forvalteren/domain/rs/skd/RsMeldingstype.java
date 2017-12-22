package no.nav.tps.forvalteren.domain.rs.skd;

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
    
    private String transtype;
    
    private String maskindato;

    private String maskintid;

    private String aarsakskode;

    private String sekvensnr;

}