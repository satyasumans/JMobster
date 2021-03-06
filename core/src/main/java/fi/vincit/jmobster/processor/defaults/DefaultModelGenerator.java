package fi.vincit.jmobster.processor.defaults;

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

import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * <p>
 *      Class used to create a client side model from server side Java entity/DTO/POJO.
 * </p>
 * <p>
 *     If uncaught IOExceptions are caught, the model processing will be terminated.
 *  {@link fi.vincit.jmobster.processor.ModelProcessor#doEndProcessing(ItemStatus)}
 *  is not called in those cases (of unless the exception was thrown from that method).
 *  The error will be logged (level: Error).
 * </p>
 */
public class DefaultModelGenerator<W extends DataWriter> implements ModelGenerator<W> {

    private static final Logger LOG = LoggerFactory
            .getLogger( DefaultModelGenerator.class );

    private final ModelProcessor modelProcessor;

    /**
     * Creates new DefaultModelGenerator
     * @param modelProcessor Model processor to use
     */
    public DefaultModelGenerator(ModelProcessor modelProcessor) {
        this.modelProcessor = modelProcessor;
    }

    @Override
    public void processAll( Collection<Model> models ) {
        processModelsInternal( models );
    }

    @Override
    public void process( Model model ) {
        processModelsInternal(Arrays.asList(model));
    }

    @Override
    public void setLanguageContext(LanguageContext<W> context) {
        modelProcessor.setLanguageContext(context);
    }

    /**
     * Process the given models. If IOException is caught in this method
     * the processing will be terminated as described in the class documentation.
     * @param models Models to process
     */
    private void processModelsInternal( Collection<Model> models ) {
        try {
            modelProcessor.doStartProcessing(ItemStatuses.firstAndLast());
            ItemProcessor.process(models).with(new ItemHandler<Model>() {
                @Override
                public void process( Model model, ItemStatus status ) {
                    modelProcessor.doProcessModel(model, status);
                }
            });
            modelProcessor.doEndProcessing(ItemStatuses.firstAndLast());
        } catch (IOException e) {
            LOG.error("Error", e);
        }
    }

}
