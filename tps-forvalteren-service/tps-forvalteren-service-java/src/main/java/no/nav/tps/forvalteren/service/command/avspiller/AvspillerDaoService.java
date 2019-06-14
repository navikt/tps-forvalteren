package no.nav.tps.forvalteren.service.command.avspiller;

import static java.time.LocalDateTime.now;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspillerProgress;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.repository.jpa.AvspillerProgressRepository;
import no.nav.tps.forvalteren.repository.jpa.AvspillerRepository;

@Log4j
@Service
@Transactional
public class AvspillerDaoService {

    private static final String JSON_ERROR = "Konvertering til Json feilet";

    @Autowired
    private AvspillerRepository avspillerRepository;

    @Autowired
    private AvspillerProgressRepository avspillerProgressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public TpsAvspiller save(TpsAvspiller avspiller) {

        return avspillerRepository.save(avspiller);
    }

    public TpsAvspiller save(RsAvspillerRequest request) {

        return save(TpsAvspiller.builder()
                .ferdig(false)
                .format(request.getFormat().getMeldingFormat())
                .kildeKoe(request.getMiljoeFra())
                .periodeFra(request.getDatoFra())
                .periodeTil(request.getDatoTil())
                .utloepKoe(request.getQueue())
                .antall(0L)
                .request(toJson(request))
                .tidspunkt(now())
                .build());
    }

    public TpsAvspillerProgress save(TpsAvspillerProgress progress) {

        return avspillerProgressRepository.save(progress);
    }

    public TpsAvspiller getStatus(Long avspillerId) {

        return avspillerRepository.findOne(avspillerId);
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writer().writeValueAsString(object);
        } catch (JsonProcessingException | RuntimeException e) {
            log.debug(JSON_ERROR, e);
        }
        return JSON_ERROR;
    }
}
