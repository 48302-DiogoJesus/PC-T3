package domain

class Rooms {
    private val rooms: MutableMap<String, Room> = mutableMapOf()

    fun exists(roomName: String) =
        rooms[roomName] != null

    fun create(roomName: String): Room {
        if (exists(roomName)) {
            throw Exception("Sala já existente: $roomName")
        }
        val newRoom = Room(roomName)
        rooms[roomName] = newRoom
        return newRoom
    }

    operator fun get(roomName: String) =
        rooms[roomName]
}