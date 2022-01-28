package br.com.example.pedro.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import br.com.example.pedro.businesscard.App
import br.com.example.pedro.businesscard.databinding.ActivityMainBinding
import br.com.example.pedro.businesscard.utils.Image
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private  val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }
    private val adapter by lazy {BusinessCardAdapter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerviewCards.adapter = adapter
        getAllCards()
        insertListeners()
    }

    // Inserts all listeners of this activity
    private fun insertListeners() {
        val addButton: FloatingActionButton = binding.buttonAddNewCard

        addButton.setOnClickListener{
            val intent: Intent = Intent(this@MainActivity, AddBusinessCard::class.java)
            startActivity(intent)
        }

        adapter.listenerShare = { card->
            Image.share(this@MainActivity, card)
        }
    }

    //Get all business cards
    private fun getAllCards(){
        viewModel.getAll().observe(this) {businessCards ->
            adapter.submitList(businessCards)
        }
    }
}