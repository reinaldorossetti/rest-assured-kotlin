package qa.reinaldo.tests

import com.github.javafaker.Faker
import com.google.gson.Gson
import io.qameta.allure.Allure.*
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel
import io.restassured.RestAssured.*
import io.restassured.module.jsv.JsonSchemaValidator.*
import io.restassured.module.kotlin.extensions.*
import io.restassured.response.Response
import org.hamcrest.Matchers.*
import org.json.JSONObject
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import qa.reinaldo.dados.UserData
import qa.reinaldo.support.Setup
import java.io.File
import java.nio.file.Files;
import java.nio.file.Paths;

class CadastroBasicoTest : Setup() {


    // ler o arquivo json
    private var cadastroJson = File("$pathProject/src/test/kotlin/resources/userData.json").readText(Charsets.UTF_8)
    // passa os dados de json para objetos para o kotlin ler.
    private val cadastroDadosBody: UserData = Gson().fromJson(cadastroJson, UserData::class.java)
    // usando o Faker para gerar dados aleatorios.
    private var faker = Faker()

    @Test
    @Order(1)
    @Severity(SeverityLevel.CRITICAL)
    fun cadastroDeUsuarioTest(){
        description("CT01 - Validar o cadastro realizado com sucesso - Teste Positivo")
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
        step("ID: $id")
        toJsonFile(id)
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.NORMAL)
    fun cadastroDeUsuarioMessageEmailTest(){
        description("CT02 - Validar mensagem de email já cadastrado - Teste Negativo")
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
    @Test
    @Order(3)
    @Severity(SeverityLevel.NORMAL)
    fun listar_novo_usuario_cadastrado(){
        description("CT03 - Realizar a listagem de usuários cadastros - Teste Positivo")
        cadastroDadosBody.email = faker.internet().emailAddress()
        cadastroDadosBody.nome = faker.name().fullName()
        val responseBody: String =
            Given {
                spec(requestSpecification()); body(cadastroDadosBody)
            } When {
                post("/usuarios")
            } Then {
                assertThat().statusCode(201); status().is2xxSuccessful
            } Extract {
                response().body().asString()
            }
        val jsonObject = JSONObject(responseBody)
        val dados: String =
            Given {
                spec(requestSpecification()); body(cadastroDadosBody)
            } When {
                get("/usuarios")
            } Then {
                assertThat().statusCode(200)
                assertThat().body(containsString("\"_id\": \"${jsonObject.getString("_id")}\""))
                assertThat().body(containsString("\"email\": \"${cadastroDadosBody.email}\""))
                assertThat().body(containsString("\"nome\": \"${cadastroDadosBody.nome}\""))
                assertThat().body(containsString("\"administrador\": \"${cadastroDadosBody.administrador}\""))
            } Extract {
                response().statusCode().toString()
            }
        step("Status Code:/n $dados")
    }

    @Test
    @Order(4)
    @Severity(SeverityLevel.MINOR)
    fun validar_json_schema_do_cadastrado(){
        description("CT03 - Realizar a listagem de usuários cadastros - Teste Positivo")
        cadastroDadosBody.email = faker.internet().emailAddress()
        cadastroDadosBody.nome = faker.name().fullName()

        // Lê o arquivo do schema como string
        val schema = String(Files.readAllBytes(Paths.get("src/test/kotlin/resources/cadasdro_schema.json")))

        val dados: String = Given {
            spec(requestSpecification()); body(cadastroDadosBody)
        } When {
            post("/usuarios")
        } Then {
            assertThat().statusCode(201); status().is2xxSuccessful
            body(matchesJsonSchema(schema))
        } Extract {
            response().statusCode().toString()
        }
        step("Status Code:/n $dados")
    }
}
