package br.com.example.pedro.businesscard.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.example.pedro.businesscard.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class Image {

    companion object{
        fun share(context:Context, view:View){
            val bitmap = getScreenShotFromView(view)

            bitmap?.let {
                saveMedia(context, bitmap)
            }
        }

        private fun saveMedia(context: Context, bitmap: Bitmap) {
            val filename = "${System.currentTimeMillis()}.jpg"

            var fileOutStream: OutputStream?

            // Tests the version because there are too many versions and saving is different from version to version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

                    }

                    val imageUri : Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    fileOutStream = imageUri?.let {
                        shareIntent(context, imageUri)
                        resolver.openOutputStream(it)
                    }

                }
            }else{
                // Devices which have android under Q version
                val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val imageFile = File(dir, filename)
                shareIntent(context, Uri.fromFile(imageFile))
                fileOutStream = FileOutputStream(imageFile)
            }

            fileOutStream?.use{
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, R.string.label_cardSaved, Toast.LENGTH_LONG).show()
            }

        }

        private fun shareIntent(context: Context, imageUri: Uri){
            val intent : Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/jpg"
            }

            context.startActivity(
                Intent.createChooser(intent, context.resources.getText(R.string.label_share))
            )
        }

        private fun getScreenShotFromView(view: View): Bitmap? {
            var bitmap : Bitmap? = null

            try {
                bitmap = Bitmap.createBitmap(
                    view.measuredWidth,
                    view.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )

                val canvas = Canvas(bitmap)
                view.draw(canvas)
            }catch (e: Exception){
                Log.e("Error - >", "Failure to create bitmap")
            }

            return bitmap
        }
    }
}