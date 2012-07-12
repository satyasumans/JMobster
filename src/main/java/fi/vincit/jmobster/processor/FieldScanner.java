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

import fi.vincit.jmobster.processor.model.ModelField;

import java.util.List;

/**
 * Interface for field scanners. Field scanners searches for
 * model fields from classes.
 */
public interface FieldScanner {
    /**
     * Scan the given class for fields.
     * @param clazz Class to scan
     * @param fieldScanMode Field scanning mode
     * @return List of found model fields. Empty list if nothing found.
     */
    List<ModelField> getFields( Class clazz, FieldScanMode fieldScanMode );

    /**
     * Sets whether static member variables should be included as model fields.
     * Note: This doesn't work for getters in {@link FieldScanMode#BEAN_PROPERTY} mode.
     * @param allowStaticFields True if should, false if not
     */
    void setAllowStaticFields( boolean allowStaticFields );

    /**
     * Sets whether final fields/getters should be included as model fields.
     * @param allowFinalFields True if should, false if not.
     */
    void setAllowFinalFields( boolean allowFinalFields );

    /**
     * How the fields are scanned from a class
     */
    public static enum FieldScanMode {
        /**
         * Use getters. In this mode static fields cannot be used.
         */
        BEAN_PROPERTY,
        /**
         * Get public/protected/fields directly
         */
        DIRECT_FIELD_ACCESS
    }
}
