# Kotlin

**Principais Características:**

- Pode ser compilado em **bytecode JVM**.
- É **interoperável com Java**.
- É **seguro** e **conciso**.
- Permite **integração no processo de compilação**.
- Foca em **segurança contra null**, **funções de extensão**, **data classes** e **coroutines**.

**Fluxo:** `Código Kotlin → Compilador Kotlin → Bytecode JVM → Executa na JVM.`

---

## **Classes**

Uma classe é um **modelo** para criar objetos.
Um objeto é uma **instância** de uma classe, combinando **atributos** (propriedades) e **comportamento** (funções/métodos).

### Normal class

Representa entidades ou objetos do mundo real com comportamento **mutável ou complexo**.

- Pode conter **propriedades e funções**
- Pode ter **estado interno** (mutável ou imutável)
- Pode ser **instanciada múltiplas vezes**

```kotlin
class Car(val brand: String, var speed: Int) {
    fun accelerate() {
        speed += 10
    }
}
```

### Data class

Usado quando a classe serve principalmente para **armazenar e comparar dados**.

- Utilizar quando a classe contém principalmente dados
- O Kotlin sobrepõe três funções importantes: `equals()`, `hashCode()` e `toString()`
- A operação `==` compara com base no **conteúdo** (não pela referência)

```kotlin
data class Person(val name: String, val email: String)
```

### Object class (Singleton)

Usado quando se necessita de **uma instância global**, como para funções utilitárias.

- Não pode ser instanciado. Existe apenas **uma instância** em todo o programa

```kotlin
object DateUtil {
    fun format(dateTime: ZoneDateTime): String {
        return "..."
    }
}
```

### Data object

Usado quando é necessário um **singleton** que **suporta comparação baseada no conteúdo**.

- `==` compara pelo **conteúdo** (não pela referência)
- Útil para **hierarquias seladas** e quando se precisa de `equals`/`hashCode`

```kotlin
data object DateUtil {
    fun format(dateTime: ZoneDateTime): String {
        return "..."
    }
}
```

### Enum class

Usado quando se tem um **conjunto limitado de valores conhecidos**.

- Utilizado para representar um conjunto fixo de constantes
- Cada constante pode ter **propriedades e métodos**
- Constantes conhecidas em tempo de compilação
- Iterável: `for (status in HttpStatus.values()) { ... }`

```kotlin
enum class HttpStatus(val code: Int, val msg: String){
    OK(200. "Ok"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not found");

    fun toResponseString(): String {
        return "Error $code: $msg"
    }
}
```

### Sealed class

Usado quando **todas as subclasses são conhecidas** e se pretende usar **`when` exaustivo**.

- Representa hierarquias de classes restritas
- Todas as subclasses são conhecidas em tempo de compilação
- Ótimo para representar **estado** ou **tipos de resultado**
- Normalmente usado com **data classes**
- Semelhante a classes **Enum**, mas permite ter **instâncias individuais de cada subclasse**
- Pode conter **dados diferentes em cada instância**
- Se não houver necessidade de guardar informação, não se utiliza construtor (tem apenas valores associados), pelo que se usará uma classe **object**

```kotlin
sealed class NetworkResult {
    data class Success(val data: String): NetworkResult()
    data class Error(val throwable: Throwable): NetworkResult()
    data object Empty:  NetworkResult()
}

// Used like this
fun handle(result: NetworkResult) = when (result) {
    is NetworkResult.Success -> println("Data: ${result.data}")
    is NetworkResult.Error -> println("Error: ${result.throwable.message}")
    NetworkResult.Empty -> println("Empty result")
}
```

### Abstract class

Usado para definir **estrutura ou comportamento comum** para subclasses, mas sem instância própria.

- Define comportamento, mas não existe um objeto específico associado (a subclass implementará o comportamento)
- Não é possível criar instâncias da classe abstrata, apenas das suas subclasses

```kotlin
abstract class Sensor {
    override val name: String
    abstract fun startListening(onNewValue: (Float) -> Unit)
}

class HeartRateSensor: Sensor() {
    override val name: String
        get() = "Heart rate tracker"

    override startListening(onNewValue: (Float) -> Unit){
        // ...
    }
}
```

### Open class

Usado quando se quer que uma classe seja **extensível para personalização**.

- Por defeito, as classes em Kotlin são **finais** (não podem ser herdadas)
- Permite estender uma implementação padrão, sobrepondo-a na sua própria implementação

```kotlin
abstract class Sensor {
    override val name: String
    abstract fun startListening(onNewValue: (Float) -> Unit)
}

open class HeartRateSensor: Sensor() {
    override val name: String
        get() = "Heart rate tracker"

    override startListening(onNewValue: (Float) -> Unit){
        // ...
    }
}

class CustomHeartRateSensor: HeartRateSensor() {
    override val name: String
        get() = "Costume heart rate tracker"
}
```

### Value class

Usado ao criar **wrappers type-safe** em torno de tipos primitivos.

- Sem sobrecarga adicional em tempo de execução
- Funciona como uma **data class de alto nível**
- Ajuda a criar wrappers type-safe (ex.: `UserId`, `Password`, etc.)

```kotlin
@JvmInline
value class Email(val email: String) {
    init {
        if(!email.contains("@")) {
            throw IllegalArgumentException("Invalid email!")
        }
    }
}
```

### Inner class

Usado quando uma classe precisa de acesso direto aos membros da classe exterior.

- Definida dentro de uma classe
- Permite acesso total às variáveis dessa classe

```kotlin
class Engine {
    inner class Piston {
        fun start() {
            println("Piston in engine running!")
        }
    }
}
```

### 📞 Callback Example

