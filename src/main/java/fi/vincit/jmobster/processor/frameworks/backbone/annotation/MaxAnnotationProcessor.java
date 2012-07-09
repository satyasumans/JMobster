package fi.vincit.jmobster.processor.frameworks.backbone.annotation;
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

import fi.vincit.jmobster.processor.defaults.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.RequiredTypes;
import fi.vincit.jmobster.util.ModelWriter;

import javax.validation.constraints.Max;
import java.lang.annotation.Annotation;

public class MaxAnnotationProcessor extends BaseValidationAnnotationProcessor {

    public MaxAnnotationProcessor() {
        super( "number", RequiredTypes.get(Max.class) );
        setBaseValidatorForClass(Max.class);
    }

    @Override
    public void writeValidatorsToStreamInternal( ModelWriter writer ) {
        if( containsAnnotation(Max.class) ) {
            Max annotation = findAnnotation(Max.class);
            writer.write( "max: " ).write( "" + ((Max)annotation).value() );
        }
    }

    @Override
    public Class[] getGroupsInternal(Annotation annotation) {
        return ((Max)annotation).groups();
    }
}
