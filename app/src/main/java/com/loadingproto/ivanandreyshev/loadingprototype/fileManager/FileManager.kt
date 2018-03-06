package com.loadingproto.ivanandreyshev.loadingprototype.fileManager

import java.io.File

class FileManager(storage: File) : IFileManager {
    companion object {
        private const val ROOT_NAME = "isPlayRoot"
        private const val WORK_DIR_NAME = "work"
        private const val TEMP_DIR_NAME = "temp"
    }

    private val mRoot = File(storage, ROOT_NAME)
    private val mWorkRoot = File(mRoot, WORK_DIR_NAME)
    private val mTempRoot = File(mRoot, TEMP_DIR_NAME)

    init {
        if (mRoot.exists()) {
            mWorkRoot.mkdir()
            mTempRoot.mkdir()
        }
    }

    override fun createItemTempFile(itemId: Int): File {
        return File(mTempRoot, itemId.toString())
    }

    override fun createItemFile(itemId: Int): File {
        return File(mWorkRoot, itemId.toString())
    }
}
