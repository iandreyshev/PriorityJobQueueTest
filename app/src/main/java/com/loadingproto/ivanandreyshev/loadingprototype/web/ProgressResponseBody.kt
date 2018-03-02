package com.loadingproto.ivanandreyshev.loadingprototype.web

import android.util.Log
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class ProgressResponseBody(
        private val responseBody: ResponseBody?,
        private val progressListener: IProgressListener?
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: 0
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            val src = source(responseBody?.source() ?: return null)
            bufferedSource = Okio.buffer(src)
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)

                if (bytesRead > -1L) {
                    totalBytesRead += bytesRead
                }

                progressListener?.update(
                        totalBytesRead,
                        responseBody?.contentLength() ?: 0,
                        bytesRead == -1L)

                return bytesRead
            }
        }
    }
}
