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

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SimpleLoggerInspector implements AutoCloseable {

    public static class Extension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

        private SimpleLoggerInspector logInspector;

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            logInspector = new SimpleLoggerInspector();
            logInspector.injectIntoSystemOut();
        }

        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            logInspector.close();
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

    private final PrintStream spyStream;
    private final ByteArrayOutputStream logSink;
    private PrintStream originalSystemOut;

    public SimpleLoggerInspector() {
        logSink = new ByteArrayOutputStream();
        spyStream = new PrintStream(logSink, true);
    }

    public void injectIntoSystemOut() {
        originalSystemOut = System.out;
        System.setOut(spyStream);
    }

    public void restoreSystemOut() {
        if (originalSystemOut != null) {
            System.setOut(originalSystemOut);
            this.originalSystemOut = null;
        }
    }


    public List<String> allLoggedLines() {
        return Stream.of(logSink.toString().split("\\r?\\n")).collect(toList());
    }

    @Override
    public void close() throws IOException {
        restoreSystemOut();
        spyStream.close();
        logSink.close();
    }

}
