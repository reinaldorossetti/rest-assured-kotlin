# 🚀 rest-assured-kotlin

Projeto de automação de API utilizando **Rest Assured** e **JUnit5** com Kotlin.  
Este repositório demonstra como testar APIs REST de forma eficiente, utilizando boas práticas e recursos modernos do ecossistema Kotlin.

## ✨ Visão Geral

- Testes automatizados para a API [ServeRest](https://serverest.dev/)
- Cobertura dos principais verbos: **GET, POST, PUT, DELETE**
- Autenticação via header
- Testes com query string
- Validação de schema JSON
- Geração de dados dinâmicos com [Faker](https://github.com/DiUS/java-faker)
- Relatórios com [Allure](https://docs.qameta.io/allure/)

---

## 🛠️ Pré-requisitos

- Java JDK 11 ([Download](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html))
- [Maven](https://maven.apache.org/)
- [Node.js](https://nodejs.org/) (para rodar o ServeRest localmente)

---

## ⚡ Subindo o servidor ServeRest

Execute no terminal:

```sh
npx serverest@latest
```

---

## 🧪 Executando os testes

No terminal, rode:

```sh
./mvnw clean test
```

---

## 🧩 Sobre o módulo `kotlin-extensions` do Rest Assured

O **Rest Assured 4.1.0** introduziu o módulo `kotlin-extensions`, que fornece funções de extensão para tornar os testes mais idiomáticos e concisos em Kotlin.

### 📦 Adicionando a dependência

No seu `pom.xml`:

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>xml-path</artifactId>
    <version>5.4.0</version>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>spring-mock-mvc</artifactId>
    <version>5.4.0</version>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-path</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>kotlin-extensions</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
```

### 📝 Exemplo de uso passo a passo

Veja como fica um teste utilizando as extensões Kotlin do Rest Assured:

```kotlin
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo

@Test
fun cadastroDeUsuarioTest() {
    val user = UserData(
        nome = "João da Silva",
        email = "joao.silva@email.com",
        password = "123456",
        administrador = "true"
    )

    val id: String =
        Given {
            baseUri("http://localhost:3000/")
            contentType(ContentType.JSON)
            body(user)
        } When {
            post("/usuarios")
        } Then {
            statusCode(201)
            body("message", equalTo("Cadastro realizado com sucesso"))
        } Extract {
            path("_id")
        }
    println("ID do usuário cadastrado: $id")
}
```

**Vantagens do `kotlin-extensions`:**
- Sintaxe mais limpa e fluida
- Uso de lambdas para separar etapas Given/When/Then/Extract
- Integração perfeita com o padrão de escrita de testes do Kotlin

---

## 📊 Relatórios

Após a execução dos testes, gere o relatório Allure:

```sh
allure generate allure-results -o allure-report/
```

---

## 📚 Referências

- [Rest Assured Kotlin Extensions - Documentação Oficial](https://github.com/rest-assured/rest-assured/wiki/Usage#kotlin-extension-module)
- [ServeRest - API para estudos](https://serverest.dev/)
- [Allure Framework](https://docs.qameta.io/allure/)

---

## 👨‍💻 Autor

Reinaldo Rossetti  
[YouTube - Vídeo explicativo](https://www.youtube.com/watch?v=DfNLaGjjN4o)  
[Relatório no GitHub Pages](https://reinaldorossetti.github.io/rest-assured-kotlin/index.html#suites)

---

> ⭐️ Sinta-se à vontade para contribuir, abrir issues
