package fi.vincit.jmobster.processor;/*
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultNamingStrategyTest {
    @Test
    public void testSimple() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        Model model = new Model(String.class, null);
        String modelName = dns.getName(model);
        assertEquals("String", modelName);
    }

    public static class TestClass1Dto {}

    @Test
    public void testSimpleWithDtoSuffix() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        Model model = new Model(TestClass1Dto.class, null);
        String modelName = dns.getName(model);
        assertEquals("TestClass1", modelName);
    }

    public static class TestClass2DTO {}

    @Test
    public void testSimpleWithDTOSuffix() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        Model model = new Model(TestClass2DTO.class, null);
        String modelName = dns.getName(model);
        assertEquals("TestClass2", modelName);
    }

    public static class TestClass2DtoModel {}

    @Test
    public void testSimpleWithDTOModelSuffix() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        Model model = new Model(TestClass2DtoModel.class, null);
        String modelName = dns.getName(model);
        assertEquals("TestClass2DtoModel", modelName);
    }
}
