
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
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class FeatureUsers : BaseTest() {

    @Test
    @Order(1)
    fun registerUser() {
         var response: String = Given {
            spec(requestSpecification); body(bodyUser()); header("Header", "Header")
         } When { post("/usuarios", ) } Then { statusCode(HttpStatus.SC_CREATED)
             body("message", equalTo("Cadastro realizado com sucesso"))
         } Extract {
             path("_id")
        }
        user.userID = response
        println(response)
    }

    @Test
    @Order(2)
    fun getUserCreated() {
        val displayName = Given { spec(requestSpecification) } When { get("/usuarios/${user.userID}") } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract { body().jsonPath().prettify() }
        print(displayName)
    }

    @Test
    @Order(3)
    fun getAllUsers() {
        val displayName = Given { spec(requestSpecification) } When { get("/usuarios") } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract { body().jsonPath().prettify() }
        print(displayName)
    }

}