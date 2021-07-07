package qa.reinaldo._core.dados

data class User(
    //id do usuario
    val idDoUsuario: String = "gabriel",
    val senha: String = "123Mud@r",
    var userInvalido: String = "",
    //Dados Cartao
    val numeroCartao: String = "5381579886310193",
    val dataValidade: String = "03/23",
    val cvc: String = "235",
)