package dev.serverest.tests

import com.github.javafaker.Faker
import com.google.gson.Gson
import io.qameta.allure.Allure.step
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.spring.commons.config.AsyncConfig.withTimeout
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import qa.reinaldo._core.dados.UserData
import java.io.File
import java.util.concurrent.TimeUnit

class CadastroBasicoTest {

    // pega o caminho do projeto
    val pathProject = System.getProperty("user.dir")
    // ler o arquivo json
    var cadastroJson = File("$pathProject/src/test/kotlin/resources/userData.json").readText(Charsets.UTF_8)
    // passa os dados de json para objetos para o kotlin ler.
    val cadastroDadosBody = Gson().fromJson(cadastroJson, UserData::class.java)
    // usando o Faker para gerar dados aleatorios.
    var faker = Faker()

    // pre-requisito global para os testes serem executados
    fun requestSpecification(): RequestSpecification {
        RestAssuredMockMvc.config = RestAssuredMockMvc.config().asyncConfig(withTimeout(20, TimeUnit.SECONDS))
        return RequestSpecBuilder()
            .setBaseUri("http://localhost:4200")
            .addHeader("Accept", "application/json")
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .log(LogDetail.ALL)
            .build()
    }

    /**
     * Given > Contém pre-requisitos do meu teste, como URL, Header, Content Type, Aceita certificado invalido, e o Body.
     * When > Metodo HTTP que vai ser utilizado e o end point.
     * Then > Vai conter as validacoes\assertivas como retorno 201 e message igual ao valor esperado.
     * Extract > Vai extrair a informacao da message para ser usada no relatorio.
     */

    @Test
    fun cadastroDeUsuarioTest(){
        step("Realizando os testes de cadastro")
        cadastroDadosBody.email = faker.internet().emailAddress()
        val message: String =
            Given {
                spec(requestSpecification())
                body(cadastroDadosBody)
            } When {
                post("/usuarios")
            } Then {
                statusCode(201)
                status().is2xxSuccessful
                body("message", equalTo("Cadastro realizado com sucesso"))
                body("_id", notNullValue())
                body("_id.length()", equalTo(16))
            } Extract {
                path("message")
            }
        println(message)
        step("Message: $message")
    }

    @Test
    fun cadastroDeUsuarioMessageEmailTest(){
        step("Realizando os testes de cadastro")
        val  response: Response =
            Given {
                spec(requestSpecification())
                body(cadastroDadosBody)
                log().ifValidationFails()
            } When {
                post("/usuarios")
            } Then {
                statusCode(400)
                body("message", equalTo("Este email já está sendo usado"))
                body("message", containsString("email já está sendo usado"))
                body("_id", emptyOrNullString())
            } Extract {
                response()
            }
        val message = response.path("message") as String
        step("message: $message")
    }

}