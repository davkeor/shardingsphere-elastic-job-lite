/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.elasticjob.lite.util.json;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.shardingsphere.elasticjob.lite.config.JobTypeConfiguration;
import org.apache.shardingsphere.elasticjob.lite.executor.handler.impl.DefaultExecutorServiceHandler;
import org.apache.shardingsphere.elasticjob.lite.fixture.APIJsonConstants;
import org.apache.shardingsphere.elasticjob.lite.fixture.config.TestDataflowJobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.fixture.config.TestJobRootConfiguration;
import org.apache.shardingsphere.elasticjob.lite.fixture.config.TestScriptJobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.fixture.config.TestSimpleJobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.fixture.handler.IgnoreJobExceptionHandler;
import org.apache.shardingsphere.elasticjob.lite.fixture.handler.ThrowJobExceptionHandler;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class JobConfigurationGsonTypeAdapterTest {
    
    @BeforeClass
    public static void setUp() {
        GsonFactory.registerTypeAdapter(TestJobRootConfiguration.class, new JobConfigurationGsonTypeAdapter());
    } 
    
    @Test
    public void assertToSimpleJobJson() {
        assertThat(GsonFactory.getGson().toJson(new TestJobRootConfiguration(
                new TestSimpleJobConfiguration(ThrowJobExceptionHandler.class.getCanonicalName(), DefaultExecutorServiceHandler.class.getCanonicalName()).getTypeConfig())),
                CoreMatchers.is(APIJsonConstants.getSimpleJobJson(ThrowJobExceptionHandler.class.getCanonicalName())));
    }
    
    @Test
    public void assertToDataflowJobJson() {
        assertThat(GsonFactory.getGson().toJson(new TestJobRootConfiguration(new TestDataflowJobConfiguration(true).getTypeConfig())),
                is(APIJsonConstants.getDataflowJobJson(IgnoreJobExceptionHandler.class.getCanonicalName())));
    }
    
    @Test
    public void assertToScriptJobJson() {
        assertThat(GsonFactory.getGson().toJson(new TestJobRootConfiguration(new TestScriptJobConfiguration("test.sh", ThrowJobExceptionHandler.class).getTypeConfig())),
                is(APIJsonConstants.getScriptJobJson(ThrowJobExceptionHandler.class.getCanonicalName())));
    }
    
    @Test
    public void assertFromSimpleJobJson() {
        TestJobRootConfiguration actual = GsonFactory.getGson().fromJson(
                APIJsonConstants.getSimpleJobJson(ThrowJobExceptionHandler.class.getCanonicalName()), TestJobRootConfiguration.class);
        TestJobRootConfiguration expected = new TestJobRootConfiguration(
                new TestSimpleJobConfiguration(ThrowJobExceptionHandler.class.getCanonicalName(), DefaultExecutorServiceHandler.class.getCanonicalName()).getTypeConfig());
        assertThat(GsonFactory.getGson().toJson(actual), is(GsonFactory.getGson().toJson(expected)));
    }
    
    @Test
    public void assertFromDataflowJobJson() {
        TestJobRootConfiguration actual = GsonFactory.getGson().fromJson(
                APIJsonConstants.getDataflowJobJson(IgnoreJobExceptionHandler.class.getCanonicalName()), TestJobRootConfiguration.class);
        TestJobRootConfiguration expected = new TestJobRootConfiguration(new TestDataflowJobConfiguration(true).getTypeConfig());
        assertThat(GsonFactory.getGson().toJson(actual), is(GsonFactory.getGson().toJson(expected)));
    }
    
    @Test
    public void assertFromScriptJobJson() {
        TestJobRootConfiguration actual = GsonFactory.getGson().fromJson(
                APIJsonConstants.getScriptJobJson(ThrowJobExceptionHandler.class.getCanonicalName()), TestJobRootConfiguration.class);
        TestJobRootConfiguration expected = new TestJobRootConfiguration(new TestScriptJobConfiguration("test.sh", ThrowJobExceptionHandler.class).getTypeConfig());
        assertThat(GsonFactory.getGson().toJson(actual), is(GsonFactory.getGson().toJson(expected)));
    }
    
    private static class JobConfigurationGsonTypeAdapter extends AbstractJobConfigurationGsonTypeAdapter<TestJobRootConfiguration> {
    
        @Override
        protected void addToCustomizedValueMap(final String jsonName, final JsonReader in, final Map<String, Object> customizedValueMap) {
        }
    
        @Override
        protected TestJobRootConfiguration getJobRootConfiguration(final JobTypeConfiguration typeConfig, final Map<String, Object> customizedValueMap) {
            return new TestJobRootConfiguration(typeConfig);
        }
    
        @Override
        protected void writeCustomized(final JsonWriter out, final TestJobRootConfiguration value) {
        }
    }
}