```kotlin
sealed class OperationResult {
    data class Success(val user: User) : OperationResult()
    data class Error(val exception: Throwable) : OperationResult()
    object Loading : OperationResult()
}

interface UserCallback {
    fun onResult(result: OperationResult)
}

class UserRepository(private val api: ApiService) {
    fun fetchUser(userId: Int, callback: UserCallback): Job {
      return CoroutineScope(Dispatchers.IO).launch {
            callback.onResult(OperationResult.Loading)

            try {
                val user = api.getUser(userId)
                callback.onResult(OperationResult.Success(user))
            } catch (e: Exception) {
                callback.onResult(OperationResult.Error(e))
            }
        }
    }
}

fun main() = runBlocking {
    val repo = UserRepository(ApiService())

    val callback = object : UserCallback {
        override fun onResult(result: OperationResult) {
            when (result) {
                is OperationResult.Loading -> println("Loading user...")
                is OperationResult.Success -> {
                    val u = result.user
                    println("User loaded!")
                }
                is OperationResult.Error -> {
                    println("Error loading user: ${result.exception.message}")
                }
            }
        }
    }

    val job = repo.fetchUser(10, callback)
    job.await()
}
```

---

## **Android Platform** 📱

```scss
                    ┌─────────────┐
                    │   Launcher  │  ← O utilizador toca no ícone da app
                    └──────┬──────┘
                           │
                           ▼
                     ┌──────────┐
                     │ Activity │  ← UI, ecrã, interage com o utilizador
                     └────┬─────┘
             ┌────────────┼─────────────┐
             │            │             │
             ▼            ▼             ▼
         Service        Intent     ContentProvider
    (Background Task)  (Message)   (Data Sharing)
             │            │             │
             └────────────┴─────────────┘
                          │
                          ▼
                   BroadcastReceiver
          (Escuta eventos do sistema ou da app)
```

- **Intent** é a cola entre os componentes: usada para iniciar Activities, Services e disparar Broadcasts.
- **Context** é a ponte usada por todos os componentes para aceder a recursos do sistema.
- **Application** é a raiz do processo da tua app, criada pelo sistema através do **Zygote**.

| Conceito                         | Função / Utilização                                | Notas                                                                       |
| -------------------------------- | -------------------------------------------------- | --------------------------------------------------------------------------- |
| **Context**                      | Ligação ao sistema, recursos e serviços            | Activity Context → UI; Application Context → global, não UI                 |
| **Application**                  | Classe raiz do ciclo de vida da app                | Executa antes de qualquer componente; usado para inicializar bibliotecas    |
| **Activity**                     | Um ecrã / UI                                       | Ciclo de vida: onCreate → onStart → onResume → onPause → onStop → onDestroy |
| **Intent**                       | Mensagem para executar ação                        | Explícita → direciona componente; Implícita → descreve ação/dados           |
| **Service**                      | Processamento em background                        | Started, Bound, ou Foreground; funciona sem UI                              |
| **BroadcastReceiver**            | Escuta eventos do sistema/app                      | Curto prazo; pode disparar serviços ou ações                                |
| **ContentProvider**              | Partilha dados estruturados                        | Operações CRUD via URIs; permite acesso a dados entre apps                  |
| **AMS (ActivityManagerService)** | Serviço do sistema que gere processos & Activities | Controla o ciclo de vida das apps, tarefas, back stack                      |
| **Zygote**                       | Processo pré-carregado que clona apps              | Acelera a criação de processos para novas apps                              |

O que uma Activity pode fazer:

- Iniciar um **Service** (trabalho em background)
- Enviar **Intents** (lançar outras Activities/apps)
- Aceder a um **ContentProvider** (dados)

O que um BroadcastReceiver pode fazer:

- Disparar **Services** ou notificar **Activities**.

### 1. Services 🔧

| Tipo de Service        | Descrição                                           | Exemplo                              |
| ---------------------- | --------------------------------------------------- | ------------------------------------ |
| **Started Service**    | Iniciado via `startService()`; corre até ser parado | Leitor de música a tocar             |
| **Bound Service**      | Permite ligação e interação com outros componentes  | App de mensagens a sincronizar dados |
| **Foreground Service** | Corre com prioridade mais alta e notificação        | GPS tracking, download de ficheiros  |

**Pontos-chave:**

- Corre no **main thread** por defeito → tarefas pesadas devem usar threads ou corrotinas.
- Funciona independentemente da UI — a Activity pode ser destruída e o Service continua.

```kotlin
val serviceIntent = Intent(this, MyService::class.java)
startService(serviceIntent)
```

### 2. Broadcast Receivers 📡

Um **BroadcastReceiver** é um ouvinte de eventos do sistema ou específicos da app.

**Exemplos de broadcasts:**

- `Intent.ACTION_BATTERY_LOW`
- `Intent.ACTION_BOOT_COMPLETED`
- Eventos personalizados da app ("com.myapp.USER_LOGGED_IN")

**Pontos-chave:**

- Curta duração: apenas activo alguns milissegundos; não pode executar tarefas longas.
- Normalmente usado para disparar um **Service** para tarefas mais longas.
- Pode ser registado de duas formas:
  - Estático (Manifest): sempre ativo, mesmo com a app fechada. Ideal para eventos do sistema.
  - Dinâmico (código): ativo apenas enquanto a Activity ou Service está em execução. Ideal para eventos internos da app.

```kotlin
class SimpleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> "⚠️ Bateria fraca!"
            Intent.ACTION_BATTERY_OKAY -> "🔋 Bateria normal"
            "com.myapp.USER_LOGGED_IN" -> "👋 Bem-vindo ${intent.getStringExtra("username") ?: ""}"
            "com.myapp.NEW_MESSAGE" -> "📩 Nova mensagem de ${intent.getStringExtra("sender") ?: ""}"
            else -> return
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

// Disparar evento personalizado
val loginIntent = Intent("com.myapp.USER_LOGGED_IN").apply {
    putExtra("username", "João")
}
sendBroadcast(loginIntent)

// Ou Registo no Manifest (estático):
<receiver android:name=".SimpleReceiver">
    <intent-filter>
        <action android:name="com.myapp.USER_LOGGED_IN"/>
        <action android:name="com.myapp.NEW_MESSAGE"/>
        <action android:name="android.intent.action.BATTERY_LOW"/>
        <action android:name="android.intent.action.BATTERY_OKAY"/>
    </intent-filter>
</receiver>
```

