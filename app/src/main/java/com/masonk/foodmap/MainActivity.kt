package com.masonk.foodmap

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.masonk.foodmap.databinding.ActivityMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    
    // NaverMap 객체
    private lateinit var naverMap: NaverMap

    // NaverMap 객체가 준비되었는지 확인
    private var isMapInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // MapView를 MainActivity 생명주기와 동기화
        // MapView는 자체적인 생명주기 메서드를 가지고 있으며, 이를 Activity의 생명주기 메서드와 연결
        // 지도를 올바르게 초기화하고, 일시 중지하고, 다시 시작하고, 메모리가 부족할 때 적절히 처리
        binding.mapView.onCreate(savedInstanceState)

        // 비동기로 NaverMap 객체 얻기 -> 콜백
        binding.mapView.getMapAsync(this)

        // 검색뷰 리스너 설정
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            // Called when the user submits the query
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어가 있으면
                if (query?.isNotEmpty() == true) {
                    // 검색
                    SearchRepository.getFamousRestaurantList(query).enqueue(object : Callback<FamousRestaurantList> {
                        override fun onResponse(
                            p0: Call<FamousRestaurantList>,
                            response: Response<FamousRestaurantList>
                        ) {
                            // 검색 결과 가져오기
                            val famousRestaurantList = response.body()?.items.orEmpty()

                            Log.e("famousRestaurantList",famousRestaurantList.toString())

                            // 검색 결과가 없을 때
                            if (famousRestaurantList.isEmpty()) {
                                Toast.makeText(this@MainActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                                return
                            } else if (!isMapInit) { // 지도가 초기화되지 않았을 때
                                Toast.makeText(this@MainActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                return
                            }

                            // 검색 결과를 바탕으로 마커 리스트 만들기
                            val markerList = famousRestaurantList.map {
                                // 마커 객체 생성
                                Marker().apply {
                                    // 위치, 좌표
                                    position = LatLng((it.mapy / 10_000_000.0), (it.mapx / 10_000_000.0)) // WGS84 -> WGS84

                                    // 캡션
                                    captionText = it.title
                                    
                                    // 마커와 네이버맵 연결
                                    map = naverMap
                                }
                            }

                            // 지도를 바라보는 카메라의 이동을 정의한 객체
                            val cameraUpdate = CameraUpdate
                                .scrollTo(markerList.first().position) // 첫 번째 마커의 위치로 카메라 이동
                                .animate(CameraAnimation.Easing) // 카메라 이동 시 애니메이션 적용, 부드럽게 가감속되는 애니메이션

                            Log.e("POSITION", markerList.first().position.toString())

                            // 카메라 이동
                            naverMap.moveCamera(cameraUpdate)
                        }

                        override fun onFailure(p0: Call<FamousRestaurantList>, t: Throwable) {
                            Log.e("FAILURE", "${t.message}")
                        }

                    })

                    // 기본 동작 수행
                    // 키보드 숨기기, 검색어 유지
                    return false
                } else {
                    // 기본 동작을 막음
                    return true
                }
            }

            // Called when the query text is changed
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })


    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    // NaverMap 객체가 준비되면 호출되는 콜백 메서드
    override fun onMapReady(map: NaverMap) {
        naverMap = map
        isMapInit = true
    }

}