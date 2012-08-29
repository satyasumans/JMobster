package fi.vincit.jmobster.processor.model;

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

import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.util.groups.GroupFilter;
import fi.vincit.jmobster.util.groups.HasGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class FieldAnnotation implements HasGroups<Class>, HasType {

    private static final Logger LOG = LoggerFactory.getLogger( FieldAnnotation.class );

    private Class[] groups;
    private Class type;
    private Annotation annotation;

    public FieldAnnotation(Annotation annotation) {
        this.groups = extractGroupsFromAnnotation(annotation);
        this.type = annotation.annotationType();
        this.annotation = annotation;
    }

    private Class[] extractGroupsFromAnnotation( Annotation annotation ) {
        try {
            Method groupsMethod = annotation.getClass().getMethod( "groups" );
            Object result = groupsMethod.invoke(annotation);
            Class[] annotationGroups = (Class[])result;
            return annotationGroups;
        } catch( NoSuchMethodException e ) {
            LOG.warn( "Validator {} doesn't have groups. Ignoring grouping for that annotation", annotation.getClass().getCanonicalName() );
        } catch( InvocationTargetException e ) {
            LOG.error( "Error e", e );
        } catch( IllegalAccessException e ) {
            LOG.error( "Error e", e );
        }
        return new Class[0];
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

    public Annotation getAnnotation() {
        return annotation;
    }
}