### 3. Content Providers 🗂️

Um **ContentProvider** permite partilhar dados estruturados de forma segura com outras apps.

**Exemplos:**

- Contactos (ContactsContract)
- Media (imagens, vídeos)
- Bases de dados personalizadas da app

**Pontos-chave:**

- Fornece operações CRUD (query, insert, update, delete).
- Ideal para partilha de dados entre apps.

```kotlin
val resolver = context.contentResolver
val uri = Uri.parse("content://com.myapp.provider/users/1")

// Ler dados
val cursor = resolver.query(uri, null, null, null, null)
cursor?.use {
    if (it.moveToFirst()) {
        val name = it.getString(it.getColumnIndex("name"))
        Log.d("ContentProvider", "Nome do utilizador: $name")
    }
}

// Inserir dados
val values = ContentValues().apply {
    put("name", "João")
    put("email", "joao@email.com")
}
resolver.insert(Uri.parse("content://com.myapp.provider/users"), values)
```

- Acesso feito via **ContentResolver**.

### 4. Android Permissions, Stubs and IPC 🧩

#### Stubs e IPC (Inter-Process Communication)

- O **Stub** faz parte do mecanismo **AIDL/Binder IPC**, usado para comunicação entre processos (apps ↔ serviços do sistema).
- Define-se uma interface `.aidl`; o Android gera automaticamente uma **Stub class**, que o serviço estende.
- O Stub faz a ponte entre cliente e serviço: **recebe chamadas remotas, desembrulha os dados (unmarshal)** e executa o método correspondente.
- O programador apenas implementa a lógica, porque a comunicação de baixo nível é gerida pelo Binder.

#### Sistema de Permissões no Android

- As permissões são **definidas no Android Framework** (em `AndroidManifest.xml` do sistema).
- Cada permissão tem um **protection level**: `normal`, `dangerous` ou `signature`.
- Os apps **não criam novas permissões**; apenas **declaram** as que precisam com `<uses-permission>`.

> `normal` → concedida automaticamente.
> `dangerous` → o utilizador deve aprovar em runtime.
>
> Quando uma permissão é concedida, o **Activity Manager / Permission Controller** atualiza o banco de dados do sistema marcando o UID do app como autorizado.

#### Package Manager Service (PMS)

- Serviço central do framework AOSP.
- Gere instalação, atualização e remoção de apps.
- Lê o manifest do app e **regista as permissões pedidas**, mas **não as concede ainda**.
- Também aplica controlos de acesso a nível de sistema.

#### Verificação de Permissões e Identidade (UID)

- Quando um app chama uma API protegida, a requisição passa pelo **Binder IPC** até o **system service** (ex: `CameraService`).
- O **Stub** no lado do serviço verifica, via **ActivityManagerService** ou **PackageManagerService**, se o UID do app tem a permissão necessária.
- Cada app tem um **UID único Linux**, usado para identificar a origem de cada chamada.
- Se o UID tem a permissão → operação continua. Caso contrário → `SecurityException`.

#### 🔄 Fluxo de Permissão Simplificado

1. Permissões definidas no framework (manifest do sistema).
2. App declara permissões no próprio manifest.
3. PMS regista os pedidos durante a instalação.
4. Utilizador aprova (ou não) permissões “dangerous”.
5. App faz chamada via Binder Stub.
6. Serviço verifica permissões do UID.
7. Se permitido → executa; senão → lança `SecurityException`.

---

## **Coroutines**

Funções que podem pausar a sua execução e retomar mais tarde, permitindo programação assíncrona.

- **Código suspenso (suspending code):** Pausa temporariamente a coroutine, libertando a thread para realizar outro trabalho. → Uso eficiente dos recursos (não bloqueante).
- **Código bloqueante (blocking code):** Para toda a thread até a operação terminar. → A thread não pode fazer mais nada durante esse tempo.

As coroutines usam **continuations**, que representam o ponto onde a execução deve ser retomada. Quando uma coroutine é suspensa, o seu estado é guardado (variáveis, posição na pilha de chamadas, etc.). Quando retomada, a continuação restaura esse estado, permitindo que a execução continue sem interrupções.

### 1. Coroutine Scopes

