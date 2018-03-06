package com.loadingproto.ivanandreyshev.loadingprototype.fileManager

import java.io.File

interface IFileManager {
    fun createItemTempFile(itemId: Int): File

    fun createItemFile(itemId: Int): File
}
