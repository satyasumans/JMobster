package fi.vincit.jmobster.util.itemprocessor;

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

/**
 * Collection of helper method for generating
 * ItemStatus object that are initialized to a certain
 * phase. Returned {@link ItemStatus} object cannot be modified.
 */
@SuppressWarnings( "UtilityClassWithoutPrivateConstructor" )
public final class ItemStatuses {

    private static ItemStatus first = new ImmutableItemStatus(2);
    private static ItemStatus last = new ImmutableItemStatus(2, 1);
    private static ItemStatus firstAndLast = new ImmutableItemStatus(1);
    private static ItemStatus notFirstNorLast = new ImmutableItemStatus(3, 1);


    /**
     * @return Item status which is first (but not last)
     */
    public static ItemStatus first() {
        return first;
    }

    /**
     * @return Item status which is last (but not first)
     */
    public static ItemStatus last() {
        return last;
    }

    /**
     * @return Same as {@link ItemStatuses#notFirstNorLast()}
     */
    public static ItemStatus notLast() {
        return notFirstNorLast();
    }

    /**
     * @return Same as {@link ItemStatuses#notFirstNorLast()}
     */
    public static ItemStatus notFirst() {
        return notFirstNorLast();
    }

    /**
     * @return Item status which is first and last.
     */
    public static ItemStatus firstAndLast() {
        return firstAndLast;
    }

    /**
     * @return Item status which is not first nor last
     */
    public static ItemStatus notFirstNorLast() {
        return notFirstNorLast;
    }

    /**
     * If given value is true, the returned item status is last but not first. If false is
     * given, the returned item status is not either last or first.
     * @param value Value
     * @return Item status
     */
    public static ItemStatus lastIfTrue(boolean value) {
        if( value ) {
            return last();
        } else {
            return notFirstNorLast();
        }
    }

    /**
     * If given value is false, the returned item status is last but not first. If true is
     * given, the returned item status is not either last or first.
     * @param value Value
     * @return Item status
     */
    public static ItemStatus lastIfFalse(boolean value) {
        return lastIfTrue(!value);
    }
}
