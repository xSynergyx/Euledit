package com.example.euledit

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    // retrofit instance
    val retrofit = Retrofit.Builder()
        .baseUrl("https://eulerity-hackathon.appspot.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
/*
    interface ApiService {
        @Multipart
        @POST("upload")
        fun uploadImage(@Part image: MultipartBody.Part): Call<YourResponseModel>
    }

    val apiService = retrofit.create(ApiService::class.java)

 */
    interface UrlApi {
        @GET("/upload")
        suspend fun getImageUrl() : Response<ImageURL>
    }

    private val urlApi: UrlApi = retrofit.create(UrlApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var motorcycleIV = findViewById<ImageView>(R.id.image)

        val filter1Button: Button = findViewById(R.id.filter)
        val filter2Button: Button = findViewById(R.id.filter2)
        val filter3Button: Button = findViewById(R.id.filter3)
        val filter4Button: Button = findViewById(R.id.filter4)
        val addTextButton: Button = findViewById(R.id.add_text_button)
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraint_layout)

        val testUploadButton: Button = findViewById(R.id.test_upload)

        // Filters
        filter1Button.setOnClickListener {
            ImageViewCompat
                .setImageTintList(
                    motorcycleIV,
                    ColorStateList.valueOf(Color.parseColor("#58f560"))
                )
        }

        filter2Button.setOnClickListener {
            ImageViewCompat
                .setImageTintList(
                    motorcycleIV,
                    ColorStateList.valueOf(Color.parseColor("#030ffc"))
                )
        }

        filter3Button.setOnClickListener {
            ImageViewCompat
                .setImageTintList(
                    motorcycleIV,
                    ColorStateList.valueOf(Color.parseColor("#f5313b"))
                )
        }

        filter4Button.setOnClickListener {
            ImageViewCompat
                .setImageTintList(
                    motorcycleIV,
                    ColorStateList.valueOf(Color.parseColor("#31f5ee"))
                )
        }

        //reset image
        motorcycleIV.setOnClickListener {
            ImageViewCompat.setImageTintList(
                motorcycleIV,
                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            )
        }

        //TODO: Finish adding text overlay
        addTextButton.setOnClickListener {
            // Create EditText
            val editText = EditText(this)
            editText.hint = "My cool hint"
            editText.layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            editText.setPadding(20, 20, 20, 20)

            // Add EditText to LinearLayout
            constraintLayout.addView(editText)
        }

        val cacheButton = findViewById<Button>(R.id.cache_button)


        cacheButton.setOnClickListener {
            val tempBitmap = BitmapFactory.decodeFile("${this.cacheDir}/temp_image.jpg")
            motorcycleIV.setImageBitmap(tempBitmap)
        }

        testUploadButton.setOnClickListener {
            // Get bitmap of the image
            val editedImageBitmap: Bitmap = getBitmapFromView(motorcycleIV)

            //Create a temporary file to store the image in
            val file = File(this.cacheDir, "temp_image.jpg")
            val fileOutputStream = FileOutputStream(file)
            editedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            //val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            //val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

            // Get the image URL
            imageUrlQuery()

//            val call = apiService.uploadImage(imageBody)
//            call.enqueue(object : Callback<YourResponseModel> {
//                override fun onResponse(call: Call<YourResponseModel>, response: Response<YourResponseModel>) {
//                    // Handle success response
//                }
//
//                override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
//                    // Handle failure
//                }
//            })


        }

        //ImageViewCompat.setImageTintMode(motorcycleIV, PorterDuff.Mode.MULTIPLY)
        //ImageViewCompat.setImageTintList(motorcycleIV, ColorStateList.valueOf(Color.parseColor("#58f560")))
    }

    private fun imageUrlQuery() = runBlocking {
        // Launching a new coroutine
        GlobalScope.launch {
            val result = urlApi.getImageUrl()
            if (result != null) {
                Log.d("URL ", result.body().toString())
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}