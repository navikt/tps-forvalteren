package no.nav.tps.forvalteren.common.test.util;

import static org.mockito.Mockito.when;

import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class LoggerTestUtils {

    public static Appender<ILoggingEvent> getMockedAppender(String loggerName) {
        Logger testLogger = (Logger) LoggerFactory.getLogger(loggerName);

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = Mockito.mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        testLogger.addAppender(mockAppender);

        return mockAppender;
    }

    public static ArgumentMatcher<ILoggingEvent> hasMessageContaining(final String token) {
        return new ArgumentMatcher<ILoggingEvent>() {
            @Override public boolean matches(ILoggingEvent iLoggingEvent) {
                return (iLoggingEvent).getFormattedMessage().contains(token);
            }
        };
    }

    public static ArgumentMatcher<ILoggingEvent> hasLevelEqualTo(final Level level) {
        return new ArgumentMatcher<ILoggingEvent>() {
            @Override public boolean matches(ILoggingEvent iLoggingEvent) {
                return (iLoggingEvent).getLevel() == level;
            }
        };
    }
}