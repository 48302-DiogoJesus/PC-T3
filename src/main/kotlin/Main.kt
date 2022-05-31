import domain.Client
import domain.Room
import domain.Rooms
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private fun main() {
    val server = Chat()
    runBlocking {
        server.acceptLoop()
    }
}

class Chat {

    private val serverSocket = AsynchronousServerSocketChannel.open()
    init {
        serverSocket.bind(InetSocketAddress("0.0.0.0", 8080))
    }

    private val rooms = Rooms()

    suspend fun acceptLoop() {
        coroutineScope {
            while (true) {
                val clientSocket = serverSocket.acceptAsync()
                Client(
                    socket = clientSocket,
                    nome = "UserTeste",
                    currentRoom = null,
                    rooms = rooms
                ).launchClientHandler()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Chat::class.java)
    }
}

suspend fun AsynchronousSocketChannel.readAsync(buffer: ByteBuffer): Int {

    val res = suspendCoroutine<Int> { continuation ->
        this.read(
            buffer,
            Unit,
            object : CompletionHandler<Int, Unit> {

                override fun completed(result: Int, attachment: Unit) {
                    continuation.resume(result)
                }

                override fun failed(exc: Throwable, attachment: Unit) {
                    continuation.resumeWithException(exc)
                }
            }
        )
    }
    return res
}

suspend fun AsynchronousSocketChannel.writeAsync(buffer: ByteBuffer): Int {

    var counter = 0
    while (buffer.hasRemaining()) {
        counter += suspendCoroutine<Int> { continuation ->
            this.write(
                buffer,
                Unit,
                object : CompletionHandler<Int, Unit> {

                    override fun completed(result: Int, attachment: Unit) {
                        continuation.resume(result)
                    }

                    override fun failed(exc: Throwable, attachment: Unit) {
                        continuation.resumeWithException(exc)
                    }
                }
            )
        }
    }
    return counter
}

suspend fun AsynchronousServerSocketChannel.acceptAsync(): AsynchronousSocketChannel {
    val socket: AsynchronousSocketChannel = suspendCoroutine { continuation ->
        this.accept(
            Unit,
            object : CompletionHandler<AsynchronousSocketChannel, Unit> {
                override fun completed(result: AsynchronousSocketChannel, attachment: Unit?) {
                    continuation.resume(result)
                }

                override fun failed(exc: Throwable, attachment: Unit?) {
                    continuation.resumeWithException(exc)
                }
            }
        )
    }
    return socket
}