# rest-assured-kotlin
Projeto de API com Rest Assured e Junit5. Nesse teste fiz um teste dependente de outro, o ideal que cada teste seja independente, mas é uma opção fazer partes separadas, fiz também porque tive pouco tempo pra elaborar o mesmo, reduzindo o código o máximo possível.

REST Assured 4.1.0 introduziu um novo módulo chamado "kotlin-extensions". Este módulo fornece algumas funções de extensão úteis ao trabalhar com REST Assured do Kotlin. Primeiro você precisa adicionar o módulo ao projeto:

O projeto foi criado com intuito de mostrar o ServeRest (https://serverest.dev/), que permite o estudo de:

- Verbos *GET, POST, PUT* e *DELETE* com persistência de dados
- Autenticação no header
- Query string
- Teste de schema json


### Para subir o servido localmente com NPM

Execute o seguinte comando no terminal:  

```sh
npx serverest@latest
```

references:

https://github.com/rest-assured/rest-assured/wiki/Usage#kotlin-extension-module