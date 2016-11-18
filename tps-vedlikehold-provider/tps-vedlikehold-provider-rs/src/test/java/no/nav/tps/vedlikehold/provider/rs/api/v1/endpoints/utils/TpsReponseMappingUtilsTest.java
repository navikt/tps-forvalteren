package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.TpsAuthorisationService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

/**
 * Created by F148888 on 18.10.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class TpsReponseMappingUtilsTest {

    private static final String XML_RESPONSE = "<enPersonRes><fnr>11111111111</fnr> </enPersonRes>";
    private static final String XML_RESPONSE2 = "<enPersonRes><fnr>22222222222</fnr> </enPersonRes>";
    private static final String XML_RESPONSE3 = "<enPersonRes><fnr>33333333333</fnr> </enPersonRes>";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ObjectMapper objectMapperMock;

    @Mock
    private TpsAuthorisationService tpsAuthorisationServiceMock;

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private MessageProvider messageProviderMock;

    @Mock
    private JSONObject jsonObject;

    @InjectMocks
    private RsTpsResponseMappingUtils responseMappingUtilsMock;

    @Before
    public void before(){

    }

    // Kommentert ut under refaktorering.
    /*
    @Test
    public void xmlResponseToServiceRoutineResponseThrowsIOExceptionWhenMappingFails() throws Exception{
        when(objectMapperMock.readValue(any(String.class), eq(Map.class))).thenThrow(IOException.class);
        expectedException.expect(IOException.class);

        responseMappingUtilsMock.xmlResponseToServiceRoutineResponse(XML_RESPONSE);
    }

    @Test
    public void remapTpsResponseThrowsIOExceptionWhenMappingFails() throws Exception{
        ServiceRoutineResponse response = mock(ServiceRoutineResponse.class);
        when(response.getXml()).thenReturn(XML_RESPONSE);
        when(objectMapperMock.readValue(any(String.class), eq(Map.class))).thenThrow(IOException.class);

        expectedException.expect(IOException.class);

        responseMappingUtilsMock.remapTpsResponse(response);
    }

    @Test
    public void removeUnauthorizedDataDoesNotThrowUnauthorizedWhenAuthorizationAccepted(){
        ServiceRoutineResponse response = mock(ServiceRoutineResponse.class);
        User user = mock(User.class);
        when(response.getXml()).thenReturn(XML_RESPONSE);
        when(tpsAuthorisationServiceMock.isAuthorizedToReadAtLeastOnePerson(any(User.class), any(List.class), any(String.class))).thenReturn(true);
        when(userContextHolderMock.getUser()).thenReturn(user);

//        responseMappingUtilsMock.removeUnauthorizedDataFromTpsResponse(response);

        verify(tpsAuthorisationServiceMock).isAuthorizedToReadAtLeastOnePerson(any(User.class), any(List.class), any(String.class));
    }

    @Test
    public void removeUnauthorizedDataThrowsUnauthorizedWhenAuthorizationFailsForEveryPersonFound() throws Exception{
        ServiceRoutineResponse response = mock(ServiceRoutineResponse.class);
        User user = mock(User.class);
        when(response.getXml()).thenReturn(XML_RESPONSE + XML_RESPONSE + XML_RESPONSE);
        when(tpsAuthorisationServiceMock.isAuthorizedToReadAtLeastOnePerson(any(User.class), any(List.class), any(String.class))).thenReturn(false);
        when(userContextHolderMock.getUser()).thenReturn(user);

        expectedException.expect(HttpUnauthorisedException.class);

//        responseMappingUtilsMock.removeUnauthorizedDataFromTpsResponse(response);
    }

    @Test
    public void removeUnauthorizedDataDoNotThrowUnauthorizedWhenAuthorizationAcceptedForAtLeastOnePerson(){
        ServiceRoutineResponse response = mock(ServiceRoutineResponse.class);
        User user = mock(User.class);
        when(response.getXml()).thenReturn(XML_RESPONSE + XML_RESPONSE + XML_RESPONSE);
        when(tpsAuthorisationServiceMock.isAuthorizedToReadAtLeastOnePerson(any(User.class), any(List.class), any(String.class))).thenReturn(true);
        when(userContextHolderMock.getUser()).thenReturn(user);

//        responseMappingUtilsMock.removeUnauthorizedDataFromTpsResponse(response);
    }
*/
/*
    @Test
    public void removeUnauthorizedDataKeepsOnlyAuthorizedData(){
        ServiceRoutineResponse response = Mockito.spy(new ServiceRoutineResponse("Rand", new Object()));
        User user = mock(User.class);
        when(response.getXml()).thenReturn(XML_RESPONSE + XML_RESPONSE2 + XML_RESPONSE3).thenReturn(response.getXml());
        when(tpsAuthorisationServiceMock.authoriseRequest(any(User.class), any(String.class), any(String.class))).thenReturn(false,true,false);
        when(userContextHolderMock.getUser()).thenReturn(user);

        responseMappingUtilsMock.removeUnauthorizedDataFromTpsResponse(response);

        assertThat(response.getXml(), not(CoreMatchers.containsString(XML_RESPONSE)));
        assertThat(response.getXml(), CoreMatchers.containsString(XML_RESPONSE2));
        assertThat(response.getXml(), not(CoreMatchers.containsString(XML_RESPONSE3)));
    }
    */
}
