package fi.vincit.jmobster.processor.frameworks.backbone.validator.writer;

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

import fi.vincit.jmobster.processor.defaults.validator.jsr303.PatternValidator;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSPatternConverter;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptValidatorWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;

public class PatternValidatorWriter extends JavaScriptValidatorWriter<PatternValidator> {

    @Override
    protected void write( JavaScriptContext context, PatternValidator validator, ItemStatus status ) {
        /*
            JSON can't output JS regexp. Change the reg exp to string
         */
        if( context.getOutputMode() == OutputMode.JSON ) {
            String pattern = JavaToJSPatternConverter.convertFromJavaToJSON(validator.getRegexp());
            pattern = pattern.replaceAll("\\\\", "\\\\\\\\");
            pattern = pattern.replaceAll("\"", "\\\\\"");
            pattern = "\"" + pattern + "\"";
            String flags = JavaToJSPatternConverter.getModifiersFromFlags(validator.getFlags());
            flags = "\"" + flags + "\"";
            context.getWriter().writeKey("pattern__regexp");
            context.getWriter().writeArray(status, pattern, flags);
        } else {
            String pattern = JavaToJSPatternConverter.convertFromJava(validator.getRegexp(), validator.getFlags());
            context.getWriter().writeKeyValue("pattern", pattern, status);
        }
    }
}
