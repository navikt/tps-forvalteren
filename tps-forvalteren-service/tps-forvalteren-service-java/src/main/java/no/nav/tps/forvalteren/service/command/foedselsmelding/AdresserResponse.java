package no.nav.tps.forvalteren.service.command.foedselsmelding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdresserResponse {

    private Adresse boadresse;
    private Postadresse postadresse;
}