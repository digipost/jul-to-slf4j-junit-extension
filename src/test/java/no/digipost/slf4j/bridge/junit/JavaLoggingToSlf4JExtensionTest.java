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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static uk.co.probablyfine.matchers.Java8Matchers.where;

@ExtendWith(LogbackInspector.Extension.class)
class JavaLoggingToSlf4JExtensionTest {

    @Test
    void logsToSlf4J(LogbackInspector logback) {
        Logger.getLogger(JavaLoggingToSlf4JExtensionTest.class.getName()).info("Message logged to SLF4J");
        assertThat(logback, where(LogbackInspector::allLoggedMessages, hasItem("Message logged to SLF4J")));
    }

}