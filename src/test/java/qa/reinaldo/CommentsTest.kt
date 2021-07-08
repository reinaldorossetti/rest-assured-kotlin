
package qa.reinaldo

import com.burakkaygusuz.tests.BaseTest
import io.restassured.RestAssured.post
import io.restassured.RestAssured.responseSpecification
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.apache.http.HttpStatus
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FeatureUsers : BaseTest() {

    @Test
    fun registerUser() {

         var response: String = Given {
            spec(requestSpecification)
            body(bodyUser())
            header("Header", "Header")
         } When {
            post("/usuarios", )
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("message")
        }
        assertEquals(response, "Cadastro realizado com sucesso")
    }

    @Test
    fun getAllUsers() {

        val displayName = Given {
            spec(requestSpecification)
        } When {
            get("/usuarios")
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            body().jsonPath().prettify()
        }
        print(displayName)
    }

}