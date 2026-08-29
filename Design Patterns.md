# Design Patters

## 🏭 Factory (Creational)

Usado para criar objetos sem expor a lógica de criação ao cliente.

- O cliente pede um objeto, e a fábrica decide qual subclasse instanciar.
- Útil quando o tipo exacto de objeto pode variar em tempo de execução.

```kotlin
// Product interface
interface Notification {
    fun send(message: String)
}

// Concrete products
class EmailNotification : Notification {
    override fun send(message: String) = println("Sending EMAIL: $message")
}

class SMSNotification : Notification {
    override fun send(message: String) = println("Sending SMS: $message")
}

class PushNotification : Notification {
    override fun send(message: String) = println("Sending PUSH: $message")
}

// Factory
object NotificationFactory {
    fun create(type: String): Notification = when(type.lowercase()) {
        "email" -> EmailNotification()
        "sms" -> SMSNotification()
        "push" -> PushNotification()
        else -> throw IllegalArgumentException("Unknown notification type")
    }
}

// Usage
fun main() {
    val notification: Notification = NotificationFactory.create("sms")
    notification.send("Your order has been shipped!")
}
```

---

## 🔒 Singleton Pattern (Creational)

Garante que apenas uma instância de uma classe existe, sendo usado para configuração, registo (logging) ou recursos partilhados.

```kotlin
object Logger {
    fun log(message: String) = println("LOG: $message")
}

// Usage
fun main() {
    Logger.log("App started")
    Logger.log("User logged in")
}
```

---

## 🏗️ Builder Pattern (Creational)

Client application will simply specify the parameters that should be used to create the complex object and the builder will take care of building the complex object.

```kotlin
// Product class
data class Pizza(
    val size: String,
    val cheese: Boolean,
    val pepperoni: Boolean,
    val mushrooms: Boolean
)

// Builder class
class PizzaBuilder {
    private var size: String = "Medium"
    private var cheese: Boolean = false
    private var pepperoni: Boolean = false
    private var mushrooms: Boolean = false

    fun size(size: String) = apply { this.size = size }
    fun cheese(value: Boolean) = apply { this.cheese = value }
    fun pepperoni(value: Boolean) = apply { this.pepperoni = value }
    fun mushrooms(value: Boolean) = apply { this.mushrooms = value }

    fun build(): Pizza {
        return Pizza(size, cheese, pepperoni, mushrooms)
    }
}

// Usage
fun main() {
    val pizza = PizzaBuilder()
        .size("Large")
        .cheese(true)
        .pepperoni(true)
        .build()

    println(pizza)
}
```

---

## 🔌 Adapter (Structural)

Usado para converter uma interface numa outra interface que o cliente espera. Ajudando a reutilizar classes existentes que não correspondem à interface necessária.

```kotlin
// Target interface expected by the client
interface PaymentGateway {
    fun pay(amount: Double)
}

// Adaptee: Legacy PayPal class
class LegacyPayPal {
    fun sendPayment(amount: Double) = println("Processing PayPal payment: $$amount")
}

// Adapter to match PaymentGateway interface
class PayPalAdapter(private val paypal: LegacyPayPal) : PaymentGateway {
    override fun pay(amount: Double) {
        paypal.sendPayment(amount)
    }
}

// Usage
fun main() {
    val payment: PaymentGateway = PayPalAdapter(LegacyPayPal())
    payment.pay(150.0)
    // Output: Processing PayPal payment: $150.0
}
```

---

## 🛡️ Proxy

Controla o acesso a outro objeto, podendo adicionar segurança, cache, registo (logging) ou carregamento preguiçoso (lazy loading).

```kotlin
interface Image {
    fun display()
}

class RealImage(private val fileName: String) : Image {
    init { println("Loading $fileName from disk...") }
    override fun display() = println("Displaying $fileName")
}

class ProxyImage(private val fileName: String) : Image {
    private var realImage: RealImage? = null
    override fun display() {
        if (realImage == null) realImage = RealImage(fileName)
        realImage?.display()
    }
}

// Usage
fun main() {
    val image: Image = ProxyImage("photo.png")
    image.display() // Loads and displays
    image.display() // Displays only, no reload
}
```

---

## 🦨 Code Smells

### 1️⃣ Long Method

A função faz demasiadas coisas → difícil de ler/manter.

```kotlin
fun processData(data: List<String>) {
    // parse, validate, filter, save
}
```

### 2️⃣ Large Class / God Class

A classe tem demasiadas responsabilidades → viola o Princípio da Responsabilidade Única.

```kotlin
class UserManager {
    fun createUser() {}
    fun sendEmail() {}
    fun generateReport() {}
}
```

### 3️⃣ Duplicated Code

Mesma lógica repetida em vários lugares → difícil de manter.

```kotlin
fun circleArea(r: Double) = 3.14 * r * r
fun sphereArea(r: Double) = 4 * 3.14 * r * r
```

### 4️⃣ Long Parameter List

Demasiados parâmetros → confuso e propenso a erros.

```kotlin
fun createUser(name: String, age: Int, email: String, address: String, phone: String) {}
```

### 5️⃣ Feature Envy

A classe utiliza excessivamente dados de outra classe → quebra o encapsulamento.

```kotlin
println(order.customer.address)
```

### 6️⃣ Switch / If-Else Overload

Condicionais repetidas em vez de usar polimorfismo → difícil de expandir.

```kotlin
when (shape.type) {
    "circle" -> drawCircle()
    "rectangle" -> drawRectangle()
}
```

### 7️⃣ Magic Numbers / Strings

Valores codificados diretamente (hardcoded) → significado pouco claro, difícil de manter.

```kotlin
if (user.age > 18) { ... }
```

### 8️⃣ Data Clumps

Grupo de variáveis sempre passado em conjunto → deveria ser uma classe.

```kotlin
fun bookTicket(name: String, age: Int, email: String) {}
```

### 9️⃣ Speculative Generality

Código criado para funcionalidades futuras que podem nunca existir → complexidade desnecessária.v

```kotlin
interface FeatureX { fun doSomething() } // never used
```

### 🔟 Comment Smell

Comentários explicam código mau em vez de o melhorar.

```kotlin
// increment i
i++
```
