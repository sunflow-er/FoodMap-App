package com.masonk.foodmap

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.masonk.foodmap.databinding.ActivityMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

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

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.3666102, 126.9783881))
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

}