package com.example.myvault


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.shockwave.pdfium.PdfiumCore
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
        RetrievePDFFromURL(pdfView).execute(fileurl)
    }
/*

    fun generateImageFromPdf(pdfUri: Uri) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this)
        try {
            val fd = contentResolver.openFileDescriptor(pdfUri, "r")
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            saveImage(bmp)
            pdfiumCore.closeDocument(pdfDocument)
        } catch (e: Exception) {

        }
    }

    fun saveImage(bmp: Bitmap) {
        val iv = findViewById<ImageView>(R.id.imageView3)
        iv.setImageBitmap(bmp)
    }
*/

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
            pdfView.fromStream(result).enableSwipe(true).swipeHorizontal(true).spacing(10).load()
        }
    }
}


