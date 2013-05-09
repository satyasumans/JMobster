package fi.vincit.jmobster;/*
 * Copyright 2012 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

/*
 * Copyright 2012-2013 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import fi.vincit.jmobster.exception.UnsupportedFramework;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class JMobsterFactoryTest {

    private JavaScriptContext getLanguageContext() {
        return new JavaScriptContext(new StringBufferWriter(), OutputMode.JAVASCRIPT);
    }

    @Test
    public void testCreateBackboneInstance() throws IOException {
        ModelGenerator generator = JMobsterFactory.getModelGeneratorBuilder( "Backbone", getLanguageContext() ).build();
        assertNotNull(generator);
    }

    @Test
    public void testCreateBackboneJSInstance() throws IOException {
        ModelGenerator generator = JMobsterFactory.getModelGeneratorBuilder( "Backbone.js", getLanguageContext() ).build();
        assertNotNull(generator);
    }

    @Test(expected = UnsupportedFramework.class)
    public void testCreateUnsupportedInstance() throws IOException {
        JMobsterFactory.getModelGeneratorBuilder( "Invalid framework", getLanguageContext() );
    }
}
