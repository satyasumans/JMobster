package fi.vincit.jmobster.processor.defaults;

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

import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

public abstract class BaseAnnotationProcessorProvider implements AnnotationProcessorProvider {
    private static final Logger LOG = LoggerFactory.getLogger( BaseAnnotationProcessorProvider.class );

    protected Collection<ValidationAnnotationProcessor> annotationProcessors;
    protected Map<Class, ValidationAnnotationProcessor> baseAnnotationProcessors;

    /**
     * Initializes inner data structures. Add validators use {@link BaseAnnotationProcessorProvider#BaseAnnotationProcessorProvider(fi.vincit.jmobster.processor.ValidationAnnotationProcessor...)}
     * constructor or {@link BaseAnnotationProcessorProvider#addAnnotationProcessor(fi.vincit.jmobster.processor.ValidationAnnotationProcessor)} method.
     */
    protected BaseAnnotationProcessorProvider() {
        baseAnnotationProcessors = new HashMap<Class, ValidationAnnotationProcessor>();
        annotationProcessors = new ArrayList<ValidationAnnotationProcessor>();
    }

    /**
     * Creates provider with custom set of annotation processors
     * @param processors Processors to add
     */
    protected BaseAnnotationProcessorProvider( ValidationAnnotationProcessor... processors ) {
        this();
        for( ValidationAnnotationProcessor processor : processors ) {
            addAnnotationProcessor( processor );
        }
    }

    /**
     * Adds annotation processor to provider.
     * @param annotationProcessor Processor to add
     */
    protected void addAnnotationProcessor( ValidationAnnotationProcessor annotationProcessor ) {
        annotationProcessors.add(annotationProcessor);
        if( annotationProcessor.isBaseValidator() ) {
            baseAnnotationProcessors.put(annotationProcessor.getBaseValidatorForClass(), annotationProcessor);
        }
    }

    @Override
    public boolean isAnnotationForValidation(Annotation annotation) {
        return baseAnnotationProcessors.get(annotation.annotationType()) != null;
    }

    @Override
    public ValidationAnnotationProcessor getBaseValidationProcessor( Annotation annotation ) {
        ValidationAnnotationProcessor processor = baseAnnotationProcessors.get(annotation.annotationType());
        if( processor == null ) {
            LOG.warn("ValidationAnnotationProcessor for annotation {} not found", annotation.annotationType().getName());
        }
        return processor;
    }

    /**
     * Filter the annotations for which a suitable processor is found.
     * @param annotations All annotations
     * @return List of annotations that are used for generating validators
     */
    protected List<ValidationAnnotationProcessor> filterProcessorsToUse( List<Annotation> annotations ) {
        List<ValidationAnnotationProcessor> processorsToUse =
                new ArrayList<ValidationAnnotationProcessor>(annotations.size());
        for( ValidationAnnotationProcessor processor : annotationProcessors ) {
            if( processor.canProcess(annotations) ) {
                processorsToUse.add( processor );
            }
        }
        return processorsToUse;
    }
}