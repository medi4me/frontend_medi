package com.example.mediforme.Data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//1. 보낼 데이터 클래스를 만든다. 2:받을 데이터 클래스를 만든다.
//3: 인터페이스? 4: 레티로핏 객체?,

//여기에 슬레시 안붙어있으면 아래에 주의해야함! .baseUrl안에 (15:50)
//여긴 마지막에 슬래쉬를 붙이니까 인터페이스에 앞에 슬래쉬는 빼야함
const val BASE_URL = "http://10.0.2.2:8080/api/"
//const val BASE_URL = "http://10.0.2.16:8080/api/"
val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return  retrofit
}

//오브젝트유/ 싱글톤패턴
//object RetrofitInstance {
//    private const val BASE_URL = "https://bbbbbb/api"
//
//    val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}