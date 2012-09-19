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

import fi.vincit.jmobster.annotation.IgnoreField;
import fi.vincit.jmobster.exception.CannotAccessDefaultConstructorError;
import fi.vincit.jmobster.exception.DefaultConstructorMissingError;
import fi.vincit.jmobster.processor.*;
import fi.vincit.jmobster.processor.model.ModelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Scans the given bean for fields that should be included in the client side model.
 */
public class DefaultModelFieldFactory implements ModelFieldFactory {
    private static final Logger LOG = LoggerFactory.getLogger( DefaultModelFieldFactory.class );

    private boolean allowStaticFields;
    private boolean allowFinalFields;
    private final FieldScanMode scanMode;
    private final ValidatorScanner validatorScanner;

    public DefaultModelFieldFactory( FieldScanMode scanMode, ValidatorScanner validatorScanner ) {
        this.allowStaticFields = false;
        this.allowFinalFields = true;
        this.scanMode = scanMode;
        this.validatorScanner = validatorScanner;
    }

    @Override
    public List<ModelField> getFields( Class clazz ) {
        switch( scanMode ) {
            case BEAN_PROPERTY: return getFieldsByGetters( clazz );
            case DIRECT_FIELD_ACCESS: return getFieldsByDirectFieldAccess( clazz );
            default: throw new IllegalArgumentException("Invalid field scan mode: " + scanMode);
        }
    }

    @Override
    public void setValidatorFilterGroups( GroupMode groupMode, Collection<Class> groups ) {
        validatorScanner.setFilterGroups(groupMode, groups);
    }

    /**
     * Scans the given class for model fields. Accesses bean properties (getter methods).
     * @param clazz Class to scan
     * @return List of model fields. Empty list if nothing found.
     * @throws CannotAccessDefaultConstructorError If the default constructor exists but cannot be accessed
     * @throws DefaultConstructorMissingError If the given model does not have a default constructor
     */
    private List<ModelField> getFieldsByGetters(Class clazz) {
        List<ModelField> fields = new ArrayList<ModelField>();
        try {
            // Introspector will find also properties from super classes
            final BeanInfo beanInfo = Introspector.getBeanInfo( clazz, Introspector.USE_ALL_BEANINFO );
            for( PropertyDescriptor property : beanInfo.getPropertyDescriptors() ) {
                if( shouldAddField(property) ) {
                    final String name = property.getName();
                    // Java's standard class getter is omitted
                    if( name.equals("class") ) {
                        continue;
                    }

                    final ModelField field = new ModelField(property, validatorScanner.getValidators( property ));
                    fields.add(field);
                }
            }
        } catch( IntrospectionException e ) {
            LOG.error("Introspection failed", e);
            throw new IllegalArgumentException("Cannot init property: Introspection failed", e);
        }
        return fields;
    }


    /**
     * Scans the given class for model fields. Accesses field directly via member variables.
     * @param clazz Class to scan
     * @return List of model fields. Empty list if nothing found.
     * @throws CannotAccessDefaultConstructorError If the default constructor exists but cannot be accessed
     * @throws DefaultConstructorMissingError If the given model does not have a default constructor
     */
    private List<ModelField> getFieldsByDirectFieldAccess( Class clazz ) {
        List<ModelField> fields = new ArrayList<ModelField>();

        for( Field field : clazz.getDeclaredFields() ) {
            final boolean wasAccessible = field.isAccessible();
            field.setAccessible(true);
            if( shouldAddField(field) ) {
                ModelField modelField = new ModelField(field, validatorScanner.getValidators( field ));
                fields.add( modelField );
            } else {
                LOG.warn( "Field {} not added to model fields", field.getName() );
            }
            field.setAccessible(wasAccessible);
        }

        // Also find all fields from every super classes
        if( clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class) ) {
            fields.addAll(getFieldsByDirectFieldAccess(clazz.getSuperclass()));
        }

        return fields;
    }

    /**
     * Checks if the field should be added to model fields.
     * @param field Field to checks
     * @return True if the field should be included in model fields. Otherwise false.
     */
    private boolean shouldAddField(Field field) {
        return allowedByAnnotation(field) && allowedByModifiers(field.getModifiers());
    }

    /**
     * Checks if the property should be added to model fields. Uses
     * the getter method properties to determine this.
     * @param property Bean property to check
     * @return True if the property should be included in the model fields. Otherwise false.
     */
    private boolean shouldAddField(PropertyDescriptor property) {
        Method method = property.getReadMethod();
        return allowedByAnnotation(method) && allowedByModifiers(method.getModifiers());
    }

    /**
     * Checks if the given modifiers allow a field or property to
     * be added to model fields
     * @param modifiers Modifiers
     * @return True if the field or property should be included, otherwise false.
     */
    private boolean allowedByModifiers(int modifiers) {
       return  (allowStaticFields || !Modifier.isStatic(modifiers))
                && (allowFinalFields  || !Modifier.isFinal(modifiers));
    }

    /**
     * Checks if the given field or property should be added to model
     * fields. For this method the bean property getter method should
     * be given.
     * @param element Bean property getter or field
     * @return True if the field or property should be included, otherwise false.
     */
    private boolean allowedByAnnotation(AnnotatedElement element) {
        return !element.isAnnotationPresent(IgnoreField.class);
    }

    @Override
    public void setAllowStaticFields( boolean allowStaticFields ) {
        this.allowStaticFields = allowStaticFields;
    }

    @Override
    public void setAllowFinalFields( boolean allowFinalFields ) {
        this.allowFinalFields = allowFinalFields;
    }


}
