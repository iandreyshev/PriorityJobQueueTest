package com.loadingproto.ivanandreyshev.loadingprototype.web

interface IProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}
