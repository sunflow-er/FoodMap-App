package com.masonk.foodmap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.masonk.foodmap.databinding.ItemFamousRestaurantBinding
import com.naver.maps.geometry.LatLng

class FamousRestaurantAdapter(private val onClick: (LatLng) -> Unit): RecyclerView.Adapter<FamousRestaurantAdapter.FamousRestaurantHolder>() {

    private var dataSet = emptyList<FamousRestaurant>()

    inner class FamousRestaurantHolder(private val binding: ItemFamousRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(famousRestaurant: FamousRestaurant) {
            binding.titleTextView.text = famousRestaurant.title
            binding.categoryTextView.text = famousRestaurant.category
            binding.locationTextView.text = famousRestaurant.roadAddress

            binding.root.setOnClickListener {
                // 좌표값 넘기기
                onClick(LatLng(famousRestaurant.mapy / 10000000.0, famousRestaurant.mapx / 10000000.0))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamousRestaurantHolder {
        return FamousRestaurantHolder(
            ItemFamousRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: FamousRestaurantHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    // dataSet에 리스트 데이터를 할당하고 리사이클러뷰 갱신
    fun setData(dataSet: List<FamousRestaurant>) {
        this.dataSet = dataSet
        
        // 데이터 세트가 변경되었음을 어댑터에 알리는 역할
        // 어댑터는 전체 데이터 세트가 변경되었음을 인식하고, 모든 항목을 다시 그림
        // 비효율적이므로 사용 지양
        notifyDataSetChanged()
    }
}