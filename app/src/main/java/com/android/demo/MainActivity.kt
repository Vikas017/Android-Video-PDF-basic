package com.android.demo


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.core.net.toUri
import com.android.demo.databinding.ActivityMainBinding

private const val TAG = "MainAct"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //interactions starts from here
        setUpVideoView()
        binding.button.setOnClickListener { onButtonClickEvent() }
    }

    private fun setUpVideoView() {
        val link = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val uri = link.toUri().buildUpon().scheme("https").build()
        Log.i(TAG, "URI is : $uri")
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.apply {
            Log.i(TAG, "Video View Setup Started")
            setVideoURI(uri)
            setMediaController(mediaController)
            setOnPreparedListener {
                it.isLooping = true
            }
            start()
        }
    }

    private fun onButtonClickEvent() {
        Log.i(TAG, "Button Clicked")
        val url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}