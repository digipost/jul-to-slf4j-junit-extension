/*
 * Copyright (C) Posten Norge AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.digipost.slf4j.bridge.junit;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.stream.Collectors.toList;

public class LogbackInspector extends AppenderBase<ILoggingEvent> {

    public static class Extension implements BeforeEachCallback, ParameterResolver {

        private final LogbackInspector logInspector = LogbackInspector.resolveFromLogback();

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            logInspector.clear();
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extension) {
            return parameterContext.getParameter().getType().isInstance(logInspector);
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return logInspector;
        }

    }

    public static LogbackInspector resolveFromLogback() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        Iterator<Appender<ILoggingEvent>> appenders = rootLogger.iteratorForAppenders();
        while (appenders.hasNext()) {
            Appender<ILoggingEvent> appender = appenders.next();
            if (appender instanceof LogbackInspector) {
                return (LogbackInspector) appender;
            }
        }
        throw new NoSuchElementException("appender of type " + LogbackInspector.class.getName());
    }

    private final Deque<ILoggingEvent> logged = new ConcurrentLinkedDeque<>();

    @Override
    protected void append(ILoggingEvent event) {
        this.logged.addLast(event);
    }

    public List<String> allLoggedMessages() {
        return logged.stream().map(ILoggingEvent::getFormattedMessage).collect(toList());
    }

    public void clear() {
        this.logged.clear();
    }

}
