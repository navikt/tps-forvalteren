package no.nav.tps.forvalteren.domain.rs;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RsPostadresse {

    private Long id;

    private String postLinje1;

    private String postLinje2;

    private String postLinje3;

    private String postLand;
}
