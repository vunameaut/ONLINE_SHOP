package com.example.btl_android.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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

        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerViewProducts.setAdapter(productAdapter);

        viewPagerSlider = view.findViewById(R.id.viewPagerSlider);
        sliderAdapter = new SliderAdapter(getContext(), sliderImageUrls);
        viewPagerSlider.setAdapter(sliderAdapter);

        fetchProductData();
        fetchSliderImages();

        return view;
    }

    private void fetchSliderImages() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("slider_images");
        storageReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference fileRef : listResult.getItems()) {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    sliderImageUrls.add(uri.toString());
                    sliderAdapter.notifyDataSetChanged();
                });
            }
        }).addOnFailureListener(e -> {
            // Xử lý lỗi khi không thể lấy dữ liệu
        });
    }

    private void fetchProductData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("san_pham");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tenSanPham = snapshot.child("ten_san_pham").getValue(String.class);
                    Long gia = snapshot.child("gia").getValue(Long.class);
                    String hinhAnh = snapshot.child("hinh_anh").getValue(String.class);

                    ProductItem product = new ProductItem();
                    product.setTenSanPham(tenSanPham);
                    product.setGia(gia);
                    product.setHinhAnh(hinhAnh);

                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
                fetchProductImages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi không thể lấy dữ liệu
            }
        });
    }

    private void fetchProductImages() {
        for (ProductItem product : productList) {
            String imageUrl = product.getHinhAnh();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(getContext())
                        .load(imageUrl)
                        .override(400, 400)
                        .fitCenter()
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                productAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        }
    }
}
