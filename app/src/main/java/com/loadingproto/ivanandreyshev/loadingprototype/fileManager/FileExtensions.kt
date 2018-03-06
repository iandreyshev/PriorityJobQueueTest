package com.loadingproto.ivanandreyshev.loadingprototype.fileManager

import java.io.File

fun File.safeDelete() {
    if (exists()) {
        delete()
    }
}
