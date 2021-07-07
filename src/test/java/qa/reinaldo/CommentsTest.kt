@file:Suppress("ClassName")

package qa.reinaldo

import com.burakkaygusuz.tests.BaseTest
import io.restassured.RestAssured.post
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

class `Comments Test` : BaseTest() {

    @Test
    fun `get all comments by blog Id and post Id`() {

        Given {
            spec(requestSpecification)
        } When {
            post('/usuarios')
        } Then {
            statusCode(HttpStatus.SC_OK)
        }
    }

    @Test
    fun `get single comment`() {

        val displayName: String = Given {
            spec(requestSpecification)
        } When {
            get("/blogs/2399953/posts/5310628572012276714/comments/6352433676268819946")
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            path("author.displayName")
        }

        assertThat(displayName, equalTo("Elizabeth Keene"))
    }

}