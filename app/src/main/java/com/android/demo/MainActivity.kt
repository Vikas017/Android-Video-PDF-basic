package com.android.demo


import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.core.net.toUri
import com.android.demo.databinding.ActivityMainBinding
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


private const val TAG = "MainAct"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // url of our PDF file.
    var pdfUrl = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"

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
        val url = "android.resource://$packageName/raw/${R.raw.sample}"
        //val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val uri = url.toUri().buildUpon().scheme("https").build()
        Log.i(TAG, "URI IS: $uri")
        RetrievePDFFromURL(binding.pdfView).execute(pdfUrl)
    }
    inner class RetrievePDFFromURL(private val pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        override fun doInBackground(vararg params: String?): InputStream? {
            var inputStream: InputStream? = null
            try {
                // on below line we are creating an url
                // for our url which we are passing as a string.
                val url = URL(params.get(0))
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                return null;
            }
            return inputStream;
        }

        override fun onPostExecute(result: InputStream?) {
            pdfView.fromStream(result).load()

        }
    }
}