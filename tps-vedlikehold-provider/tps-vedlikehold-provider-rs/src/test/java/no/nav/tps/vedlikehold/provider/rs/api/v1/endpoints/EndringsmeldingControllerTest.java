package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.TpsResponseMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by F148888 on 28.10.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class EndringsmeldingControllerTest {


    @Mock
    private UserContextHolder userContextHolder;

    @Mock
    private TpsRequestService tpsRequestService;

    @Mock
    private TpsResponseMappingUtils tpsResponseMappingUtils;

    @Mock
    private RsRequestMappingUtils mappingUtils;

    @InjectMocks
    private EndringsmeldingController endringsmeldingController;
}
