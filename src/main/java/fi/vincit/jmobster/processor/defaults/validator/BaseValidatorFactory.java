package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.processor.ValidatorConstructor;
import fi.vincit.jmobster.processor.ValidatorFactory;
import fi.vincit.jmobster.processor.model.Validator;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 *
 */
public abstract class BaseValidatorFactory implements ValidatorFactory {

    final private Set<ValidatorConstructor> validatorConstructors;

    protected BaseValidatorFactory() {
        validatorConstructors = new HashSet<ValidatorConstructor>();
    }

    @Override
    public void setValidator(ValidatorConstructor validatorConstructor) {
        validatorConstructors.add( validatorConstructor );
    }

    @Override
    public List<Validator> createValidators( Annotation[] annotations ) {
        List<Validator> validators = new ArrayList<Validator>(annotations.length);
        Set<Annotation> annotationSet = new HashSet<Annotation>();
        for( Annotation annotation : annotations ) {
            annotationSet.add(annotation);
        }

        for( ValidatorConstructor validatorConstructor : validatorConstructors ) {
            Validator validator = validatorConstructor.construct(annotationSet);
            if( validator != null ) {
                validators.add(validator);
            }
        }
        return validators;
    }
}
