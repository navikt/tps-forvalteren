package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.TpsServiceRutine;
import org.codehaus.jackson.map.DeserializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Service
public class DefaultGetTpsServiceRutinerService implements GetTpsServiceRutinerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGetTpsServiceRutinerService.class);

    private static final String SERVICE_RUTINER_FILE_PATH = "ServiceRutiner.xml";

    private XmlMapper xmlMapper;

    public DefaultGetTpsServiceRutinerService() {
        xmlMapper = new XmlMapper();

        xmlMapper.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    @Override
    public Collection<TpsServiceRutine> exectue() {

        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(SERVICE_RUTINER_FILE_PATH);

            TpsServiceRutine[] serviceRutinesArray = xmlMapper.readValue(inputStream, TpsServiceRutine[].class);

            return Arrays.asList(serviceRutinesArray);
        } catch (JsonParseException | JsonMappingException exception) {
            LOGGER.error("Failed to map service rutines XML to a TpsServiceRutine object with exception: {}", exception.toString());
        } catch (IOException exception) {
            LOGGER.error("Failed to read file '{}' with exception: {}", SERVICE_RUTINER_FILE_PATH, exception.toString());
        }

        return null;
    }
}