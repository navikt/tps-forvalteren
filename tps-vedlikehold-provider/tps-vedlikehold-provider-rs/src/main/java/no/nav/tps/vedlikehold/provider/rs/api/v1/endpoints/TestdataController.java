package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.vedlikehold.domain.rs.testdata.RsTestDataRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.TestDataRequest;
import no.nav.tps.vedlikehold.service.command.testdata.GenererFiktiveIdenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
@RestController
@RequestMapping(value = "api/v1")
public class TestdataController {

    @Inject
    private MapperFacade testDataRequestMapper;

    @Autowired
    private GenererFiktiveIdenter genererFiktiveIdenter;

    @RequestMapping(value = "/testdata/fnr", method = RequestMethod.GET)
    public List<String> getGeneratedFnr(@RequestBody RsTestDataRequest rsTestDataRequest) {

        TestDataRequest testDataRequest = testDataRequestMapper.map(rsTestDataRequest, TestDataRequest.class);

        return genererFiktiveIdenter.execute(testDataRequest);
    }
}
