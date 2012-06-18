package fi.vincit.jmobster;

import fi.vincit.jmobster.converter.JavaToJSValueConverter;
import fi.vincit.jmobster.converter.valueconverters.ConverterMode;
import fi.vincit.jmobster.util.ModelWriter;

public class JMobsterFactory {
    public static ModelGenerator getInstance(String framework, ModelWriter writer) {
        if( "backbone.js".equalsIgnoreCase(framework) ) {
        ModelProcessor modelProcessor = new ModelProcessor(writer);
        return new DefaultModelGenerator(modelProcessor, new JavaToJSValueConverter( ConverterMode.NULL_AS_DEFAULT));
        } else {
            throw new IllegalArgumentException("Framework " + framework + "not supported");
        }
    }
}
