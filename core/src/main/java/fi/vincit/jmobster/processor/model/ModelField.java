package fi.vincit.jmobster.processor.model;
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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Single model field that is converted to the target platform.
 */
public class ModelField {

    final private Class fieldType;
    final private Collection<FieldAnnotation> annotations;
    final private String name;

    /**
     * Model field constructed from Java object field
     * @param field Java field
     * @param annotations Annotations
     */
    public ModelField( Field field, Collection<FieldAnnotation> annotations) {
        this.fieldType = field.getType();
        this.name = field.getName();
        this.annotations = new ArrayList<FieldAnnotation>();
        addAnnotations(annotations);
    }

    /**
     * Model field constructed from bean property
     * @param property Bean property
     * @param annotations Annotations
     */
    public ModelField( PropertyDescriptor property, Collection<FieldAnnotation> annotations ) {
        this.fieldType  = property.getPropertyType();
        this.name = property.getName();
        this.annotations = new ArrayList<FieldAnnotation>();
        addAnnotations(annotations);
    }

    public String getName() {
        return name;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void addAnnotation(FieldAnnotation annotation) {
        this.annotations.add(annotation);
    }

    public final void addAnnotations(Collection<? extends FieldAnnotation> validators) {
        this.annotations.addAll(validators);
    }

    public Collection<FieldAnnotation> getAnnotations() {
        return Collections.unmodifiableCollection(this.annotations);
    }

    public boolean hasAnnotations() {
        return !this.annotations.isEmpty();
    }
}
