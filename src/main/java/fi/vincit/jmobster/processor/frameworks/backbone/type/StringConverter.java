package fi.vincit.jmobster.processor.frameworks.backbone.type;

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

import fi.vincit.jmobster.processor.frameworks.base.BaseFieldTypeConverter;
import fi.vincit.jmobster.processor.languages.javascript.util.JavaScriptUtil;
import fi.vincit.jmobster.processor.model.ModelField;

public class StringConverter extends BaseFieldTypeConverter {

    public StringConverter() {
        super( String.class );
    }

    @Override
    public String convert( ModelField field ) {
        return JavaScriptUtil.asString("Text");
    }
}
