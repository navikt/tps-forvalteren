package no.nav.tps.forvalteren.common.java.message;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.UNKNOWN_MESSAGE_KEY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class MessageProviderTest {

    @InjectMocks
    private MessageProvider messageProvider;

    @Mock
    private MessageSource messageSource;

    @Test
    public void returnsMessageFromMessageSourceWhenNoAdditionalArgumentsAreSupplied() {
        when(messageSource.getMessage(eq("result.message"), any(Object[].class), any(Locale.class)))
                .thenReturn("Result message");

        String result = messageProvider.get("result.message");

        assertThat(result, is(equalTo("Result message")));
    }

    @Test
    public void returnsMessageFromMessageSourceWhenAdditionalArgumentsAreSupplied() {
        when(messageSource.getMessage(eq("result.message"), any(Object[].class), any(Locale.class)))
                .thenReturn("Result message");

        String result = messageProvider.get("result.message", "Result", "message");

        assertThat(result, is(equalTo("Result message")));
    }

    @Test
    public void looksUpLocalizedStringFromMessageResource() {
        Locale norwegianLocale = Locale.forLanguageTag("nb");
        Locale englishLocale = Locale.forLanguageTag("en");

        LocaleContextHolder.setLocale(norwegianLocale);
        messageProvider.get("result.message");

        LocaleContextHolder.setLocale(englishLocale);
        messageProvider.get("result.message");

        verify(messageSource).getMessage(eq("result.message"), any(Object[].class), eq(norwegianLocale));
        verify(messageSource).getMessage(eq("result.message"), any(Object[].class), eq(englishLocale));
    }

    @Test
    public void callsMessageSourceWithUnknownMessageKeyWhenMessageIsNotFound() {
        NoSuchMessageException thrownException = new NoSuchMessageException("result.message");
        when(messageSource.getMessage(eq("result.message"), any(Object[].class), any(Locale.class)))
                .thenThrow(thrownException);

        messageProvider.get("result.message", "Result", "message");

        verify(messageSource).getMessage(eq(UNKNOWN_MESSAGE_KEY), eq(new String[] { "result.message" }), any(Locale.class));
    }

//    @Test
//    public void logsWarningWhenMessageIsNotFound() {
//        NoSuchMessageException thrownException = new NoSuchMessageException("result.message");
//        when(messageSource.getMessage(eq("result.message"), any(Object[].class), any(Locale.class))).thenThrow(thrownException);
//        when(messageSource.getMessage(eq(UNKNOWN_MESSAGE_KEY), any(Object[].class), any(Locale.class))).thenReturn("Message not found");
//
//        Appender<ILoggingEvent> mockedAppender = LoggerTestUtils.getMockedAppender(MessageProvider.class.getCanonicalName());
//
//        messageProvider.get("result.message");
//
//        verify(mockedAppender).doAppend(argThat(both(hasLevelEqualTo(WARN)).and(hasMessageContaining("Message not found"))));
//    }
//
//    @Test
//    public void logsErrorWhenDefaultMessageIsNotFound() {
//        NoSuchMessageException thrownException = new NoSuchMessageException("result.message");
//        when(messageSource.getMessage(eq("result.message"), any(Object[].class), any(Locale.class))).thenThrow(thrownException);
//        when(messageSource.getMessage(eq(UNKNOWN_MESSAGE_KEY), any(Object[].class), any(Locale.class))).thenThrow(thrownException);
//
//        Appender<ILoggingEvent> mockedAppender = LoggerTestUtils.getMockedAppender(MessageProvider.class.getCanonicalName());
//
//        messageProvider.get("result.message");
//
//        verify(mockedAppender).doAppend(argThat(both(hasLevelEqualTo(ERROR)).and(hasMessageContaining(UNKNOWN_MESSAGE_KEY))));
//    }
}
