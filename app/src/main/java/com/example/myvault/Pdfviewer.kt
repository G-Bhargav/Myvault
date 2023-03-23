package com.example.myvault

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.example.myvault.databinding.ActivityPdfviewerBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


@Suppress("DEPRECATION")
class Pdfviewer : AppCompatActivity() {

    lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)
        pdfView = findViewById(R.id.pdfView)
        val filename = intent.getStringExtra("EXTRA_PDFNAME")
        val fileurl = intent.getStringExtra("EXTRA_FILEURL")
        Log.d("123456","asdfg")
        RetrievePDFFromURL(pdfView).execute(fileurl)
    }
    class RetrievePDFFromURL(var pdfView: PDFView) : AsyncTask<String, Void, InputStream>() {
        override fun doInBackground(vararg params: String?): InputStream? {

            var inputStream: InputStream? = null
            try {
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


