package commands

class CommandHandler {

    private val commands: MutableMap<String, Command> = mutableMapOf()

    init {
        commands["enter"] = Command("enter", "Entra na sala <room-name>. A sala é criada caso não exista. Ex: /enter <room-name>", ::EnterCommand)
        commands["enter"] = Command("enter", "Entra na sala <room-name>. A sala é criada caso não exista. Ex: /enter <room-name>")
    }

    operator fun get(commandName: String) =
        commands[commandName]

    fun printAll() {
        println("|-- Comandos Disponíveis --|")
        for (command in commands.va) {
            println(command.)
        }
        println()
    }
}
