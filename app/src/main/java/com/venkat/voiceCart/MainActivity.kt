package com.venkat.voiceCart
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_SPEECH_INPUT = 1000

    private lateinit var voiceInputButton: Button
    private lateinit var cartContentsTextView: TextView
    private lateinit var clearCartButton: Button

    private var cartItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        voiceInputButton = findViewById(R.id.voice_input_button)
        cartContentsTextView = findViewById(R.id.cart_contents_textview)
        clearCartButton = findViewById(R.id.clear_cart_button)

        voiceInputButton.setOnClickListener {
            startVoiceInput()
        }

        clearCartButton.setOnClickListener {
            clearCart()
        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your food preference...")
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (result != null && result.isNotEmpty()) {
                    val sandwich = result[0]
                    addCartItem(sandwich)
                }
            }
        }
    }

    private fun addCartItem(item: String) {
        cartItems.add(item)
        updateCartContents()
    }

    private fun removeCartItem(position: Int) {
        if (position >= 0 && position < cartItems.size) {
            cartItems.removeAt(position)
            updateCartContents()
        }
    }

    private fun clearCart() {
        cartItems.clear()
        updateCartContents()
    }

    private fun updateCartContents() {
        val stringBuilder = StringBuilder()
        for ((index, item) in cartItems.withIndex()) {
            stringBuilder.append("${index + 1}. $item\n")
        }
        cartContentsTextView.text = stringBuilder.toString()
    }
}
