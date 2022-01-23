package br.com.example.pedro.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import br.com.example.pedro.businesscard.App
import br.com.example.pedro.businesscard.R
import br.com.example.pedro.businesscard.data.BusinessCard
import br.com.example.pedro.businesscard.databinding.ActivityAddBusinessCardBinding

class AddBusinessCard : AppCompatActivity() {

    private val binding by lazy {ActivityAddBusinessCardBinding.inflate(layoutInflater)}
    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }
    private var resultLaucher:ActivityResultLauncher<Intent>? = null

    companion object{
        private const val REQUEST_CODE_IMAGE_GALLERY:Int = 2001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        resultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if (result.resultCode == RESULT_OK){
                val data:Intent? = result.data
                if (data != null){
                    val image = data.data
                }
            }

        }

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
        val companyEdit = binding.edittextCompanyName


        // Load an image from gallery
        loadImage.setOnClickListener{
            getPhotoFile(resultLaucher)
        }

        // Save the content in Database SQLite
        saveButton.setOnClickListener{
            val name:String = nameEdit.text.toString()
            val countryCode:String = countryCodeEdit.text.toString()
            val provincyCode:String = provincyCodeEdit.text.toString()
            val phoneN:String = phoneNumberEdit.text.toString()
            val phoneNumber:String = "+${countryCode} (${provincyCode})${phoneN.subSequence(0,5)}-${phoneN.substring(6)}"
            val backgroundColor:String = backgroundColorEdit.text.toString()
            val email:String = emailEdit.text.toString()
            val company:String = companyEdit.text.toString()

            val businessCard:BusinessCard = BusinessCard(
                0,
                name,
                company,
                email,
                phoneNumber,
                backgroundColor)

            //Persists data
            viewModel.insert(businessCard)

            Toast.makeText(applicationContext, R.string.label_add_success, Toast.LENGTH_LONG).show()

            // Kills the current activity
            finish()

        }
    }

    // Get a photo from gallery through name
    private fun getPhotoFile(resultLaucher:ActivityResultLauncher<Intent>?) {

        val intent:Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        resultLaucher?.launch(intent)
    }
}