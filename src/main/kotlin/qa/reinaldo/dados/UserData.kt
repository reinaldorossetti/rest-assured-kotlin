package qa.reinaldo.dados

data class UserData(
    val administrador: String,
    var email: String,
    var nome: String,
    val password: String
)

data class UserCreated(
    var _id: String
)