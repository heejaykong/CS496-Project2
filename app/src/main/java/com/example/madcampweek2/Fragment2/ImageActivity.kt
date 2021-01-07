package com.example.madcampweek2.Fragment2

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.madcampweek2.R
import org.jetbrains.anko.toast

public class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.focus_image)
        val intent = getIntent()
        val imageUri = intent.getStringExtra("uri")
        val imageView : ImageView = findViewById(R.id.imageView)
        if (imageUri != null) {
            setImage(imageView, imageUri)
        }
        else{
            toast("ERROR")
            finish()
        }
        //btnBack.setOnClickListener{
        //    finish()
        //}
    }
    private fun setImage(imageView : ImageView, imageUri : String){
        Log.d("test", "URI: $imageUri")
        try {
            imageView.setImageURI(Uri.parse(imageUri))
        }catch(e: Exception){
            toast("ERROR")
            finish()
        }
    }
}