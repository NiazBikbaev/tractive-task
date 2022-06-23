package com.tractive.task.controller

import com.tractive.task.core.AggregateProductsRequest
import com.tractive.task.core.PurchaseService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PurchaseController(private val purchaseService: PurchaseService) {

    @PostMapping(
        "/aggregate",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun aggregate(@RequestBody request: AggregateProductsRequest) = purchaseService.aggregateProducts(request)
}