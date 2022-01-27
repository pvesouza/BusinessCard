package br.com.example.pedro.businesscard.ui

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Insets
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.view.WindowMetrics
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.example.pedro.businesscard.App
import br.com.example.pedro.businesscard.R
import br.com.example.pedro.businesscard.data.BusinessCard
import br.com.example.pedro.businesscard.databinding.ActivityAddBusinessCardBinding
import br.com.example.pedro.businesscard.utils.Verify
import com.google.zxing.WriterException
import java.io.ByteArrayOutputStream

class AddBusinessCard : AppCompatActivity() {

    private val binding by lazy {ActivityAddBusinessCardBinding.inflate(layoutInflater)}
    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }
    // Byte array that will contain the image
    private lateinit var qrCodeByteArray: ByteArray


    companion object{
        private const val TAG = "AddBusinessCard"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    // Set all listeners
    private fun setListeners() {
        val loadImage:Button = binding.buttonLoad
        val saveButton:Button = binding.buttonSave
        val nameEdit:EditText = binding.edittextNameCard
        val countryCodeEdit:EditText = binding.edittextPhoneCountryCode
        val provincyCodeEdit:EditText = binding.edittextPhoneProvincyCard
        val phoneNumberEdit:EditText = binding.edittextPhoneNumberCard
        val backgroundColorEdit:EditText = binding.edittextBackgroundColor
        val emailEdit:EditText = binding.edittextEmailCard
        val companyEdit:EditText = binding.edittextCompanyName
        val qrCodeView: ImageView = binding.imageViewQrCode


        // Load an image from gallery
        loadImage.setOnClickListener{

            val email = emailEdit.text.toString()
            val dimension = calcDimension()
            val verify = Verify()

            if (verify.verifyEmail(email)){
                val qrCoder = QRGEncoder(email, null, QRGContents.Type.TEXT, dimension)
                try {
                    val bitmap = qrCoder.encodeAsBitmap()
                    // Load the bitmap inside imageView
                    qrCodeView.setImageBitmap(bitmap)
                    // Transforms to BLOB data
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    qrCodeByteArray = stream.toByteArray()

       /*             qrCodeByteArray.forEach {
                        Log.d(TAG, "${it}")
                    }*/



                }catch (e:WriterException){
                    Log.e(TAG, "Error during generation QR-Code")
                }
            }else{
                Toast.makeText(this, R.string.label_add_not_success, Toast.LENGTH_SHORT).show()
            }
        }

        // Save the content in Database SQLite
        saveButton.setOnClickListener{
            val name:String = nameEdit.text.toString()
            val countryCode:String = countryCodeEdit.text.toString()
            val provincyCode:String = provincyCodeEdit.text.toString()
            val phoneN:String = phoneNumberEdit.text.toString()
            var phoneNumber:String = ""
            if (countryCode.isNotEmpty() && provincyCode.isNotEmpty() && phoneN.isNotEmpty()){
                phoneNumber = "+${countryCode} (${provincyCode})${phoneN.subSequence(0,5)}-${phoneN.substring(6)}"
            }
            val backgroundColor:String = backgroundColorEdit.text.toString()
            val email:String = emailEdit.text.toString()
            val company:String = companyEdit.text.toString()

            val list:List<String> = listOf(
                name,
                phoneNumber,
                backgroundColor,
                email,
                company,
            )

            val verificator = Verify()

            if (verificator.isAllNotEmpty(list) && verificator.verifyPhoneNumber(phoneNumber) && verificator.verifyEmail(email)){

//                val qrCodeString = String(this.qrCodeByteArray)
//                val imageInputStrem = ByteArrayInputStream(this.qrCodeByteArray)
//                val bitmap1 = BitmapFactory.decodeStream(imageInputStrem)
//                qrCodeView.setImageBitmap(bitmap1)

                val businessCard = BusinessCard(
                    0,
                    name,
                    company,
                    email,
                    phoneNumber,
                    backgroundColor,
                    this.qrCodeByteArray)

                //Persists data
                viewModel.insert(businessCard)

                Toast.makeText(applicationContext, R.string.label_add_success, Toast.LENGTH_LONG).show()

                // Kills the current activity
                finish()
            }else{
                Toast.makeText(applicationContext, R.string.label_add_not_success, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calcDimension() : Int{
        val dimension: Int
        val screenWidth:Int = getScreenWidth()
        val screenHeight:Int = getScreenHeight()

        dimension = if (screenWidth < screenHeight) {
            screenWidth * 3 / 4
        }else{
            screenHeight * 3 /4
        }

        return dimension
    }

    private fun getScreenHeight(): Int {
        val screenHeight: Int
        // Tests the SDK Build
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Gets the current window metrics
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            // Gets the bouns of the phone screen
            val bounds: Rect = windowMetrics.bounds
            // Get the insets ignoring the visibility of system's bar
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            // Is phone in landscape mode ?
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                resources.configuration.smallestScreenWidthDp < 600
            ) {
                screenHeight = bounds.height()
            } else { // Portrait or tablet
                val navigationBarSize: Int = insets.bottom
                screenHeight = bounds.height() - navigationBarSize
            }
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics((displayMetrics))
            screenHeight = displayMetrics.heightPixels
        }
        return screenHeight

    }

    // Gets the Screen Width
    private fun getScreenWidth(): Int{
        val screenWidth1: Int
        // Tests the SDK Build
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Gets the current window metrics
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            // Gets the bouns of the phone screen
            val bounds: Rect = windowMetrics.bounds
            // Get the insets ignoring the visibility of system's bar
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            // Is phone in landscape mode ?
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                resources.configuration.smallestScreenWidthDp < 600
            ) {
                val navigationBarSize: Int = insets.right + insets.left
                screenWidth1 = bounds.width() - navigationBarSize
            } else { // Portrait or tablet
                screenWidth1 = bounds.width()
            }
        } else {
            val displayMetrics: DisplayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics((displayMetrics))
            screenWidth1 = displayMetrics.widthPixels
        }
        return screenWidth1
    }
}