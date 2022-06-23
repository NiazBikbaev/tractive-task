package com.tractive.task.core

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PurchaseService {

    fun aggregateProducts(request: AggregateProductsRequest): List<ProductAggregation> {
        logger.debug { "Aggregate products request: request=$request" }

        if (request.products.isEmpty()) {
            logger.info { "Purchased products list is empty: request=$request" }
            return emptyList()
        }

        return with(request) {
            products
                .groupingBy { it }
                .eachCount()
                .mapNotNull { (product, count) -> buildAggregation(product, count) }
        }.also {
            logger.info { "Aggregate products response: request=$request, response=${it.hide(3)}" }
        }
    }

    private fun AggregateProductsRequest.buildAggregation(product: String, count: Int): ProductAggregation? {
        val description = productDescription[product] ?: return null

        return ProductAggregation(
            edition = description.edition,
            version = description.version,
            quantity = count,
        )
    }

    private companion object {
        val logger = KotlinLogging.logger { }
    }
}