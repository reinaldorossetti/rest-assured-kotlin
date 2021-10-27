package qa.reinaldo.tests

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
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import qa.reinaldo.dados.UserCreated
import qa.reinaldo.dados.UserData
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.concurrent.TimeUnit


class CadastroBasicoTest {

    // pega o caminho do projeto
    val pathProject = System.getProperty("user.dir")
    // ler o arquivo json
    var cadastroJson = File("$pathProject/src/test/kotlin/resources/userData.json").readText(Charsets.UTF_8)
    var userIDJson = File("$pathProject/src/test/kotlin/resources/userID.json")

    // passa os dados de json para objetos para o kotlin ler.
    val cadastroDadosBody = Gson().fromJson(cadastroJson, UserData::class.java)
    // usando o Faker para gerar dados aleatorios.
    var faker = Faker()

    // pre-requisito global para os testes serem executados
    fun requestSpecification(): RequestSpecification {
        RestAssuredMockMvc.config = RestAssuredMockMvc.config().asyncConfig(withTimeout(20, TimeUnit.SECONDS))
        return RequestSpecBuilder()
            .setBaseUri("https://serverest.dev/")
            .addHeader("Accept", "application/json")
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .log(LogDetail.ALL)
            .build()
    }

    private fun toJsonFile(value: String){
        try {
            // transforma o json para objeto para se alterado o id.
            val userID: UserCreated = Gson().fromJson(userIDJson.readText(Charsets.UTF_8), UserCreated::class.java)
            userID._id = value
            // escreve os dados alterados para json.
            FileWriter(userIDJson).use { writer -> Gson().toJson(userID, writer) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Given > Contém pre-requisitos do meu teste, como URL, Header, Content Type, Aceita certificado invalido, e o Body.
     * When > Metodo HTTP que vai ser utilizado e o end point.
     * Then > Vai conter as validacoes\assertivas como retorno 201 e message igual ao valor esperado.
     * Extract > Vai extrair a informacao da message para ser usada no relatorio.
     */

    @Test
    @Order(0)
    fun ListarUsuariosCadastrados(){
        step("Realizando os testes de cadastro")
        cadastroDadosBody.email = faker.internet().emailAddress()
        cadastroDadosBody.nome = faker.name().fullName()
        val dados: Response =
            Given {
                spec(requestSpecification()); body(cadastroDadosBody)
            } When {
                get("/usuarios")
            } Then {
                assertThat().statusCode(200)
            } Extract {
                response()
            }
        println(dados)
        step("Response: $dados")
    }


    @Test
    @Order(1)
    fun cadastroDeUsuarioTest(){
        step("Realizando os testes de cadastro")
        cadastroDadosBody.email = faker.internet().emailAddress()
        cadastroDadosBody.nome = faker.name().fullName()
        val id: String =
            Given {
                spec(requestSpecification()); body(cadastroDadosBody)
            } When {
                post("/usuarios")
            } Then {
                assertThat().statusCode(201); status().is2xxSuccessful
                assertThat().body("message", equalTo("Cadastro realizado com sucesso"))
                assertThat().body("_id", notNullValue())
                assertThat().body("_id.length()", equalTo(16))
            } Extract {
                path("_id")
            }
        println(id)
        step("ID: $id")
        toJsonFile(id)
    }

    @Test
    @Order(2)
    fun cadastroDeUsuarioMessageEmailTest(){
        step("Realizando os testes de cadastro")
        val  response: Response =
            Given {
                spec(requestSpecification())
                body(cadastroDadosBody)
            } When {
                post("/usuarios")
            } Then {
                assertThat().statusCode(400)
                assertThat().body("message", equalTo("Este email já está sendo usado"))
                assertThat().body("message", containsString("email já está sendo usado"))
                assertThat().body("_id", emptyOrNullString())
            } Extract {
                response()
            }
        val message = response.path("message") as String
        step("message: $message"); step("response: ${response.print()}")
    }

}
