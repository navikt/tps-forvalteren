package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.springframework.stereotype.Service;

@Service
public class ContainsXmlElements {

    public boolean execute(String message) {
        if (message.matches(".*[</>].*")) {
            return true;
        }
        return false;
    }
}
