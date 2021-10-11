package qa.reinaldo._core.dados

data class UserData(
        var nome: String,
        var email: String,
        val password: String,
        val administrador: String,
)

data class UserCreated(
        var userID: String = "",
)