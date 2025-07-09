package qa.reinaldo.dados

data class UserData(
    var administrador: String = "true",
    var email: String,
    var nome: String,
    val password: String
)

data class UserCreated(
    var _id: String
)