| **Scope**            | **Duração**                                                          | **Cancelado Quando**                                                          | **Caso de Uso Típico**                                                                           |
| -------------------- | -------------------------------------------------------------------- | ----------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ |
| **`GlobalScope`**    | Enquanto o processo da app estiver ativo ou até a coroutine terminar | Apenas quando cancelado manualmente ou quando o processo da app termina       | Tarefas em background que devem continuar independentemente do estado da UI (usar com moderação) |
| **`LifecycleScope`** | Enquanto o **Activity** ou **Fragment** estiver ativo                | Quando o **Activity**/**Fragment** é destruído (ex.: mudança de configuração) | Trabalho ligado à UI que deve parar quando o ecrã é destruído                                    |
| **`ViewModelScope`** | Enquanto o **ViewModel** existir                                     | Quando o **ViewModel** é limpo (ex.: ecrã removido do back stack)             | Tarefas de longa duração ligadas ao estado da UI ou carregamento de dados no **ViewModel**       |

> Numa `customLifecycleScope = CoroutineScope(Dispatchers.Main)` devemos cancelá-la sobrescrevendo o método `onDestroy()`.

```kotlin
// --- GlobalScope use ---
val job = GlobalScope.launch {
    // ...
}
job.cancel()

// --- LifecycleScope use ---
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) { // in an activity
        println("A correr enquanto a Activity está STARTED")
    }
}

viewLifecycleOwner.lifecycleScope.launch { // in a fragment
    delay(1000)
    println("LifecycleScope no Fragment")
}

// --- viewModelScope use ---
viewModelScope.launch { // inside a ViewModel
    delay(2000)
    data.value = "dados carregados"
}
```

### 2. Jobs & Deferreds

#### Jobs

Útil para tarefas que correm em segundo plano que não se requer um resultado.

- Permite iniciar, cancelar ou verificar o estado de uma coroutine, nunca a bloqueando
- Pode ser usado para gerir uma hierarquia de coroutines (relações pai/filho)

```kotlin
val job: Job = CoroutineScope(Dispatchers.Default).launch {
    repeat(5) { i ->
        println("Task $i running...")
        delay(500)
    }
}

job.cancel() // Cancels the coroutine if needed
```

| **Operação**                 | **Descrição / Comportamento**                                                                                               |
| ---------------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| **`join()`**                 | **Suspende a coroutine atual** até que este Job seja concluído. Espera pelo fim do Job sem bloquear a thread.               |
| **`cancel()`**               | **Cancela o Job**, interrompendo a sua execução. Coroutines filhas também são canceladas. Pode passar uma razão (opcional). |
| **`isCompleted`**            | **Verifica se o Job terminou**, seja normalmente ou por cancelamento. Retorna `true` se estiver concluído.                  |
| **`isActive`**               | **Verifica se o Job ainda está a correr**. Retorna `true` se não terminou nem foi cancelado.                                |
| **`isCancelled`**            | **Verifica se o Job foi cancelado**. Retorna `true` se `cancel()` foi chamado.                                              |
| **`invokeOnCompletion { }`** | Regista um **callback** a ser chamado quando o Job terminar ou for cancelado.                                               |

#### Deferred

Usado quando uma coroutine produz um valor de que precisa para fazer algo depois.

- Especie de **Job** que retorna um resultado. Pode ser visto como um **future/promise**
- Bloqueia a coroutina com o uso do await(), obetendo o resultado de forma assíncrona

```kotlin
val deferred: Deferred<Int> = CoroutineScope(Dispatchers.Default).async {
    delay(1000)
    42 // result
}

runBlocking {
    val result = deferred.await() // suspend until result is ready
    println("Result is $result")
}
```

### 3. Coroutine Contexts

Um Coroutine Context define toda a informação que uma coroutine precisa para ser executada.

- **Job**: Gere o ciclo de vida da coroutine (início, cancelamento, conclusão)
- **CoroutineDispatcher**: Decide qual thread ou pool de threads executa a coroutine
- **CoroutineName**: Útil para depuração/registo (debugging/logging)
- **CoroutineExceptionHandler**: Trata exceções não capturadas

```kotlin
CoroutineScope(Dispatchers.IO + CoroutineName("MyCoroutine")).launch() {
    println(coroutineContext)
}
```

#### withContext

Muda temporariamente o contexto da coroutine dentro de uma função suspendida. Normalmente usado para alternar entre threads, por exemplo, de IO para Main.

```kotlin
suspend fun loadData() {
    val data = withContext(Dispatchers.IO) { fetchFromNetwork() }
    withContext(Dispatchers.Main) { showData(data) }
}
```

| Dispatcher         | Tipo de Trabalho                                | Comportamento de Thread                                                 | Exemplo                                                           |
| ------------------ | ----------------------------------------------- | ----------------------------------------------------------------------- | ----------------------------------------------------------------- |
| **Main**           | Trabalho de UI                                  | Executa na **thread principal**                                         | `launch(Dispatchers.Main) { textView.text = "Hello" }`            |
| **Main.immediate** | Trabalho de UI otimizado                        | Executa **imediatamente** se já estiver na main thread                  | `launch(Dispatchers.Main.immediate) { updateUI() }`               |
| **IO**             | Operações de entrada/saída (rede, arquivos, DB) | Pool de threads compartilhado, escalável para múltiplas operações de IO | `launch(Dispatchers.IO) { val data = fetchFromNetwork() }`        |
| **Default**        | Tarefas CPU-intensivas                          | Pool de threads igual ao número de núcleos do CPU                       | `launch(Dispatchers.Default) { val result = computeLargeData() }` |

### 4. Coroutines in Compose

#### LaunchEffect ✨

É uma função de **efeito secundário** que executa um bloco de código suspenso numa coroutine ligada ao ciclo de vida de um Composable.

- Executa-se uma vez quando a(s) chave(s) mudam.
- Cancela e reinicia automaticamente se a chave mudar.
- A coroutine é cancelada se o Composable sair da composição (por exemplo, se o utilizador navegar para outro ecrã).

```Kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    val data = viewModel.data.collectAsState()

    LaunchedEffect(data.value) { // key = data.value
        println("Data changed: ${data.value}")
        // Suspend functions allowed here
        delay(1000)
    }

    Text("Data: ${data.value}")
}
```

#### produceState 🏗

É uma função Composable que cria um objeto **State** cujo valor é atualizado a partir de uma coroutine.

- Lança uma coroutine internamente.
- Retorna um **State<T>** que pode ser usado diretamente na UI.
- Cancela automaticamente a coroutine quando o Composable sai da composição.

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    val data: State<List<User>> = produceState(initialValue = emptyList()) {
        value = viewModel.loadUsers() // suspend function
    }

    LazyColumn {
        items(data.value) { user ->
            Text(user.name)
        }
    }
}
```

### 5. Cancelamento de Coroutine

Uma coroutine pode ser cancelada a qualquer momento. Para que o cancelamento funcione corretamente, as coroutines devem cooperar, seja atingindo pontos de suspensão como `delay()` ou `withContext()`, ou verificando explicitamente o cancelamento com `ensureActive()`.

- O cancelamento propaga-se automaticamente para todas as coroutines filhas.
- Se os recursos não forem libertados corretamente (ficheiros, ligações, etc.), o sistema pode ficar num estado inconsistente.

> Nota: Envolva coroutines lançadas com `launch` em try/catch para tratamento de exceções.
> No caso de `async`, o try/catch deve envolver a chamada a `await()`.

#### Armadilha #1: try/catch

```kotlin
try {
    delay(1000)
} catch (e: Exception) {
    // Capturou CancellationException e continuou, impedindo o cancelamento
}
```

- Problema: Capturar Exception pode interceptar CancellationException e impedir a coroutine de cancelar.
- Solução: Capturar apenas exceções que não sejam CancellationException, ou relançar a exceção:

```kotlin
try {
    delay(1000)
} catch (e: CancellationException) {
    throw e // propaga o cancelamento
} catch (e: Exception) {
    // tratar outras exceções
}
```

#### Armadilha #2: operações dependentes

Se uma coroutine executar várias operações dependentes, cancelar a meio pode deixar o sistema num estado inconsistente.

- Solução: usar try/finally ou estruturas transacionais para garantir rollback ou limpeza de recursos:

```kotlin
try {
    operation1()
    operation2()
} finally {
    cleanup() // executa sempre, mesmo se a coroutine for cancelada
}
```

#### Armadilha #3: try/finally com NonCancellable

- O bloco finally executa sempre, mesmo em caso de cancelamento.
- No entanto, funções de suspensão dentro de finally ainda podem ser canceladas.
- Use o contexto NonCancellable para garantir que tarefas críticas, como limpeza ou rollback, sejam concluídas:

```kotlin
try {
    doWork() // trabalho cancelável
} finally {
    withContext(NonCancellable) {
        cleanup() // garante execução mesmo com cancelamento
    }
}
```

#### ensureActive vs yield

| Função         | O que faz                                                                                            | Quando usar                                                                  |
| -------------- | ---------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| ensureActive() | Verifica se a coroutine foi cancelada; lança CancellationException se sim                            | Dentro de loops longos ou cálculos pesados para cooperar com cancelamento    |
| yield()        | Suspende a coroutine; lança CancellationException se cancelada; permite que outras coroutines corram | Dentro de loops ou tarefas pesadas para ceder a execução a outras coroutines |

💡 Regra prática:

- ensureActive → verifica cancelamento sem suspender
- yield → verifica cancelamento e suspende, permitindo que outras coroutines corram

> Computações normais (loops, cálculos, etc.) não são canceláveis por defeito.
> Não verificam cancelamento automaticamente, por isso deve chamar ensureActive manualmente.

### 6. Superviser Job

Todas as coroutines dentro do mesmo CoroutineScope são canceladas quando uma excepção é lançada. Nenhuma coroutine continua a executar suspended code, elas vão apenas receber `CancellationException`.
O SupervisorJob muda o comportamento do `parent`:

- Cada `children` é independente no que toca a exceções.
- Se uma falhar, as outras continuam.
- O `parent` não é cancelado automaticamente.

```kotlin
    val scope = CoroutineScope(Dispatchers.Main + supervisorJob())
```

### 7. coroutineScope & supervisorScope

| Característica              | `coroutineScope`                          | `supervisorScope`                             |
| --------------------------- | ----------------------------------------- | --------------------------------------------- |
| **Cancelamento**            | Cancela todas as `children` se uma falhar | Outras `children` continuam                   |
| **Uso típico**              | Tarefas dependentes                       | Tarefas independentes                         |
| **Herança de contexto**     | Sim                                       | Sim                                           |
| **Scope termina quando...** | Todas as `children` terminam com sucesso  | Todas as `children` terminam (mesmo com erro) |
| **Lança exceção da filha?** | Sim                                       | Só se for manualmente tratada ou propagada    |

### 8. Continuations

Quando fazemos uma call assíncrona e precisamos do resultado (por exemplo, getLocation() que retorna a localização), ela pode demorar ou nem completar. Para lidar com isso, usamos continuations, que permitem suspender e depois retomar a execução de uma coroutine.

- Uma `continuation`, representa “o resto da execução” de uma coroutine que foi suspensa.
- Com `suspendCancellableCoroutine`, recebemos um objeto `Continuation` para resumir a coroutine quando o resultado estiver disponível. Ou seja, funciona como uma unica chamada bloqueante, mas apenas suspende e resume sem bloquear a thread que a chama

**Como usar:**

- continuation.resume(value) → retoma a coroutine com o resultado.
- continuation.resumeWithException(exception) → retoma a coroutine com uma exceção.
- continuation.invokeOnCancellation { ... } → cleanup se a coroutine for cancelada.

```kotlin
suspend fun simpleSuspend(): String {
    return suspendCancellableCoroutine { cont ->
        // Simulate an asynchronous operation
        delay(10000) // 10 seconds delay

        // Resume the coroutine with a result
        cont.resume("Hello from continuation!")

        // Called if the coroutine is cancelled before completion
        cont.invokeOnCancellation {
            println("Coroutine cancelled before finishing")
        }
    }
}

fun main() = runBlocking {
    val job = launch {
        try {
            val result = simpleSuspend()
            println(result) // prints "Hello from continuation!"
        } catch (e: CancellationException) {
            println("Coroutine was cancelled")
        }
    }

    // Cancel the coroutine before it completes
    job.cancel()
}
```

### 9. Syncronization

```kotlin
fun synchronizationExample() = runBlocking {
    var count = 0
    val mutex = Mutex()

    val jobs = (1..100_000).map {
        launch {
            // Only one coroutine can execute this block at a time
            mutex.withLock {
                count++
            }
        }
    }

    jobs.joinAll()
    println("The count is $count")
}
```

> 💡 Without mutex.withLock, multiple coroutines may update count simultaneously, leading to race conditions and incorrect results.

| Scenario                               | Recommended Solution                               | Why                                                     |
| -------------------------------------- | -------------------------------------------------- | ------------------------------------------------------- |
| **Many writes, few reads**             | `Mutex` + regular collection (`List`, `Map`, etc.) | Guarantees exclusive access during writes               |
| **Many reads, few writes**             | `CopyOnWriteArrayList` or `ConcurrentHashMap`      | Reads are lock-free, writes are synchronized internally |
| **Very high concurrency with updates** | `ConcurrentHashMap` or `ConcurrentSkipListMap`     | Optimized for high-throughput concurrent access         |

✅ Thread-safe for individual operations (e.g., put, get, remove).
❌ Not safe for compound operations like “check then act” (if (map[key] == null) map[key] = value).

> 👉 Use Mutex + ConcurrentHashMap for safe compound updates in a concurrent environment.

```kotlin
val map = ConcurrentHashMap<String, Int>()
val mutex = Mutex()

suspend fun incrementValue(key: String) {
    mutex.withLock {
        val current = map[key] ?: 0
        map[key] = current + 1
    }
}
```

### 10. Testint coroutines

| Concept                       | Purpose                                                       | Key Function / Usage                                  |
| ----------------------------- | ------------------------------------------------------------- | ----------------------------------------------------- |
| `runTest`                     | Runs coroutines in a test scope synchronously                 | `runTest { ... }`                                     |
| `TestDispatcher`              | Control execution and virtual time                            | `StandardTestDispatcher()`, injected in class         |
| `lateinit var testDispatcher` | Reusable dispatcher instance for setup/teardown               | `lateinit var testDispatcher: StandardTestDispatcher` |
| `Dispatchers.setMain`         | Replace Main dispatcher for tests                             | `Dispatchers.setMain(testDispatcher)`                 |
| `Dispatchers.resetMain`       | Reset Main dispatcher after test                              | `Dispatchers.resetMain()`                             |
| `advanceTimeBy(ms)`           | Simulate time passing                                         | `testScheduler.advanceTimeBy(ms)`                     |
| `advanceUntilIdle`            | Execute all pending tasks                                     | `testScheduler.advanceUntilIdle()`                    |
| `runCurrent`                  | Execute pending coroutines immediately without advancing time | `testScheduler.runCurrent()` or `runCurrent()`        |

**Examples:**

```kotlin
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

// Standard implementation used in production code
object StandardDispatchers: DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}

// Test implementation using a TestDispatcher for deterministic testing
class TestDispatchers(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
): DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher
}

class MainCoroutineRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
): TestWatcher() {

    override fun starting(description: org.junit.runner.Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: org.junit.runner.Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
```

```kotlin
class SomeViewModel(private val dispatchers: DispatcherProvider) {
    var lastResult: Int = 0

    suspend fun calculateFibonacci(n: Int) {
        withContext(dispatchers.default) {
            lastResult = fib(n)
        }
    }

    private fun fib(n: Int): Int = if (n <= 1) n else fib(n - 1) + fib(n - 2)
}

class MyCoroutineTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule() // Automatically set/reset Main dispatcher

    private lateinit var viewModel: SomeViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setup() {
        testDispatchers = TestDispatchers(mainCoroutineRule.testDispatcher)
        viewModel = SomeViewModel(dispatchers = testDispatchers)
    }

    @Test
    fun testFibonacciCalculation() = runTest(mainCoroutineRule.testDispatcher) {
        val job = launch {
            viewModel.calculateFibonacci(10)
        }
        assertEquals(0, viewModel.lastResult)
        runCurrent() // Run all pending coroutines immediately
        assertEquals(55, viewModel.lastResult)
        job.cancel()
    }

    @Test
    fun testDelayedCalculation() = runTest(mainCoroutineRule.testDispatcher) {
        val deferred = async {
            delay(1000)
            42
        }
        assertEquals(false, deferred.isCompleted)
        advanceTimeBy(1000)
        assertEquals(42, deferred.await())
    }
}
```

---

## **⚡️ Kotlin Flows**

| **Concept**        | **Type**            | **Emission Behavior / Lifecycle**                                                                  | **Primary Use Case**                                   | **Key Operators / Builders (corrigido)**                                                                                          | **Notes**                                                                           |
| ------------------ | ------------------- | -------------------------------------------------------------------------------------------------- | ------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| **`Flow`**         | ❄️ _Cold_           | Começa a emitir **somente quando coletado**; cada collector inicia uma nova stream.                | Streams assíncronos sequenciais com múltiplos valores. | **Builders:** `flow {}` • **Emission:** `emit()` • **Uso:** `.collect()` • **Ops:** `.map`, `.filter`, `.onEach`                  | Lazy por natureza; não partilhado; cada coleta reexecuta o fluxo.                   |
| **`SharedFlow`**   | 🔥 _Hot_            | Emite assim que existe produtor; shared entre collectors; não armazena valor inicial.              | Broadcast de eventos para múltiplos collectors.        | **Construtor:** `MutableSharedFlow()` • **Ops:** `.shareIn()` • **Emission:** `.emit()` / `.tryEmit()` • **Config:** `replay = X` | Não tem valor inicial; pode reemitir X valores passados conforme `replay`.          |
| **`StateFlow`**    | 🔥 _Hot_            | Mantém **valor atual**; reemite imediatamente para novos collectors.                               | Representar estados da UI persistentes.                | **Construtor:** `MutableStateFlow(initial)` • **Ops:** `.stateIn()` • **Acesso:** `.value` • **Update:** `.update {}` / `value =` | Só emite quando o valor muda (via `equals`).                                        |
| **`callbackFlow`** | 🔗 \_Cold wrapper\* | Cold por fora, hot por dentro; inicia ao coletar; cancela automaticamente quando a coleta termina. | Adaptar callbacks e listeners para `Flow`.             | **Builder:** `callbackFlow {}` • **Emission:** `trySend()` • **Cleanup:** `awaitClose {}`                                         | Ideal para APIs baseadas em callback; suporta cancelamento automático.              |
| **`stateIn()`**    | 🔧 _Operator_       | Converte um _cold Flow_ em _hot StateFlow_ com estratégia de partilha.                             | Manter estado reativo entre collectors.                | **Operator:** `.stateIn(scope, started, initialValue)`                                                                            | Estratégias: `SharingStarted.Eagerly`, `Lazily`, `WhileSubscribed`.                 |
| **`shareIn()`**    | 🔧 _Operator_       | Converte _cold Flow_ em _hot SharedFlow_ compartilhado.                                            | Broadcast para múltiplos collectors.                   | **Operator:** `.shareIn(scope, started, replay)`                                                                                  | Não mantém valor atual; ideal para eventos e streams onde estado atual não importa. |

#### 1️⃣ Flow (Cold)

```kotlin
// Emits only when collected; new collector restarts emission
val numbers = flow<Int> {
    for (i in 1..3) emit(i)
}

runBlocking {
    numbers.collect { println(it) } // Starts emission
    numbers.collect { println(it) } // Re-starts again for this collector
}
```

#### 2️⃣ SharedFlow (Hot)

```kotlin
// Emits immediately, shared among collectors
val shared = MutableSharedFlow<Int>(replay = 1)

runBlocking {
    launch { shared.onEach { println("A: $it") } }
    shared.emit(5) // Active even if no collectors
    launch { shared.onEach { println("B: $it") } } // Gets replayed value
}
```

> By default, SharedFlow does not hold a value. However, you can configure it with a replay buffer to store a set number of past emissions, allowing new collectors to immediately receive those cached events. Otherwise, collectors only receive emissions that occur after they start collecting.

#### 3️⃣ StateFlow (Hot)

```kotlin
// Hot flow that always has a current value
val state = MutableStateFlow(0)

runBlocking {
    launch { state.onEach { println("State: $it") } }
    state.value = 1
    state.value = 2
}
```

> StateFlow always holds/caches its current state. Any collector that subscribes will immediately receive this current value

#### 4️⃣ stateIn() (Cold → Hot StateFlow)

```kotlin
val uiState = repo.dataFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = UiState()
)

uiState.onEach { println(it) }
```

#### 5️⃣ shareIn() (Cold → Hot SharedFlow)

```kotlin
val events = api.eventFlow.shareIn(
    scope = viewModelScope,
    started = SharingStarted.Lazily,
    replay = 1
)

events.onEach { println("Event: $it") }
```

#### 6️⃣ Callback Flow

```kotlin
// Converts a callback API into a Flow
fun observeLocation(interval: Long): Flow<Location> {
    return callbackFlow {
        val locationManager = context.getSystemService<LocationManager>()!!
        var isGpsEnable = false
        var inNetworkEnable = false

        while (!isGpsEnable && !isNetworenable) {
            isGpsEnable = locationManager.isProviderEnable(LocationManager.GPS_PROVIDER)
            isNetworkEnable = locationManager.isProviderEnable(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnable && !isNetworenable) { delay (3000L) }
        }

        //  check packageManager hasFineLocationPermission and hasCoarseLocationPermission permissions ...

        if(hasFineLocationPermission && hasCoarseLocationPermission) {
            val request = LocationRequest.Builder(
                    Priority.PRIORITY_HIGHT_ACCURACY,
                    interval
                ).build()

            val callback = object: LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let {
                        location -> trySend(location)
                    }
                }
            }

            client.requestLocationUpdate(request, callback, context.mainLooper)

            awaitClose {
                client.removeLocationUpdates(callback)
            }
        } else {
            close()
        }
    }
}

// Call flow
runBlocking {
    observeLocation().onEach { println(it) }
}
```

> Starts when collected, stops when collector is cancelled; bridges hot callbacks safely into a Flow.

### Error Handling & Retry

| **Operator / Function**               | **Propósito**                                                                      |
| ------------------------------------- | ---------------------------------------------------------------------------------- |
| `catch { }`                           | Captura exceções no flow e permite tratar ou emitir valores de fallback.           |
| `retry(n)`                            | Tenta novamente o flow upstream `n` vezes em caso de exceção.                      |
| `retryWhen { cause, attempt -> ... }` | Retry condicional baseado no tipo de exceção ou no número de tentativas.           |
| `onCompletion { cause -> ... }`       | Executa código quando o flow termina, quer seja com sucesso ou com erro.           |
| `emitAll(flow)`                       | Emite outro flow dentro do `catch` como fallback ou recuperação.                   |
| `retryWhen` com delay / backoff       | Retry com atraso, podendo implementar exponential backoff para erros transitórios. |

#### Exemplos de Uso

**1️⃣ `catch { }`**

```kotlin
flow<Int> {
    emit(1)
    emit(2 / 0) // lança ArithmeticException
}.catch { e ->
    println("Erro capturado: $e")
    emit(-1) // valor fallback
}.collect { println(it) }
```

**2️⃣ `retry(n)`**

```kotlin
flow {
    emit(1)
    if (Random.nextBoolean()) throw IOException("Falha aleatória")
}.retry(3) // tenta até 3 vezes
 .collect { println(it) }
```

**3️⃣ `retryWhen { cause, attempt -> ... }`**

```kotlin
flow {
    emit(1)
    if (Random.nextBoolean()) throw IOException("Falha aleatória")
}.retryWhen { cause, attempt ->
    cause is IOException && attempt < 5
}.collect { println(it) }
```

**4️⃣ `onCompletion { cause -> ... }`**

```kotlin
flowOf(1, 2, 3)
    .onCompletion { cause ->
        if (cause != null) println("Flow terminado com erro: $cause")
        else println("Flow terminado com sucesso ✅")
    }
    .collect { println(it) }
```

### Outras Operações com Flows

#### zip vs combine

| Operator                   | Propósito                            | Nota                                                                                       |
| -------------------------- | ------------------------------------ | ------------------------------------------------------------------------------------------ |
| `.zip`                     | Combina emissions de múltiplos flows | Se um flow emitir mais rápido, espera pelo outro.                                          |
| `.combine`/`combineLatest` | Combina flows com os últimos valores | Emite sempre que qualquer flow emitir, combinando com os últimos valores dos outros flows. |

**Exemplo `zip`**

```kotlin
val flow1 = flowOf(1, 2, 3)
val flow2 = flowOf("A", "B", "C")

flow1.zip(flow2) { a, b -> "$a$b" }
     .collect { println(it) }
// Output: 1A, 2B, 3C
```

**Exemplo `combine`**

```kotlin
val flow1 = flowOf(1, 2).onEach { delay(100) }
val flow2 = flowOf("A", "B", "C").onEach { delay(150) }

flow1.combine(flow2) { a, b -> "$a$b" }
     .collect { println(it) }
// Output: 1A, 2A, 2B, 2C (depende do timing)
```

#### Flattening Operators (flatMapConcat, flatMapMerge, flatMapLatest)

| **Operator**    | **Propósito**                                                | **Nota / Comportamento**                                                                |
| --------------- | ------------------------------------------------------------ | --------------------------------------------------------------------------------------- |
| `flatMapConcat` | Transforma sequencialmente cada emissão em um novo flow      | Espera cada inner flow terminar antes de processar o próximo; mantém a ordem.           |
| `flatMapMerge`  | Transforma cada emissão em um novo flow de forma concorrente | Executa múltiplos inner flows em paralelo; emissões podem intercalar.                   |
| `flatMapLatest` | Processa apenas a emissão mais recente                       | Cancela inner flows anteriores se chegar nova emissão; ótimo para cenários de "latest". |

**Exemplo `flatMapConcat`**

```kotlin
val flow = (1..3).asFlow()
flow.flatMapConcat { value ->
    flow {
        emit("$value-A")
        emit("$value-B")
    }
}.collect { println(it) }
// Output: 1-A, 1-B, 2-A, 2-B, 3-A, 3-B
```

**Exemplo `flatMapMerge`**

```kotlin
val flow = (1..3).asFlow()
flow.flatMapMerge { value ->
    flow {
        delay(100L * value)
        emit("$value-A")
        emit("$value-B")
    }
}.collect { println(it) }
// Output exemplo (varia com concorrência): 1-A, 2-A, 1-B, 3-A, 2-B, 3-B
```

**Exemplo `flatMapLatest`**

```kotlin
val flow = (1..3).asFlow().onEach { delay(100L) }

flow.flatMapLatest { value ->
    flow {
        emit("$value-A")
        delay(200L)
        emit("$value-B")
    }
}.collect { println(it) }
// Output: 1-A, 2-A, 3-A, 3-B
```

**Exemplo de Caso Combinado**

```kotlin
val flow = (1..3).asFlow()

flow.flatMapMerge { value ->
    flow {
        emit("$value-Start")
        delay(50L)
        emit("$value-End")
    }
}.collect { println(it) }
// Output exemplo: 1-Start, 2-Start, 1-End, 3-Start, 2-End, 3-End
```

#### Handling Backpressure

💡 Backpressure happens when the producer emits values faster than the consumer can process them. Without handling, this can cause slow collectors to block the flow and reduce performance.

| Operador / Função  | Propósito / Comportamento                                                                          |
| ------------------ | -------------------------------------------------------------------------------------------------- |
| `buffer()`         | Cria um buffer entre produtor e consumidor; permite que o produtor emita sem esperar o consumidor. |
| `conflate()`       | Ignora valores intermédios se o consumidor for lento; processa apenas o valor mais recente.        |
| `collectLatest {}` | Cancela o processamento atual se chegar um novo valor; processa apenas a emissão mais recente.     |

**1️⃣ `buffer()`**

```kotlin
val flow = (1..5).asFlow()
flow.onEach { println("A emitir $it"); delay(100) }
    .buffer()
    .collect {
        delay(300)  // consumidor lento
        println("Processado $it")
    }
```

Output:

```txt
A emitir 1 > A emitir 2 > A emitir 3 > Processado 1 > A emitir 4
> Processado 2 > A emitir 5 > Processado 3 > Processado 4 > Processado 5

O produtor continua a emitir mesmo que o consumidor seja lento, graças ao buffer.
```

**2️⃣ `conflate()`**

```kotlin
val flow = (1..5).asFlow()
flow.onEach { println("A emitir $it"); delay(100) }
    .conflate()
    .collect {
        delay(300)  // consumidor lento
        println("Processado $it")
    }
```

Output:

```txt
A emitir 1 > A emitir 2 > A emitir 3 > Processado 3 > A emitir 4
> A emitir 5 > Processado 5

Valores intermédios são ignorados se o consumidor estiver ocupado; processa apenas os mais recentes.
```

**3️⃣ `collectLatest {}`**

```kotlin
val flow = (1..5).asFlow()
flow.onEach { println("A emitir $it"); delay(100) }
    .collectLatest { value ->
        println("A processar $value")
        delay(300)  // processamento lento
        println("Terminado $value")
    }
```

Output:

```txt
A emitir 1 > A processar 1 > A emitir 2 > A processar 2 > A emitir 3 > A processar 3
> A emitir 4 > A processar 4 > A emitir 5 > A processar 5 > Terminado 5

Cada nova emissão cancela o processamento anterior; apenas a emissão mais recente é concluída.
```

### Testing Flows

```kotlin
@Test
fun testRegisterLoading_withFlow() = runTest(testDispatchers.testDispatcher) {
    viewModel.isLoading.test {
        val initialEmission = awaitItem()
        assertThat(initialEmission).isFalse()

        viewModel.register()

        val loadingEmission = awaitItem()
        assertThat(loadingEmission).isTrue()

        val notLoadingEmission = awaitItem()
        assertThat(notLoadingEmission).isFalse()
    }
}

@Test
fun testCanRegister() = runTest(testDispatchers.testDispatcher) {
    viewModel.canRegister.test {
        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("test")
        assertThat(awaitItem()).isFalse()

        viewModel.onPasswordChange("test12345")
        assertThat(awaitItem()).isTrue()
    }
}
```
