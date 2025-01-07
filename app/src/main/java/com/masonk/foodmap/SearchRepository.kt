package com.masonk.foodmap

import android.app.appsearch.SearchResult
import android.content.res.Resources
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SearchRepository {
    // Moshi
    // 추가적인 설정이나 어댑터를 추가
    //  KotlinJsonAdapterFactory를 추가하여 Kotlin 데이터 클래스를 지원
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // OkHttp
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(SearchIntercepter())
        .build()

    // Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    // retrofit 객체를 이용한 SearchService 인터페이스 구현체
    private val searchService = retrofit.create(SearchService::class.java)

    // 검색어 주변 맛집 리스트 받아오는 함수
    fun getFamousRestaurantList(query: String): Call<FamousRestaurantList> {
        return searchService.getFamousRestaurantList(query = "${query} 맛집", display = 5)
    }
    
    // Intercepter -> OkHttp -> Retrofit
    class SearchIntercepter : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            // X-Naver-Client-Id
            val clientId = FoodMapApplication.appContext.getString(R.string.naver_search_client_id)

            // X-Naver-Client-Secret
            val clientSecret = FoodMapApplication.appContext.getString(R.string.naver_search_client_secret)

            // 새로운 request
            val newRequest = chain.request() // 기존 request 가져오기
                .newBuilder() // 새로운 정보 추가
                .addHeader("X-Naver-Client-Id", clientId) // 헤더 추가
                .addHeader("X-Naver-Client-Secret", clientSecret) // 헤더 추가
                .build() // 새로운 request 빌드

            // 새로운 request로 네트워크 요청을 계속 진행
            return chain.proceed(newRequest)
        }

    }
}