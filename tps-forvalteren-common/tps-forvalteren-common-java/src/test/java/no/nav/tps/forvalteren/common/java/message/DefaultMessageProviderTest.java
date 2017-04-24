package no.nav.tps.forvalteren.common.java.message;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import no.nav.tps.forvalteren.common.test.util.LoggerTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.WARN;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.UNKNOWN_MESSAGE_KEY;
import static no.nav.tps.forvalteren.common.test.util.LoggerTestUtils.hasLevelEqualTo;
import static no.nav.tps.forvalteren.common.test.util.LoggerTestUtils.hasMessageContaining;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageProviderTest {

    @InjectMocks
    private DefaultMessageProvider messageProvider;

    @Mock
    private MessageSource messageSourceMock;

    @Test
    public void returnsMessageFromMessageSourceWhenNoAdditionalArgumentsAreSupplied() {
        when(messageSourceMock.getMessage(eq("result.message"), any(Object[].class), any(Locale.class)))
                .thenReturn("Result message");

        String result = messageProvider.get("result.message");

        assertThat(result, is(equalTo("Result message")));
    }

    @Test
    public void returnsMessageFromMessageSourceWhenAdditionalArgumentsAreSupplied() {
        when(messageSourceMock.getMessage(eq("result.message"), any(Object[].class), any(Locale.class)))
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

        verify(messageSourceMock).getMessage(eq("result.message"), any(Object[].class), eq(norwegianLocale));
        verify(messageSourceMock).getMessage(eq("result.message"), any(Object[].class), eq(englishLocale));
    }

    @Test
    public void callsMessageSourceWithUnknownMessageKeyWhenMessageIsNotFound() {
        NoSuchMessageException thrownException = new NoSuchMessageException("result.message");
        when(messageSourceMock.getMessage(eq("result.message"), any(Object[].class), any(Locale.class)))
                .thenThrow(thrownException);

        messageProvider.get("result.message", "Result", "message");

        verify(messageSourceMock).getMessage(eq(UNKNOWN_MESSAGE_KEY), eq(new String[] { "result.message" }), any(Locale.class));
    }

    @Test
    public void logsWarningWhenMessageIsNotFound() {
        NoSuchMessageException thrownException = new NoSuchMessageException("result.message");
        when(messageSourceMock.getMessage(eq("result.message"), any(Object[].class), any(Locale.class))).thenThrow(thrownException);
        when(messageSourceMock.getMessage(eq(UNKNOWN_MESSAGE_KEY), any(Object[].class), any(Locale.class))).thenReturn("Message not found");

        Appender<ILoggingEvent> mockedAppender = LoggerTestUtils.getMockedAppender(DefaultMessageProvider.class.getCanonicalName());

        messageProvider.get("result.message");

        verify(mockedAppender).doAppend(argThat(both(hasLevelEqualTo(WARN)).and(hasMessageContaining("Message not found"))));
    }

    @Test
    public void logsErrorWhenDefaultMessageIsNotFound() {
        NoSuchMessageException thrownException = new NoSuchMessageException("result.message");
        when(messageSourceMock.getMessage(eq("result.message"), any(Object[].class), any(Locale.class))).thenThrow(thrownException);
        when(messageSourceMock.getMessage(eq(UNKNOWN_MESSAGE_KEY), any(Object[].class), any(Locale.class))).thenThrow(thrownException);

        Appender<ILoggingEvent> mockedAppender = LoggerTestUtils.getMockedAppender(DefaultMessageProvider.class.getCanonicalName());

        messageProvider.get("result.message");

        verify(mockedAppender).doAppend(argThat(both(hasLevelEqualTo(ERROR)).and(hasMessageContaining(UNKNOWN_MESSAGE_KEY))));
    }
}
