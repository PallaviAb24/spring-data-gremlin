/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.common;

import com.microsoft.spring.data.gremlin.exception.GremlinIllegalConfigurationException;
import com.microsoft.spring.data.gremlin.telemetry.EmptyTracker;
import com.microsoft.spring.data.gremlin.telemetry.TelemetryTracker;
import lombok.NoArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.microsoft.spring.data.gremlin.common.TestConstants.EMPTY_STRING;
import static com.microsoft.spring.data.gremlin.common.TestConstants.ILLEGAL_ENDPOINT_PORT;

@ContextConfiguration(classes = {GremlinFactoryUnitTest.TestConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class GremlinFactoryUnitTest {

    @Autowired
    private GremlinFactory factory;

    @Test(expected = GremlinIllegalConfigurationException.class)
    public void testGremlinFactoryException() {
        final GremlinConfig config = GremlinConfig.builder(TestConstants.FAKE_ENDPOINT, TestConstants.FAKE_USERNAME,
                TestConstants.FAKE_PASSWORD).build();

        new GremlinFactory(config).getGremlinClient();
    }

    @Test
    public void testGremlinFactoryNormal() {
        final Client client = factory.getGremlinClient();

        Assert.assertEquals(client.getCluster().getPort(), TestConstants.DEFAULT_ENDPOINT_PORT);
        Assert.assertFalse(client.getSettings().getSession().isPresent());
    }

    @Configuration
    @NoArgsConstructor
    static class TestConfiguration {

        @Bean
        public TelemetryTracker getTelemetryTracker() {
            return new EmptyTracker();
        }

        @Bean
        public GremlinFactory getGremlinFactory() {
            return new GremlinFactory(getGremlinConfig());
        }

        @Bean
        public GremlinConfig getGremlinConfig() {
            return GremlinConfig.builder(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING)
                    .port(ILLEGAL_ENDPOINT_PORT)
                    .build();
        }
    }
}
