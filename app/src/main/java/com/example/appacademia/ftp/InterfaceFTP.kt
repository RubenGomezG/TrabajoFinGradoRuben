package com.example.appacademia.ftp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.appacademia.R
import com.example.appacademia.databinding.ActivityMainBinding
import com.example.appacademia.ui.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.*
import java.net.InetAddress

 class InterfaceFTP : AppCompatActivity() {

    companion object {
        const val HOST = "81.88.53.117"
        const val USUARIO = "admin_images@appacademia.es"
        const val CONTRASENA = "Velasco9!Velasco9!"
    }

    protected lateinit var binding: ActivityMainBinding

    /**
     * Se crea la InterfazFTP como una MainActivity, para lanzarse al mismo tiempo y estar activa
     * durante toda la ejecución
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.button.setOnClickListener {
        //    requestPermission()
        //}
    }

    private fun requesPermission() {
        when{
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED ->{
                pickPhoto()
            }

            else-> requestPermisionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

     /**
      * Variable que pide los permisos al Usuario, si se conceden, pasaremos a elegir una foto
      */
    private var requestPermisionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted->
        if (isGranted){
            pickPhoto()
        }else{
            Toast.makeText(this,"Necesitas habilitar los permisos de galería",Toast.LENGTH_SHORT).show()
        }

    }

     /**
      * Descomprime las imágenes a un ByteArray
      */
    protected fun getByteArrayFromBitmap(bitmap: Bitmap?): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    protected fun createAndUploadEmptyFile() {
        var ftpClient: FTPClient? = null

        try {
            ftpClient = FTPClient()
            ftpClient.connect(InetAddress.getByName(HOST))

            if (ftpClient.isConnected) {
                ftpClient.login(USUARIO, CONTRASENA)
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                // Crear un archivo vacío
                val emptyFileContent = "".toByteArray()
                val inputStream: InputStream = ByteArrayInputStream(emptyFileContent)

                ftpClient.enterLocalPassiveMode()

                // Subir el archivo vacío al servidor
                val remoteFileName = "archivo_vacio.txt"
                if (ftpClient.storeFile(remoteFileName, inputStream)) {
                    showToast("¡Archivo vacío subido exitosamente!")
                } else {
                    val storeError = ftpClient.replyString
                    showToast("Error al subir el archivo vacío. Detalles: $storeError")
                    Log.i("a", storeError)
                }

                inputStream.close()
                ftpClient.logout()
            } else {
                showToast("No se pudo establecer la conexión FTP.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Error durante la operación FTP: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Error desconocido: ${e.message}")
        } finally {
            try {
                ftpClient?.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
                showToast("Error al desconectar: ${e.message}")
            }
        }
    }

     /**
      * Este método abre la conexión con el servidor y cogiendo la imagen que se le pasa
      * por parámetro, sube a la ruta del servidor especificada la imagen via FTP
      * @param bitmap
      * @param fileName
      */
      fun uploadFileToFTP(bitmap: Bitmap, fileName: String) {
         var ftpClient: FTPClient? = null
         try {
             ftpClient = FTPClient()
             ftpClient.connect(InetAddress.getByName(HOST))
             if (ftpClient.isConnected) {
                 showToast("¡Conectado exitosamente!")
                 Log.i("conexion ftp", "¡Conectado exitosamente!")
                 ftpClient.login(USUARIO, CONTRASENA)
                 ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                 val byteArray = getByteArrayFromBitmap(bitmap)
                 val inputStream = ByteArrayInputStream(byteArray)

                 ftpClient.enterLocalPassiveMode()

                 ftpClient.storeFile(fileName, inputStream)

                 inputStream.close()
                 ftpClient.logout()
                 showToast("¡Archivo subido exitosamente!")
                 Log.i("conexion ftp", "¡Archivo subido exitosamente!")

             } else {
                 showToast("No se pudo establecer la conexión FTP.")
             }
         } catch (e: IOException) {
             e.printStackTrace()
             showToast("Error durante la operación FTP: ${e.message}")
         } finally {
             try {
                 ftpClient?.disconnect()
             } catch (e: IOException) {
                 e.printStackTrace()
                 showToast("Error al desconectar: ${e.message}")
             }
         }
     }


     /**
      * Este método abre la conexión con el servidor y cogiendo la ruta que se pasa por parámetro
      * descarga del servidor la imagen via FTP
      * @param fileName
      */
     public suspend fun downloadFileFromFTP(fileName: String): Bitmap? = withContext(Dispatchers.IO) {
        var ftpClient: FTPClient? = null
        var bitmap: Bitmap? = null

        try {
            ftpClient = FTPClient()
            ftpClient.connect(InetAddress.getByName("81.88.53.117"))
            if (ftpClient.isConnected) {
                //showToast("¡Conectado exitosamente!")
                Log.i("conexion ftp", "¡Conectado exitosamente!")
                ftpClient.login("admin_images@appacademia.es", "Velasco9!Velasco9!")
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

                ftpClient.enterLocalPassiveMode()

                val outputStream = BufferedInputStream(ftpClient.retrieveFileStream(fileName))
                bitmap = BitmapFactory.decodeStream(outputStream)
                outputStream.close()
                ftpClient.completePendingCommand()

                //showToast("¡Archivo descargado exitosamente!")
                Log.i("conexion ftp", "¡Archivo descargado exitosamente!")

                ftpClient.logout()
            } else {
                //showToast("No se pudo establecer la conexión FTP.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            //showToast("Error durante la operación FTP: ${e.message}")
        } finally {
            try {
                ftpClient?.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
                //showToast("Error al desconectar: ${e.message}")
            }
        }
        //return en "hilo"
        bitmap
    }

    public fun colocarImage(bitmap: Bitmap?,imageView: ImageView){
        Glide.with(imageView.context)
        .load(bitmap)
        .into(imageView)
    }

     /**
      * Variable que comprueba que se ha lanzado la actividad de la Galería de Android
      */
    protected val startForActivityGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                try {
                    val bitmap: Bitmap? = if (data != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(contentResolver, data)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            // Versiones anteriores a Android P
                            MediaStore.Images.Media.getBitmap(contentResolver, data)
                        }
                    } else {
                        null
                    }

                    //binding.imageView.setImageBitmap(getResizedBitmap(bitmap, 1024))

                    if (bitmap != null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val username = MainActivity().username
                            if (username.isNotBlank()) {
                                uploadFileToFTP(bitmap, username)
                            } else {
                                showToast("Nombre de usuario no disponible.")
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    protected fun getResizedBitmap(bitmap: Bitmap?, maxSize: Int): Bitmap? {
        var width = bitmap?.width
        var height = bitmap?.height
        if (width != null) {
            if (height != null) {
                if (width <= maxSize && height <= maxSize) {
                    return bitmap
                }
            }
        }
        var bitmapRatio = width!!.toFloat() / height!!.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap!!, width, height, true)
    }

    protected fun getStringImage(bitmap: Bitmap?): String {
        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    protected fun pickPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    protected fun showToast(message: String) {
        runOnUiThread {
            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
