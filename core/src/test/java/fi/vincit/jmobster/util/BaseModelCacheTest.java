package fi.vincit.jmobster.util;

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

import fi.vincit.jmobster.processor.ModelFactory;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class BaseModelCacheTest {
    public static class TestModelCache extends BaseModelCache<LanguageContext<DataWriter>, DataWriter> {
        public TestModelCache(ModelProcessor<LanguageContext<DataWriter>, DataWriter> modelGenerator,
                              ModelFactory modelFactory) {
            super(modelGenerator, modelFactory);
        }
    }
    
    @Mock ModelProcessor modelProcessor;
    @Mock ModelFactory modelFactory;
    @Mock LanguageContext languageContext;
    @Mock DataWriter dataWriter;

    private Model testModel;

    private static Collection TEST_CLASSES = Arrays.asList(TestModel.class);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        initMocks();
    }
    
    private TestModelCache getModelCache() {
        TestModelCache modelCache = new TestModelCache(modelProcessor, modelFactory);
        return modelCache;
    }

    public static class TestModel {
    }

    private void initMocks() {
        testModel = mock(Model.class);
        when(testModel.getName()).thenReturn("TestModel");
        Collection<Model> models = Arrays.asList(testModel);
        when(modelFactory.createAll(TEST_CLASSES)).thenReturn(models);

        when(modelProcessor.getLanguageContext()).thenReturn(languageContext);
        when(languageContext.getWriter()).thenReturn(dataWriter);

        when(dataWriter.toString()).thenReturn("Test_Model", "Test_Model_2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetModel_NoneFound() {
        TestModelCache modelCache = getModelCache();
        modelCache.getModelByNameAndGroupClasses("test");
    }

    @Test
    public void testGetModel_FoundForFirstTime() {
        TestModelCache modelCache = getModelCache();

        modelCache.addModels(TEST_CLASSES);

        String generatedModel = modelCache.getModelByNameAndGroupClasses("TestModel");

        assertThat(generatedModel, is("Test_Model"));

        verify(modelProcessor).doProcessModel(eq(testModel), any(ItemStatus.class));
    }

    @Test
    public void testGetModel_GotFromCache() {
        TestModelCache modelCache = getModelCache();

        modelCache.addModels(TEST_CLASSES);

        String generatedModel1 = modelCache.getModelByNameAndGroupClasses("TestModel");
        String generatedModel2 = modelCache.getModelByNameAndGroupClasses("TestModel");

        assertThat(generatedModel1, is("Test_Model"));
        assertThat(generatedModel2, is("Test_Model"));

        verify(modelProcessor).doProcessModel(eq(testModel), any(ItemStatus.class));
    }

    @Test
    public void testClearModelCache() {
        TestModelCache modelCache = getModelCache();

        modelCache.addModels(TEST_CLASSES);

        String generatedModel1 = modelCache.getModelByNameAndGroupClasses("TestModel");
        modelCache.clearModelCache();
        String generatedModel2 = modelCache.getModelByNameAndGroupClasses("TestModel");

        assertThat(generatedModel1, is("Test_Model"));
        assertThat(generatedModel2, is("Test_Model_2"));

        verify(modelProcessor, times(2)).doProcessModel(eq(testModel), any(ItemStatus.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClearModelCacheAndModels() {
        TestModelCache modelCache = getModelCache();

        modelCache.addModels(TEST_CLASSES);

        modelCache.getModelByNameAndGroupClasses("TestModel");
        modelCache.clearModelCacheAndModels();
        modelCache.getModelByNameAndGroupClasses("TestModel");
    }

    @Test
    public void testGetModelNames() {
        TestModelCache modelCache = getModelCache();
        modelCache.addModels(TEST_CLASSES);
        assertThat(modelCache.getModelNames(), hasItems("TestModel") );
    }
}
