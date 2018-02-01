package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;

@Transactional
@RestController
@RequestMapping(value = "api/v1/doedsmelding")
public class DeathRowController {

    @LogExceptions
    @PostMapping("/")
    public void opprett(@RequestBody Meldinger melding) {
        System.out.println("Opprett: " + melding.toString());
    }

    @LogExceptions
    @PostMapping("/edit")
    public void endre(@RequestBody Melding melding) {
        System.out.println("Endre: " + melding.toString());
    }

    @LogExceptions
    @GetMapping("/")
    public List<Melding> hent() {
        List<Melding> meldinger = new ArrayList<>();
        Melding melding = new Melding();
        melding.setIdent("1222333333");
        melding.setDoedsdato(LocalDate.now());
        melding.setMiljoe("u5");
        melding.setHandling("U");
        meldinger.add(melding);
        meldinger.add(melding);
        meldinger.add(melding);
        return meldinger;
    }

    @LogExceptions
    @PostMapping("/delete/{ident}")
    public void slette(@PathVariable String ident) {
        System.out.println("Slette: " + ident);
    }

    @LogExceptions
    @PostMapping("/delete")
    public void toemSkjema(){
        System.out.println("Toem skjema");
    }

    @LogExceptions
    @PostMapping("/send")
    public void sendTilTps(@RequestBody List<Melding> meldinger){
        System.out.println("Send til TPS");
    }
}