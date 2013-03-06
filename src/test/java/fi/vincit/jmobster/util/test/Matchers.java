package fi.vincit.jmobster.util.test;

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

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Custom matchers for tests
 */
public class Matchers {
    public static Matcher<Class> isClass(final Class<?> numberClass) {
        return new BaseMatcher<Class>() {
            @Override
            public boolean matches(Object o) {
                return o.equals(numberClass);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(numberClass.toString());
            }
        };
    }
}
