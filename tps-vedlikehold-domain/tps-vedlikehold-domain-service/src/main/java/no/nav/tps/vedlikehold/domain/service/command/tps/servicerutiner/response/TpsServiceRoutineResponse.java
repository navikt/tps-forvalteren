package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TpsServiceRoutineResponse {

    private String xml;
    private Object response;


    /**
    {
        status: {
            kode : 00,
            melding: "something",
            utfyllendeMelding: "something something"

        },
        result: {

        }
    }

*/

}
