package utils

import java.nio.ByteBuffer
import java.nio.charset.Charset

fun String.toByteBuffer() =
      ByteBuffer.wrap(this.encodeToByteArray())

fun ByteBuffer.getString(charset: Charset = Charsets.UTF_8) =
     String(
          this.array().filter { it.compareTo(0) != 0 }.toByteArray(),
          charset
     )