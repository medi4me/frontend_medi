package com.example.mediforme.remote.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.text.Typography.dagger

const val BASE_URL = "http://3.39.29.2:8080/"
fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return  retrofit
}

/**
 * [NetworkModule]
 * Dagger Hilt를 사용하여 애플리케이션 전반에 걸쳐 네트워크 관련 의존성을 제공하는 모듈입니다.
 * `@Module` 어노테이션은 이 객체가 Hilt 모듈임을 나타냅니다.
 * `@InstallIn(SingletonComponent::class)`은 이 모듈의 의존성들이 애플리케이션의 수명 주기(Singleton 스코프)와 함께 존재함을 의미합니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * [provideBaseUrl]
     * API 요청을 보낼 서버의 기본 URL을 제공합니다.
     * `@Provides` 어노테이션은 Hilt가 이 함수가 제공할 수 있는 의존성을 생성함을 나타냅니다.
     * `@Singleton` 어노테이션은 이 URL이 애플리케이션 전체에서 단 하나의 인스턴스만 존재하도록 보장합니다.
     *
     * @return API 서버의 기본 URL 문자열
     */
    @Provides
    @Singleton
    fun provideBaseUrl() = "http://3.39.29.2:8080/"

    /**
     * [provideRetrofit]
     * Retrofit 인스턴스를 생성하여 제공합니다.
     * `@Provides` 어노테이션은 Hilt가 이 함수를 통해 Retrofit 객체를 생성함을 나타냅니다.
     * `@Singleton` 어노테이션은 이 Retrofit 인스턴스가 애플리케이션 전체에서 단 하나만 존재하도록 보장합니다.
     *
     * @param BASE_URL Hilt가 자동으로 provideBaseUrl() 함수를 통해 주입하는 기본 URL
     * @return 구성된 Retrofit 인스턴스
     */
    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // 기본 URL 설정
            .addConverterFactory(GsonConverterFactory.create()) // JSON 응답을 Kotlin 객체로 변환하기 위한 컨버터 설정
            .build() // Retrofit 인스턴스 빌드
    }

    /**
     * [provideApiService]
     * Retrofit 인스턴스를 사용하여 [ApiService] 인터페이스의 구현체를 생성하고 제공합니다.
     * 이 [ApiService]는 실제 API 엔드포인트들을 정의하며, 네트워크 요청을 수행하는 데 사용됩니다.
     * `@Provides` 어노테이션은 Hilt가 이 함수를 통해 ApiService 객체를 생성함을 나타냅니다.
     * `@Singleton` 어노테이션은 이 ApiService 인스턴스가 애플리케이션 전체에서 단 하나만 존재하도록 보장합니다.
     *
     * @param retrofit Hilt가 자동으로 provideRetrofit() 함수를 통해 주입하는 Retrofit 인스턴스
     * @return Retrofit을 통해 생성된 ApiService 구현체
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
