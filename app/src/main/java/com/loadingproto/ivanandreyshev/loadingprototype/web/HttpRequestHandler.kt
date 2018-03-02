package com.loadingproto.ivanandreyshev.loadingprototype.web

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

abstract class HttpRequestHandler(urlString: String) {

    companion object {
        internal const val DEFAULT_MAX_CONTENT_BYTES = 5242880L // 5MB

        private const val OK_RESPONSE_CODE = 200
        private const val DEFAULT_PROTOCOL = "http://"

        private const val DEFAULT_MAX_READ_TIMEOUT_MS = 3000L
        private const val DEFAULT_MAX_CONNECTION_TIMEOUT_MS = 3000L
        private const val DEFAULT_MAX_WRITE_TIMEOUT_MS = 3000L
    }

    enum class State {
        NOT_SEND,
        SUCCESS,
        BAD_URL,
        BAD_CONNECTION,
        PERMISSION_DENIED
    }

    var progressListener: IProgressListener? = null
    var maxContentBytes: Long = DEFAULT_MAX_CONTENT_BYTES
    var readTimeoutMs: Long = DEFAULT_MAX_READ_TIMEOUT_MS
    var connectionTimeoutMs: Long = DEFAULT_MAX_CONNECTION_TIMEOUT_MS
    var writeTimeoutMs: Long = DEFAULT_MAX_WRITE_TIMEOUT_MS

    var urlString = urlString
        protected set
    var state: State = State.NOT_SEND
        protected set
    var body: ByteArray? = null
        protected set

    open fun send(url: String? = null): State {
        url?.let { urlString = url }
        val httpUrl = parseUrl(url ?: urlString)

        state = if (httpUrl == null) {
            State.BAD_URL
        } else {
            val client = createClient(getClientBuilder())
            val request = createRequest(httpUrl)
            send(client, request)
        }

        return state
    }

    private fun send(client: OkHttpClient, request: Request): State {
        return try {
            client.newCall(request).execute().use { response ->

                if (response.code() != OK_RESPONSE_CODE) {
                    return State.BAD_CONNECTION
                }

                response.body().use { body ->
                    when {
                        (body == null || body.contentLength() > maxContentBytes) ->
                            State.BAD_CONNECTION
                        else -> {
                            this.body = body.source().readByteArray()
                            State.SUCCESS
                        }
                    }
                }
            }
        } catch (ex: SecurityException) {
            Log.getStackTraceString(ex)
            State.PERMISSION_DENIED
        } catch (ex: Exception) {
            Log.getStackTraceString(ex)
            State.BAD_CONNECTION
        }
    }

    protected abstract fun createRequest(url: HttpUrl): Request

    private fun getClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .readTimeout(readTimeoutMs, TimeUnit.MILLISECONDS)
                .connectTimeout(connectionTimeoutMs, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeoutMs, TimeUnit.MILLISECONDS)
    }

    private fun parseUrl(url: String): HttpUrl? {
        return HttpUrl.parse(url) ?: HttpUrl.parse(DEFAULT_PROTOCOL + url)
    }

    private fun createClient(clientBuilder: OkHttpClient.Builder): OkHttpClient {
        return clientBuilder.addNetworkInterceptor { chain ->
            val originalResponse = chain.proceed(chain.request())
            originalResponse.newBuilder()
                    .body(ProgressResponseBody(originalResponse.body(), progressListener))
                    .build()
        }.build()
    }
}