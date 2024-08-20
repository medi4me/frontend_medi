package com.example.mediforme.Data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//1. 보낼 데이터 클래스를 만든다. 2:받을 데이터 클래스를 만든다.
//3: 인터페이스? 4: 레티로핏 객체?,

//여기에 슬레시 안붙어있으면 아래에 주의해야함! .baseUrl안에 (15:50)
const val BASE_URL = "http://10.0.2.2:8080"
fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
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