
fun main() {
    val employee1 = Employee(name = "Fred")
    val employee2 = Employee(name = "John")

    employee1.acceptOrder(CoffeeMachine.LATTE)
    employee2.acceptOrder(CoffeeMAchine.BLACK_SHORT)
}

enum class CoffeeType (val brewTime: Long) {
    LATTE(10000L),
    BLACK_SHORT(5000L)
}

data class Coffee(val type: CoffeeType)

interface OnCoffeeBrewListner {
    fun onCoffeeBrewed(coffee: Coffee)
}

class CoffeMaker {
    suspend fun brewCoffee(type: CoffeeType, callback: OnCoffeeBrewListner) {
        delay(type.brewTime)
        callback.onCoffeeBrewed(Coffee(type))
    }
}

class Employee(val name: String): OnCoffeeBrewListner {
    private val coffeeMaker = CoffeMaker()

    fun acceptOrder(type: CoffeeType) {
        coffeeMaker.brewCoffee(type, this)
    }

    override fun onCoffeeBrewed(coffee: Coffee) {
        println("$name finished brewing ${coffee.type}")
    }
}