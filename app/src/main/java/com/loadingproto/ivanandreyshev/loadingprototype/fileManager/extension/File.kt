package com.loadingproto.ivanandreyshev.loadingprototype.fileManager.extension

import java.io.File

fun File.safeDelete() {
    if (exists()) {
        delete()
    }
}
