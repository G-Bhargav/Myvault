package com.example.myvault



import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


@Suppress("DEPRECATION")
class Pdfviewer : AppCompatActivity() {

    lateinit var pdfView: PDFView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)
        pdfView = findViewById(R.id.pdfView)
        progressBar= findViewById(R.id.videoProgressBar)
        val filename = intent.getStringExtra("EXTRA_PDFNAME")
        val fileurl = intent.getStringExtra("EXTRA_FILEURL")
        RetrievePDFFromURL(pdfView,progressBar).execute(fileurl)

    }

    class RetrievePDFFromURL(var pdfView: PDFView,val progressBar: ProgressBar) : AsyncTask<String, Void, InputStream>() {
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
            pdfView.fromStream(result).enableSwipe(true).swipeHorizontal(true).spacing(10).load()
            progressBar.visibility= View.GONE
        }
    }
}


