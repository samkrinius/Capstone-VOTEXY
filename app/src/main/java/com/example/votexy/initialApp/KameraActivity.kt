package com.example.votexy.initialApp

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.votexy.R
import com.example.votexy.appPage.HomeActivity
import com.example.votexy.databinding.ActivityKameraBinding
import com.example.votexy.databinding.ActivityMainBinding
import com.example.votexy.ml.ModelFaceRecognition
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class KameraActivity : AppCompatActivity() {

    private var getFile : File? = null

    private lateinit var binding : ActivityKameraBinding

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button2 = findViewById<Button>(R.id.buttonToSignIn)
        button2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.startDetection.setOnClickListener { uploadImage() }
    }

    private fun startCameraX() {
        val intent = Intent(this, XKameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startDetection() {
        if (getFile != null){
            //deteksi
            val model = ModelFaceRecognition.newInstance(this)
            val detectUsers = getAppUser()

            //prepare input
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 64, 64, 3), DataType.FLOAT32)

            //file to bitmap
            val bitmap = BitmapFactory.decodeFile(getFile?.path)
            val resize = Bitmap.createScaledBitmap(bitmap, 64, 64, true)

            //create tensorImage
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(resize)

            //masukinnn
            inputFeature0.loadBuffer(tensorImage.buffer)

            //output
            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            var maxIndex = 0
            var maxValue = outputFeature0.getFloatValue(0)
            for (i in 0 until 4){
                val value = outputFeature0.getFloatValue(i)
                if (value > maxValue){
                    maxValue = value
                    maxIndex = i
                }
            }

            val detectUser = detectUsers[maxIndex]

            binding.hasilDetect.text = detectUser

        }else{
            Toast.makeText(this, "Masukan foto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAppUser(): List<String> {
        val inputString = this.assets.open("tabeluser.txt").bufferedReader().use {
            it.readText()
        }

        return inputString.split("\n")
    }

    private fun uploadImage() {
        Toast.makeText(this, "You are human, next go to the App page", Toast.LENGTH_SHORT).show()
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@KameraActivity)
                getFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

}