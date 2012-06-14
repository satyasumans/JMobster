package fi.vincit.modelgenerator;

import fi.vincit.modelgenerator.backbone.*;
import fi.vincit.modelgenerator.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    public void startProcessing() throws IOException {
        writer = new ModelWriter(modelFilePath);
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
