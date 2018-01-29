package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.rs.dodsmelding.RsDodsmeldingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConvertJsonToRsDodsmeldingType {

    @Autowired
    private ObjectMapper mapper;



    public RsDodsmeldingType execute(RsDodsmeldingType meldinger) {
        //RsDodsmeldingType melding = mapper.readValue(meldinger, RsDodsmeldingType.class);




        return null;
    }

}
