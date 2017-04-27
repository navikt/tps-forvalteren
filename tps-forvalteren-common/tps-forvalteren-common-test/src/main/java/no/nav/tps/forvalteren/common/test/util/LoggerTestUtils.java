package no.nav.tps.forvalteren.common.test.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.when;


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
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent) argument).getFormattedMessage().contains(token);
            }
        };
    }

    public static ArgumentMatcher<ILoggingEvent> hasLevelEqualTo(final Level level) {
        return new ArgumentMatcher<ILoggingEvent>() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent) argument).getLevel() == level;
            }
        };
    }
}