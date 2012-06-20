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

import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;

/**
 * Interface for writing validator to model writer
 */
public interface ValidationAnnotationProcessor {
    /**
     * Writes the given annotation to model writer
     * @param annotation Annoation to write
     * @param writer Model writer
     */
    void writeValidatorsToStream( Annotation annotation, ModelWriter writer );

    /**
     * Checks that the existing field type is valid. If no
     * type exists, the type is valid.
     * @param type Existing type
     * @throws fi.vincit.jmobster.exception.InvalidType If the type isn't correct
     */
    void validateType(String type);

    /**
     * Returns the type the validator requires. E.g. min/max validators
     * require number in Backbone
     * @return Type as string. Null if no type required.
     */
    String requiredType();

    /**
     * Does the annotation require type information
     * @return True if type is required
     */
    boolean requiresType();

    /**
     * Returns groups that the given annotation has
     * @param annotation Annotation which groups are returned
     * @return Found groups, empty array if none.
     */
    Class[] getGroups(Annotation annotation);

    /**
     * Checks if the given annotation has at least one group
     * @param annotation Annotation to check
     * @return True if the annotation has at least one group, otherwise false.
     */
    boolean hasGroups(Annotation annotation);

    /**
     * Returns the supported annotations. Method should return an empty
     * array if it doesn't support any annotation even though an annotation
     * processor should always support at least one annotation.
     * @return Supported annotation, empty array if none.
     */
    Class[] getSupportedAnnotations();
}
