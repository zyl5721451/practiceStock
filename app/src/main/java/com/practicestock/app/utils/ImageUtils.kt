package com.practicestock.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    
    /**
     * 创建临时图片文件用于拍照
     */
    fun createImageFile(context: Context): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "JPEG_${timeStamp}_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (ex: IOException) {
            null
        }
    }
    
    /**
     * 将图片保存到应用内部存储
     */
    fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val filename = "IMG_${timeStamp}.jpg"
            
            val directory = File(context.filesDir, "images")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            
            val file = File(directory, filename)
            val outputStream = FileOutputStream(file)
            
            // 压缩图片以节省存储空间
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
            outputStream.close()
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 从文件路径加载图片
     */
    fun loadImageFromPath(imagePath: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(imagePath)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 删除图片文件
     */
    fun deleteImageFile(imagePath: String): Boolean {
        return try {
            val file = File(imagePath)
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 获取图片文件大小（字节）
     */
    fun getImageFileSize(imagePath: String): Long {
        return try {
            val file = File(imagePath)
            file.length()
        } catch (e: Exception) {
            0L
        }
    }
    
    /**
     * 检查图片文件是否存在
     */
    fun imageFileExists(imagePath: String?): Boolean {
        return if (imagePath.isNullOrEmpty()) {
            false
        } else {
            try {
                val file = File(imagePath)
                file.exists() && file.isFile
            } catch (e: Exception) {
                false
            }
        }
    }
}