package com.tractive.task

import com.tractive.task.core.AggregateProductsRequest
import com.tractive.task.core.ProductAggregation
import com.tractive.task.core.ProductDescription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.assertj.core.api.Assertions as AssertJ

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AggregateProductsTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @ParameterizedTest
    @MethodSource("productsDataProvider")
    fun `should aggregate products`(request: AggregateProductsRequest, expected: List<ProductAggregation>) {
        // given:
        // when:
        val response = makeRequest(request)

        // then:
        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)

        AssertJ.assertThat(response.body ?: emptyList())
            .containsExactlyInAnyOrderElementsOf(expected)
    }

    private fun makeRequest(request: AggregateProductsRequest) =
        testRestTemplate.exchange("/aggregate", HttpMethod.POST, HttpEntity(request), RESPONSE_TYPE)

    private companion object {

        val RESPONSE_TYPE = object : ParameterizedTypeReference<List<ProductAggregation>>() {}

        val DESCRIPTIONS = mapOf(
            "DDDF" to ProductDescription(edition = null, version = 1),
            "CVCD" to ProductDescription(edition = "X", version = 1),
            "SDFD" to ProductDescription(edition = "Z", version = 1),
        )

        @JvmStatic
        fun productsDataProvider() = listOf(
            Arguments.of(
                request(listOf("CVCD", "SDFD", "DDDF", "SDFD")),
                listOf(
                    aggregation("CVCD", 1),
                    aggregation("SDFD", 2),
                    aggregation("DDDF", 1)
                )
            ),
            Arguments.of(
                request(listOf("CVCD", "SDFD", "DDDF", "SDFD", "AAAA")),
                listOf(
                    aggregation("CVCD", 1),
                    aggregation("SDFD", 2),
                    aggregation("DDDF", 1)
                )
            ),
            Arguments.of(request(emptyList()), emptyList<ProductAggregation>())
        )

        fun request(products: List<String>) =
            AggregateProductsRequest(
                productDescription = DESCRIPTIONS,
                products = products
            )

        fun aggregation(product: String, quantity: Int) =
            ProductAggregation(
                edition = DESCRIPTIONS[product]?.edition,
                quantity = quantity,
                version = 1
            )
    }
}
