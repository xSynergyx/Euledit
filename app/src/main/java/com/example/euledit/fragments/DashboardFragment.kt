package com.example.euledit.fragments

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import com.example.euledit.ImageURL
import com.example.euledit.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.FileOutputStream

class DashboardFragment : Fragment() {

    // retrofit instance
    val retrofit = Retrofit.Builder()
        .baseUrl("https://eulerity-hackathon.appspot.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    interface ImagePostApi {
        @Multipart
        @POST("upload")
        fun uploadImage(@Part image: MultipartBody.Part)//: Call<YourResponseModel>
    }

    val imagePostApi = retrofit.create(ImagePostApi::class.java)

    interface UrlApi {
        @GET("/upload")
        suspend fun getImageUrl() : Response<ImageURL>
    }

    private val urlApi: UrlApi = retrofit.create(UrlApi::class.java)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var motorcycleIV = view.findViewById<ImageView>(R.id.image)

        val filter1Button: Button = view.findViewById(R.id.filter)
        val filter2Button: Button = view.findViewById(R.id.filter2)
        val filter3Button: Button = view.findViewById(R.id.filter3)
        val filter4Button: Button = view.findViewById(R.id.filter4)
        val addTextButton: Button = view.findViewById(R.id.add_text_button)
        val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraint_layout)

        val testUploadButton: Button = view.findViewById(R.id.test_upload)

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
            val editText = EditText(requireContext())
            editText.hint = "My cool hint"
            editText.layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            editText.setPadding(20, 20, 20, 20)

            // Add EditText to LinearLayout
            constraintLayout.addView(editText)
        }

        val cacheButton = view.findViewById<Button>(R.id.cache_button)


        cacheButton.setOnClickListener {
            val tempBitmap = BitmapFactory.decodeFile("${requireContext().cacheDir}/temp_image.jpg")
            motorcycleIV.setImageBitmap(tempBitmap)
        }

        testUploadButton.setOnClickListener {
            // Get bitmap of the image
            val editedImageBitmap: Bitmap = getBitmapFromView(motorcycleIV)

            //Create a temporary file to store the image in
            val file = File(requireContext().cacheDir, "temp_image.jpg")
            val fileOutputStream = FileOutputStream(file)
            editedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

            // Get the image URL
            imageUrlQuery(imageBody)

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
    }

    private fun imageUrlQuery(imageBody: MultipartBody.Part) = runBlocking {
        // Launching a new coroutine
        GlobalScope.launch {
            val result = urlApi.getImageUrl()
            Log.d("URL ", result.body().toString())
            //Post request for image
            uploadQuery(imageBody)
        }

    }

    private fun uploadQuery(imageBody: MultipartBody.Part) = runBlocking {

        Log.d("MainActivity", "Made it to the upload query part")
        /*
        val call = imagePostApi.uploadImage(imageBody)
        call.enqueue(object : Callback<YourResponseModel> {
            override fun onResponse(call: Call<YourResponseModel>, response: Response<YourResponseModel>) {
                // Handle success response
            }

            override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
                // Handle failure
            }
        })*/
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