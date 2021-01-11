package com.example.madcampweek2.RetroFit

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import com.example.madcampweek2.Fragment1.PhoneBookData
import com.facebook.FacebookSdk.getApplicationContext
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream


class RetrofitClient private constructor() {
    val apiService: ApiService

    companion object {
        val instance = RetrofitClient()
    }

    init {
        val client = OkHttpClient.Builder().build()
        apiService = Retrofit.Builder()
            .baseUrl("http://192.249.18.208:3000")
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Throws(IOException::class)
    fun postContactProfile(imageData : ByteArray) {
        val reqfile: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageData)
        val body = MultipartBody.Part.createFormData("upload", "abc", reqfile)
        val name = RequestBody.create(MediaType.parse("text/plain"), "upload")
        val req: Call<ResponseBody> = apiService.postImage(body)
        req.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(getApplicationContext(), "Request success", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
    fun uriToByteArrayBody (context : Context, uri : Uri?) : MultipartBody.Part? {
        var imageData : ByteArray
        if (uri == null) {
            return null
        }
        // uri to bit array
        try {
            val inputStream : InputStream? = context.contentResolver.openInputStream(uri);
            var buffer : ByteArray = ByteArray(1024)
            val byteArrayOutputStream = ByteArrayOutputStream(buffer.size);
            var len = 0
            while (inputStream!!.read(buffer).apply{len = this} != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            imageData = byteArrayOutputStream.toByteArray()
            val reqfile: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageData)
            val body = MultipartBody.Part.createFormData("upload", "abc", reqfile)
            return body

        } catch (e : FileNotFoundException) {
            e.printStackTrace();
        } catch (e : IOException) {
            e.printStackTrace();
        }
        return null
    }
}

interface ApiService {
    @GET("/contacts/getAll")
    fun contactsGetAll(
    ) : Call<ResponseBody?>?

    @GET("/contacts/get/{id}")
    fun contactsGet(
        @Path("id") id: String?
    ) : Call<ResponseBody?>?

    @POST("/contacts/post/{name}/{phoneNum}")
    fun contactsPost(
        @Path("name") name: String?,
        @Path("phoneNum") phoneNum: String?,
        @Part image : MultipartBody.Part?
    ): Call<ResponseBody?>?

    @PUT("/contacts/put/{id}/{name}/{phoneNum}")
    fun contactsPut(
        @Path("id") id: String?,
        @Path("name") name: String?,
        @Path("phoneNum") phoneNum: String?,
        @Part image : MultipartBody.Part?
    ): Call<ResponseBody?>?

    @DELETE("/contacts/delete/{id}")
    fun contactsDelete(
        @Path("id") id: String?
    ) : Call<ResponseBody?>?

    @Multipart
    @POST("/images/upload")
    fun postImage(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @PUT("/images/upload")
    fun putImage(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>

}