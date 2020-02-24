/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */
package ch.fhnw.digibp.external.config

import org.camunda.bpm.client.ExternalTaskClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExternalTaskClientConfig {
    @Value("\${camunda-rest.url}")
    private val camundaRestUrl: String? = null

    @Bean
    fun externalTaskClient(): ExternalTaskClient {
        return ExternalTaskClient.create()
                .baseUrl(camundaRestUrl)
                .asyncResponseTimeout(29000)
                .disableBackoffStrategy()
                .build()
    }
}