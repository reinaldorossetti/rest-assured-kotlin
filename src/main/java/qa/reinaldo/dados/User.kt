package qa.reinaldo._core.dados

data class User(
    val nome: String,
    var email: String,
    //Dados Cartao
    val password: String,
    val administrador: String,
)

data class UserCreated(
        //id do usuario
        var userID: String = "",
)