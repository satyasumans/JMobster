package fi.vincit.jmobster.processor;
/*
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

import fi.vincit.jmobster.backbone.*;
import fi.vincit.jmobster.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 *     ModelProcessor controls how the models given from {@link fi.vincit.jmobster.ModelGenerator}
 * are processed. This Backbone.js implementation will put all the given
 * models inside a single namespace called "Models".
 * </p>
 */
public class ModelProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( ModelProcessor.class );

    private ModelWriter writer;
    private String modelFilePath;
    private AnnotationProcessor annotationProcessor;

    public ModelProcessor( String modelFilePath ) {
        this.modelFilePath = modelFilePath;
        annotationProcessor = new DefaultAnnotationProcessor(new DefaultAnnotationProcessorProvider());
    }

    public ModelProcessor( ModelWriter writer ) {
        this((String)null);
        this.writer = writer;
    }

    public void startProcessing() throws IOException {
        if( writer == null ) {
            writer = new ModelWriter(modelFilePath);
        }
        writer.open();

        writer.writeLine("/*\n" +
                " * Auto-generated file\n" +
                " */\n" +
                "var Models = {");
        writer.indent();
    }

    public void processModel(Model model, boolean isLastModel) {
        String modelName = model.getModelClass().getSimpleName();

        writer.write(modelName).writeLine( ": Backbone.Model.extend({" ).indent();

        DefaultValueSectionWriter defaultValueSectionWriter = new DefaultValueSectionWriter(writer);
        defaultValueSectionWriter.writeDefaultValues( model.getFields(), model.hasValidations() );

        ValidationSectionWriter validationSectionWriter =
                new ValidationSectionWriter(writer, annotationProcessor);
        validationSectionWriter.writeValidators( model.getFields() );

        writer.indentBack();
        writer.writeLine("})", ",", !isLastModel);
    }

    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing() throws IOException {
        writer.indentBack();
        writer.writeLine("};");
        writer.close();
    }

    public AnnotationProcessor getAnnotationProcessor() {
        return annotationProcessor;
    }

    public String getModelFilePath() {
        return modelFilePath;
    }
}
