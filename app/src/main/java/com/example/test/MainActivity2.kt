package com.example.test

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityMain2Binding
import org.chromium.base.FileUtils
import java.io.File

class MainActivity2 : AppCompatActivity() {
   private lateinit var binding:ActivityMain2Binding
    lateinit var imageAdapter: AdapterImages
    var selectedPaths = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        imageAdapter = AdapterImages()
        binding.recycler.adapter = imageAdapter
        val selectImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    //If multiple image selected
                    if (data?.clipData != null) {
                        val count = data.clipData?.itemCount ?: 0
                        for (i in 0 until count) {
                            val imageUri: Uri? = data.clipData?.getItemAt(i)?.uri
                            val file = getImageFromUri(imageUri)
                            file.let {
                                it?.let { it1 -> selectedPaths.add(it1.absolutePath) }
                            }
                        }
                        imageAdapter.addSelectedImages(selectedPaths)
                    }
                    //If single image selected
                    else if (data?.data != null) {
                        val imageUri: Uri? = data.data
                        val file = getImageFromUri(imageUri)
                        file?.let {
                            selectedPaths.add(it.absolutePath)
                            Log.d("LL",selectedPaths.toString())
                        }
                        imageAdapter.addSelectedImages(selectedPaths)
                    }
                }
            }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "*/*"
            selectImagesActivityResult.launch(intent)
        }
        try {
            deleteTempFiles()
        } catch (e: Exception) {

        }

    }

    private fun getImageFromUri(imageUri: Uri?) : File? {
        imageUri?.let { uri ->
            val mimeType = getMimeType(this@MainActivity2, uri)
            mimeType?.let {
                val file = createTmpFileFromUri(this, imageUri,"temp_image", ".$it")
                file?.let { Log.d("image Url = ", file.absolutePath) }
                return file
            }
        }
        return null
    }


    private fun getMimeType(context: Context, uri: Uri): String? {
        val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
        return extension
    }

    private fun createTmpFileFromUri(context: Context, uri: Uri, fileName: String, mimeType: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, mimeType,cacheDir)
            FileUtils.copyStreamToFile(stream,file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun deleteTempFiles(file: File = cacheDir): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory) {
                        deleteTempFiles(f)
                    } else {
                        f.delete()
                    }
                }
            }
        }
        return file.delete()
    }

}