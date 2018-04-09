package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.springframework.stereotype.Service;

@Service
public class ContainsXmlElements {

    public boolean execute(String message) {
        String xmlCharacters = "^</>";
        for (char c : xmlCharacters.toCharArray()) {
            if (message.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
}
