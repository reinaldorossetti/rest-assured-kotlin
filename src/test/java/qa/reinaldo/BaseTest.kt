package com.burakkaygusuz.tests

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import com.fasterxml.jackson.module.kotlin.*
import qa.reinaldo._core.dados.User
import qa.reinaldo._core.dados.UserCreated
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {

    companion object {
        lateinit var requestSpecification: RequestSpecification
    }
    val mapper = jacksonObjectMapper()
    val path = System.getProperty("user.dir")
    var userObject: User = mapper.readValue<User>(File("$path/src/test/java/resources/user.json"))
    var user =  UserCreated()

    fun bodyUser(): String {
        return mapper.writeValueAsString(userObject)
    }


    @BeforeAll
    fun setUp() {

        val logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
        val config = RestAssuredConfig.config().logConfig(logConfig)

        requestSpecification = RequestSpecBuilder()
            .setBaseUri("http://localhost:3000")
            .addHeader("Accept", "application/json")
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .setConfig(config)
            .build()
    }

    @AfterAll
    fun tearDown() {
        RestAssured.reset()
    }
}