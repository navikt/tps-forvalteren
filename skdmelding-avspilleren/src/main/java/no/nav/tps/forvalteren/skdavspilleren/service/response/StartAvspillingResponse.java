package no.nav.tps.forvalteren.skdavspilleren.service.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class StartAvspillingResponse {
    
    private int antallSendte=0;
    private int antallFeilet=0;
    private List<StatusPaaAvspiltSkdMelding> statusFraFeilendeMeldinger = new ArrayList();
    
    public void incrementAntallSendte() {
        this.antallSendte ++;
    }
    
    public void incrementAntallFeilet() {
        this.antallFeilet ++;
    }
    
    public void addStatusFraFeilendeMeldinger(StatusPaaAvspiltSkdMelding statusFraFeilendeMeldinger) {
        this.statusFraFeilendeMeldinger.add(statusFraFeilendeMeldinger);
    }
}
