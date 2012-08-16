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

import fi.vincit.jmobster.processor.model.Validator;

public abstract class BaseValidator implements Validator {
    // TODO: Required and optional types
    private final Class type;
    private final Class[] groups;

    protected BaseValidator(Class type, Class[] groups) {
        this.type = type;
        if( groups != null ) {
            this.groups = groups.clone();
        } else {
            this.groups = new Class[0];
        }
    }

    @Override
    public Class[] getGroups() {
        return groups;
    }

    @Override
    public boolean hasGroups() {
        return groups.length > 0;
    }

    @Override
    public Class getType() {
        return type;
    }
}
