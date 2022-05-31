package domain

data class Room(
    val name: String,
    val clients: MutableList<Client> = mutableListOf()
) {
    private val messagesQueue = TODO()

    fun join(client: Client) {
        if (hasClient(client.nome)) {
            throw Exception("O cliente já se encontra dentro desta sala")
        }
        clients.add(client)
    }

    fun leave(client: Client) {
        if (!hasClient(client.nome)) {
            throw Exception("O cliente não se encontra nesta sala")
        }
        clients.remove(client)
    }

    fun hasClient(client: String): Boolean =
        clients.any { it.nome == client }

    /** Push a message to the messages queue */
    fun sendMessage(client: Client, message: String) {
        if (!hasClient(client.nome)) {
            throw Exception("Precisa de estar na sala para enviar mensagens")
        }
        val newMessage = Message(client, message)
        TODO("Add message to messages queue")
    }

    /** Get first message in the queue (most recent) */
    fun popMessage(): Message {
        TODO("Passively wait for new messages")
    }
}