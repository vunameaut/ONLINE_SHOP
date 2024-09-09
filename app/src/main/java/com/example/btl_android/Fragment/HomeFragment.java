package com.example.btl_android.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btl_android.Adapter.ProductAdapter;
import com.example.btl_android.Adapter.SliderAdapter;
import com.example.btl_android.R;
import com.example.btl_android.item.ProductItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<ProductItem> productList;
    private List<String> sliderImageUrls;
    private ViewPager2 viewPagerSlider;
    private SliderAdapter sliderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productList = new ArrayList<>();
        sliderImageUrls = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo RecyclerView cho danh sách sản phẩm
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerViewProducts.setAdapter(productAdapter);

        // Khởi tạo ViewPager cho slider ảnh
        viewPagerSlider = view.findViewById(R.id.viewPagerSlider);
        sliderAdapter = new SliderAdapter(getContext(), sliderImageUrls);
        viewPagerSlider.setAdapter(sliderAdapter);

        // Tải dữ liệu
        fetchProductData();
        fetchSliderImages();

        return view;
    }

    // Tải các hình ảnh cho slider
    private void fetchSliderImages() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("slider_images");
        storageReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference fileRef : listResult.getItems()) {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    sliderImageUrls.add(uri.toString());
                    sliderAdapter.notifyDataSetChanged(); // Cập nhật slider khi có dữ liệu
                });
            }
        }).addOnFailureListener(e -> {
            // Xử lý lỗi khi không thể lấy dữ liệu
        });
    }

    // Tải dữ liệu sản phẩm từ Firebase
    private void fetchProductData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("san_pham");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy các thông tin của sản phẩm từ Firebase
                    String tenSanPham = snapshot.child("ten_san_pham").getValue(String.class);
                    Long gia = snapshot.child("gia").getValue(Long.class);
                    String hinhAnh = snapshot.child("hinh_anh").getValue(String.class);
                    String moTa = snapshot.child("mo_ta").getValue(String.class);
                    int soLuongTonKho = snapshot.child("so_luong_ton_kho").getValue(Integer.class);
                    String loai = snapshot.child("loai").getValue(String.class);

                    // Tạo đối tượng sản phẩm và set các thuộc tính
                    ProductItem product = new ProductItem();
                    product.setTenSanPham(tenSanPham);
                    product.setGia(gia);
                    product.setHinhAnh(hinhAnh);
                    product.setMoTa(moTa);
                    product.setSoLuongTonKho(soLuongTonKho);
                    product.setLoai(loai);

                    // Thêm sản phẩm vào danh sách
                    productList.add(product);
                }
                // Cập nhật adapter sau khi tải xong dữ liệu
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi không thể lấy dữ liệu
            }
        });
    }
}
