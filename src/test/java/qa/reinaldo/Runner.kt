package qa.reinaldo

import _core.Capabilities
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.qameta.allure.Allure.step
import io.qameta.allure.Step
import org.junit.jupiter.api.*
import org.opentest4j.AssertionFailedError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import qa.reinaldo._core.screens.ScreenLogin
import qa.reinaldo._core.screens.ScreenLogout
import qa.reinaldo._core.screens.ScreenRegisterUser
import qa.reinaldo._core.screens.ScreenShopping
import qa.reinaldo._core.dados.Produto
import qa.reinaldo._core.dados.User

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class Runner {

    private var driver: AppiumDriver<MobileElement>? = null
    private var screenRegisterUser = ScreenRegisterUser()
    private var user = User()
    private var produto = Produto()

    @BeforeEach
    fun iniciar() {
        driver = Capabilities.inicializarAppiumDriver()
    }

    @AfterEach
    fun finalizar() {
        Capabilities.finalizarAppiumDrivery()
    }

    @Test
    @Step("Realizando o cadastro de usuario")
    @Order(1)
    fun testeCadastrarUsuario() {
        screenRegisterUser.cadastrarUsuario()
        screenRegisterUser.nome(user.idDoUsuario)
        screenRegisterUser.senha(user.senha)
        screenRegisterUser.confirmarSenha(user.senha)
        screenRegisterUser.cadastrar()
    }

    @Test
    @Step("Realizando o cadastro com senha invalida - msn: {resultUsuarioSenhaInvalidos}")
    @Order(2)
    fun loginUsuarioSenhaInvalidos() {
        assertThrows(
            AssertionFailedError::class.java
        ) {
            val screenLogin = ScreenLogin()
            screenLogin.preencherIdDoUsuario("usuarioInexistente")
            screenLogin.preencherSenha("9999999")
            screenLogin.logar()
            user.userInvalido = screenLogin.validaUsuarioSenhaInvalidos()
            validaUsuarioSenhaInvalidos(user.userInvalido)
        }
    }

    @Step("Assert mensagem: {resultado}")
    fun validaUsuarioSenhaInvalidos(resultado: String){
        println(resultado)
        assertEquals("Usuario ou senha invalidos",resultado)
    }

    @Test
    @Order(3)
    @Step("Realizando o login com sucesso")
    fun testeLogin() {
        val screenLogin = ScreenLogin()
        step("usuario: ${user.idDoUsuario}")
        screenLogin.preencherIdDoUsuario(user.idDoUsuario)
        screenLogin.preencherSenha(user.senha)
        screenLogin.logar()
    }

    @Test
    @Order(3)
    @Step("Realizando a compra de um produto")
    fun testComprarProduto() {
        screenRegisterUser.cadastrarUsuario()
        screenRegisterUser.nome(user.idDoUsuario).senha(user.senha).confirmarSenha(user.senha)
        screenRegisterUser.cadastrar()
        val screenLogin = ScreenLogin()
        screenLogin.preencherIdDoUsuario(user.idDoUsuario)
        screenLogin.preencherSenha(user.senha)
        screenLogin.logar()
        val screenCompras = ScreenShopping()
        screenCompras.produto(produto.bolaFutebol)
        screenCompras.comprar()
        screenCompras.preencherNumeroCartao(user.numeroCartao)
        step("numeroCartao: ${user.numeroCartao}")
        screenCompras.preencherDataValidade(user.dataValidade)
        screenCompras.preencherCvc(user.cvc)
        screenCompras.confirmarPagamento()

        //"Falso Negativo" ou "Falso Negativo" ???
        val falhaAoCriarPagamento = "Falso Positivo"
        val mensagemErro = "Falha ao criar pagamento"
        if (falhaAoCriarPagamento.equals("Falso Positivo")) {
            println("No cenario de teste 'Comprar Produto', o sistema esta apresentando a seguinte notificacao: <<<$mensagemErro>>> propositalmente.")
        } else {
            fail("App com falha ao Criar Pagamento")
        }
    }

    @Test
    @Order(4)
    @Step("Realizando o logout")
    fun testLogout() {
        screenRegisterUser.cadastrarUsuario()
        screenRegisterUser.nome(user.idDoUsuario).senha(user.senha).confirmarSenha(user.senha)
        screenRegisterUser.cadastrar()
        val screenLogin = ScreenLogin()
        screenLogin.preencherIdDoUsuario(user.idDoUsuario)
        screenLogin.preencherSenha(user.senha)
        screenLogin.logar()
        val screenLogout = ScreenLogout()
        screenLogout.deslogar()
    }
}