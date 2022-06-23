package com.tractive.task.core

data class AggregateProductsRequest(
    val productDescription: Map<String, ProductDescription>,
    val products: List<String>,
) {
    override fun toString(): String {
        return """
            AggregateProductsRequest(productDescription=${productDescription.hide(3)}, products=${products.hide(3)})
        """.trimIndent()
    }
}

data class ProductDescription(
    val edition: String?,
    val version: Int,
)
