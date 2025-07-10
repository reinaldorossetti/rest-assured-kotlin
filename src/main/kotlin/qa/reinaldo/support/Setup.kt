package qa.reinaldo.support

import com.google.gson.Gson
import qa.reinaldo.dados.UserCreated
import java.io.FileWriter
import java.io.IOException
import java.util.concurrent.TimeUnit
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.spring.commons.config.AsyncConfig.withTimeout
import java.io.File
import io.restassured.specification.RequestSpecification
import org.yaml.snakeyaml.Yaml

open class Setup {
    // pega o caminho do projeto
    val pathProject: String = System.getProperty("user.dir")
    private var userIDJson = File("$pathProject/src/test/kotlin/resources/userID.json")

    // pre-requisito global para os testes serem executados
    fun requestSpecification(): RequestSpecification {
        RestAssuredMockMvc.config = RestAssuredMockMvc.config().asyncConfig(withTimeout(20, TimeUnit.SECONDS))

        var url: String? = System.getProperty("url")
        if (url.isNullOrBlank()) {
            url = YamlConfig.getBaseUrl()
        }
        return RequestSpecBuilder()
            .setBaseUri(url)
            .addHeader("Accept", "application/json")
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .log(LogDetail.ALL)
            .build()
    }

    fun toJsonFile(value: String){
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
}

object YamlConfig {
    private val config: Map<String, Any>
    // pega o caminho do projeto
    val pathProject: String = System.getProperty("user.dir")

    init {
        // ler o arquivo json
        val inputStream = File("$pathProject/src/test/kotlin/resources/setup.yml").readText(Charsets.UTF_8)
        config = Yaml().load(inputStream)
    }

    fun getBaseUrl(): String {
        val api = config["api"] as Map<*, *>
        return api["base-url"] as String
    }
}