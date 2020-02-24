/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */
package ch.fhnw.digibp.external.client

import org.camunda.bpm.client.ExternalTaskClient
import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskService
import org.camunda.bpm.engine.variable.value.TypedValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class SurpriseMenuClient {
    @Autowired
    var client: ExternalTaskClient? = null

    @PostConstruct
    private fun subscribeTopics() {
        client!!.subscribe("GetSurpriseMenu")
                .tenantIdIn("showcase")
                .handler { externalTask: ExternalTask, externalTaskService: ExternalTaskService ->
                    try {
                        var vegetarianGuests = false
                        val vegetarianGuestsValue = externalTask.getVariableTyped<TypedValue>("vegetarianGuests")
                        if (vegetarianGuestsValue != null) vegetarianGuests = externalTask.getVariableTyped<TypedValue>("vegetarianGuests").value as Boolean
                        val menu: String
                        menu = if (vegetarianGuests) {
                            Arrays.asList("pizza", "pasta", "verdura")[Random().nextInt(3)]
                        } else {
                            Arrays.asList("pizza", "pasta", "carne", "verdura")[Random().nextInt(4)]
                        }
                        val variables: MutableMap<String, Any> = HashMap()
                        variables["menu"] = menu
                        externalTaskService.complete(externalTask, variables)
                    } catch (e: Exception) {
                        externalTaskService.handleBpmnError(externalTask, "failed")
                    }
                }
                .open()
    }
}