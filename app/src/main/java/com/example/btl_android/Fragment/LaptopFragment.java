package com.example.btl_android.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btl_android.Adapter.FragmentAdapter;
import com.example.btl_android.CheckConn;
import com.example.btl_android.Model.SanPham;
import com.example.btl_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaptopFragment extends Fragment {

    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    ArrayList<SanPham> mangSanPham;
    FragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_computer, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        mangSanPham = new ArrayList<>();
        adapter = new FragmentAdapter(requireContext(), mangSanPham);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);

        if (CheckConn.haveNetworkConn(requireContext()))
            getData();
        else
            CheckConn.showToast(requireContext(), "Không có kết nối mạng");

        return view;
    }

    public void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("san_pham");

        dbRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mangSanPham.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String ten = snapshot.child("ten_san_pham").getValue(String.class);
                    Integer gia = snapshot.child("gia").getValue(Integer.class);
                    String anh = snapshot.child("hinh_anh").getValue(String.class);
                    String loai = snapshot.child("loai").getValue(String.class);
                    assert loai != null;
                    if (loai.equals("Laptop")) {
                        mangSanPham.add(new SanPham(ten, gia, anh, loai));
                        //Log.d("PhoneFragment", "Tên sản phẩm: " + ten);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CheckConn.showToast(requireContext(), "Lỗi: " + error.getMessage());
            }
        });
    }
}