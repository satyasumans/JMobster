package fi.vincit.jmobster.processor.frameworks.backbone;
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

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.defaults.base.BaseModelProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.type.FieldTypeConverterManager;
import fi.vincit.jmobster.processor.frameworks.backbone.validator.writer.BackboneValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.ItemHandler;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ItemStatus;
import fi.vincit.jmobster.util.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 *     Backbone.js implementation of ModelProcessor. It will put
 *     all the given models inside a single namespace called "Models".
 * </p>
 */
public class BackboneModelProcessor extends BaseModelProcessor {
    private static final Logger LOG = LoggerFactory.getLogger( BackboneModelProcessor.class );

    private static final String NAMESPACE_START = "{";


    private static final String VARIABLE = "var";
    private static final String NAMESPACE_END = "};";
    private static final String DEFAULT_START_COMMENT = "/*\n * Auto-generated file\n */";
    private static final String DEFAULT_NAMESPACE = "Models";

    private static final String DEFAULTS_BLOCK_NAME = "defaults";
    private static final String RETURN_BLOCK = "return "; // Note the space
    private static final String VALIDATOR_BLOCK_NAME = "validation";

    private static final String MODEL_EXTEND_START = ": Backbone.Model.extend({";
    private static final String MODEL_EXTEND_END = "})";

    private String startComment;
    private String namespaceName;

    private JavaScriptWriter writer;

    final private BackboneValidatorWriterManager validatorWriterManager;
    final private FieldValueConverter valueConverter;

    /**
     * Construct slightly customized model processor with custom writer, naming strategy and annotation writer.
     * @param writer Writer
     */
    public BackboneModelProcessor(DataWriter writer, FieldValueConverter valueConverter, FieldTypeConverterManager typeConverterManager) {
        super(writer, valueConverter);
        this.startComment = DEFAULT_START_COMMENT;
        this.namespaceName = DEFAULT_NAMESPACE;
        this.validatorWriterManager = new BackboneValidatorWriterManager(this.writer);
        this.valueConverter = valueConverter;
    }

    @Override
    public void startProcessing() throws IOException {
        this.writer = new JavaScriptWriter(getWriter());
        this.writer.open();
        this.validatorWriterManager.setWriter(this.writer);

        this.writer.writeLine( startComment );
        this.writer.writeLine( VARIABLE + " " + namespaceName + " = " + NAMESPACE_START );
        this.writer.indent();
    }

    @Override
    public void processModel( Model model, ItemStatus status ) {
        String modelName = model.getName();

        this.writer.write( modelName ).writeLine( MODEL_EXTEND_START ).indent();

        writeValidators( model );
        writeFields( model );

        this.writer.indentBack();
        this.writer.writeLine( MODEL_EXTEND_END, ",", status.isNotLastItem() );
    }

    private void writeValidators( Model model ) {
        writer.writeKey( VALIDATOR_BLOCK_NAME ).startBlock();
        final ItemHandler<Validator> validatorWriter = new ItemHandler<Validator>() {
            @Override
            public void process( Validator validator, ItemStatus status ) {
                validatorWriterManager.write( validator, status );
            }
        };

        ItemProcessor.process( model.getFields() ).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                writer.writeKey(field.getName()).startBlock();
                ItemProcessor.process(validatorWriter, field.getValidators());
                writer.endBlock( status );
            }
        });
        writer.endBlock( ItemStatuses.notLast());
    }

    private void writeFields( Model model ) {
        writer.writeKey(DEFAULTS_BLOCK_NAME).startAnonFunction();
        writer.write(RETURN_BLOCK).startBlock();
        ItemProcessor.process( model.getFields() ).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                String defaultValue = valueConverter.convert(field.getFieldType(), null);
                writer.writeKeyValue(field.getName(), defaultValue, status);
            }
        });
        writer.endBlock( ItemStatuses.last());
        writer.endFunction( ItemStatuses.lastIfFalse( model.hasValidations() ) );
    }

    @Override
    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing() throws IOException {
        this.writer.indentBack();
        this.writer.writeLine( NAMESPACE_END );
        this.writer.close();
    }

    /**
     * Set start commend for the model file. Note that you have include
     * comment start and end marks and wrap the comment appropriately.
     * @param startComment Start comment
     */
    public void setStartComment( String startComment ) {
        this.startComment = startComment;
    }

    /**
     * Set namespace in which the models are created
     * @param namespaceName Namespace
     */
    public void setNamespaceName( String namespaceName ) {
        this.namespaceName = namespaceName;
    }
}
