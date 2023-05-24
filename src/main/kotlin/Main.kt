import kotlin.random.Random

// Батьківський клас для гравців
open class Player(val name: String) {
    open fun guess(): Int {
        println("$name, введіть свою догадку:")
        return readGuess()
    }

    private fun readGuess(): Int {
        while (true) {
            val guess = readLine()?.toIntOrNull()
            if (guess != null && guess in 1..100) {
                return guess
            } else {
                println("Введіть число з діапазону від 1 до 100.")
            }
        }
    }
}

// Клас гравця-людини
class HumanPlayer(name: String) : Player(name)

// Клас гравця-комп'ютера
class ComputerPlayer : Player("Комп'ютер") {
    // Власні властивості для обмеження діапазону догадок
    private var lowerBound = 1
    private var upperBound = 100
    private var lastGuess: Int? = null

    // Перевизначена функція догадки для комп'ютерного гравця
    override fun guess(): Int {
        // Логіка догадки комп'ютера
        val guess = lastGuess?.let { lastGuess ->
            val midpoint = (lowerBound + upperBound) / 2
            if (midpoint == lastGuess) {
                lowerBound = lastGuess + 1
            }
            midpoint
        } ?: Random.nextInt(lowerBound, upperBound + 1)

        lastGuess = guess
        println("Догадка Комп'ютера: $guess")
        return guess
    }

    // Оновлення меж діапазону догадок комп'ютера
    fun updateBounds(secretNumber: Int) {
        lastGuess?.let { lastGuess ->
            if (secretNumber < lastGuess) {
                upperBound = lastGuess - 1
            } else {
                lowerBound = lastGuess + 1
            }
        }
    }

    // Скидання меж діапазону догадок комп'ютера
    fun resetBounds() {
        lowerBound = 1
        upperBound = 100
        lastGuess = null
    }
}

// Клас гри "Вгадай число"
class GuessNumberGame {
    private var secretNumber: Int = 0
    private lateinit var player1: Player
    private lateinit var player2: Player
    private lateinit var currentPlayer: Player
    private var isPlayingWithComputer: Boolean = false

    // Зміна гравця
    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == player1) player2 else player1
    }

    // Виведення повідомлення
    private fun displayMessage(message: String) {
        println(message)
    }

    // Перевірка догадки гравця
    private fun checkGuess(guess: Int) {
        if (guess == secretNumber) {
            displayMessage("${currentPlayer.name} вгадав число!")
            if (isPlayingWithComputer && currentPlayer is ComputerPlayer) {
                val computerPlayer = currentPlayer as ComputerPlayer
                computerPlayer.resetBounds()
            }
        } else if (guess < secretNumber) {
            displayMessage("Число більше, ніж догадка гравця ${currentPlayer.name}.")
            if (currentPlayer is ComputerPlayer) {
                val computerPlayer = currentPlayer as ComputerPlayer
                computerPlayer.updateBounds(secretNumber)
            }
        } else {
            displayMessage("Число менше, ніж догадка гравця ${currentPlayer.name}.")
            if (currentPlayer is ComputerPlayer) {
                val computerPlayer = currentPlayer as ComputerPlayer
                computerPlayer.updateBounds(secretNumber)
            }
        }
    }

    // Скидання гри до початкових умов
    private fun resetGame() {
        secretNumber = Random.nextInt(1, 101)
        currentPlayer = player1
        if (isPlayingWithComputer) {
            val computerPlayer = player2 as ComputerPlayer
            computerPlayer.resetBounds()
        }
    }

    // Продовження гри
    private fun continueGame(): Boolean {
        println("Бажаєте продовжити гру? (Так/Ні)")
        val input = readLine()?.toLowerCase()
        return input == "так" || input == "yes"
    }

    // Налаштування гравців і режиму гри
    private fun setupPlayers() {
        println("Введіть ваше ім'я:")
        val playerName = readLine() ?: ""
        player1 = HumanPlayer(playerName)

        println("Оберіть режим гри:")
        println("1. Гра з іншим гравцем")
        println("2. Гра з комп'ютером")

        val modeInput = readLine()?.toIntOrNull() ?: 1

        isPlayingWithComputer = modeInput == 2

        if (isPlayingWithComputer) {
            player2 = ComputerPlayer()
        } else {
            println("Введіть ім'я гравця 2:")
            val player2Name = readLine() ?: ""
            player2 = HumanPlayer(player2Name)
        }

        currentPlayer = player1
    }

    // Початок гри
    fun start() {
        println("Ласкаво просимо до гри 'Вгадай число'!")

        var continuePlaying = true

        while (continuePlaying) {
            setupPlayers()
            resetGame()

            while (true) {
                val guess = currentPlayer.guess()
                checkGuess(guess)

                if (guess == secretNumber) {
                    break
                }

                switchPlayer()
            }

            displayMessage("Гравець ${currentPlayer.name} переміг у грі!")

            println("Бажаєте зіграти ще раз? (Так/Ні)")
            val playAgainInput = readLine()?.toLowerCase()
            continuePlaying = playAgainInput == "так" || playAgainInput == "yes"
        }
    }
}

// Головна функція програми
fun main() {
    val game = GuessNumberGame()
    game.start()
}
