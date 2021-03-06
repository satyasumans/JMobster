package fi.vincit.jmobster.processor.frameworks.backbone;

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

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.defaults.DummyDataWriter;
import fi.vincit.jmobster.processor.languages.javascript.BaseJavaScriptModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.io.IOException;

public class DefaultValueProcessor extends BaseJavaScriptModelProcessor {

    private static final String RETURN_BLOCK = "return "; // Note the space

    public static class Builder {
        private String name = "defaults";
        private FieldValueConverter valueConverter;
        private JavaScriptContext context = new JavaScriptContext(
                new JavaScriptWriter(DummyDataWriter.getInstance()),
                OutputMode.JAVASCRIPT
        );

        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setValueConverter(FieldValueConverter valueConverter) {
            this.valueConverter = valueConverter;
            return this;
        }

        public Builder setWriter(DataWriter writer, OutputMode outputMode) {
            this.context = new JavaScriptContext(writer, outputMode);
            return this;
        }

        public DefaultValueProcessor build() {
            return new DefaultValueProcessor(this);
        }
    }

    private DefaultValueProcessor( Builder builder ) {
        super(builder.name);
        setFieldValueConverter(builder.valueConverter);
        setLanguageContext(builder.context);
    }

    @Override
    public void startProcessing(ItemStatus status) throws IOException {
        getWriter().startAnonFunction();
    }

    @Override
    public void processModel(Model model, ItemStatus status) {
        getWriter().write( RETURN_BLOCK ).startBlock();
        ItemProcessor.process( model.getFields() ).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                String defaultValue = getValueConverter().convert( field.getFieldType(), null );
                getWriter().writeKeyValue( field.getName(), defaultValue, status );
            }
        });
        getWriter().endBlock( ItemStatuses.last() );
    }

    @Override
    public void endProcessing(ItemStatus status) throws IOException {
        getWriter().endFunction( status );
    }
}
