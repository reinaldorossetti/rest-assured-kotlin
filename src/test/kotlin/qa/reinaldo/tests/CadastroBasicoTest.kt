package dev.serverest.tests

import com.github.javafaker.Faker
import com.google.gson.Gson
import io.qameta.allure.Allure.step
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import qa.reinaldo._core.dados.UserData
import java.io.File

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
        return RequestSpecBuilder()
            .setBaseUri("http://localhost:4200")
            .addHeader("Accept", "application/json")
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
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
                spec(requestSpecification());
                body(cadastroDadosBody)
            } When {
                post("/usuarios")
            } Then {
                statusCode(201)
                body("message", equalTo("Cadastro realizado com sucesso"))
            } Extract {
                path("message")
            }
        println(message)
        step("Message: $message")
    }

    @Test
    fun cadastroDeUsuarioMessageEmailTest(){
        step("Realizando os testes de cadastro")
        val message: String =
            Given {
                spec(requestSpecification());
                body(cadastroDadosBody)
            } When {
                post("/usuarios")
            } Then {
                statusCode(400)
                body("message", equalTo("Este email já está sendo usado"))
            } Extract {
                path("message")
            }
        println(message)
        step("Message: $message")
    }

}