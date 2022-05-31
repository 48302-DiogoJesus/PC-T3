package domain

import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import readAsync
import utils.getString
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel

data class Client(
    val socket: AsynchronousSocketChannel,
    val nome: String,
    var currentRoom: Room? = null,
    val rooms: Rooms
) {
    fun joinOrCreateRoom(roomName: String) {
        // Create the room if not exists. Join if it exists
        val room = rooms[roomName] ?: rooms.create(roomName)
        // Join the room
        room.join(this)
        currentRoom = room
    }

    fun leaveCurrentRoom() {
        val room = currentRoom
            ?: throw Exception("Não se encontra numa sala")
        // Leave the room
        room.leave(this)
        currentRoom = null
    }

    suspend fun launchClientHandler() {
        coroutineScope {
            launch { readLoop(socket) }
            launch { writeLoop(socket) }
        }
    }

    private suspend fun readLoop(socket: AsynchronousSocketChannel) {
        logger.info("Client read loop started")
        val buffer = ByteBuffer.allocate(1024)
        while (true) {
            socket.readAsync(buffer)
            val input = buffer.getString()
            if (input[0] != '/')
                currentRoom.

        }
    }

    private suspend fun writeLoop(socket: AsynchronousSocketChannel) {
        logger.info("Client write loop started")
        while (true) {
            // Passively looking at the messages queue for this client room
            // socket.writeAsync(msg)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Client::class.java)
    }
}