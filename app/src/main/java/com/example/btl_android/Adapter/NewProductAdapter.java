package com.example.btl_android.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.btl_android.Model.SanPham;
import com.example.btl_android.item.ProductItem;
import com.example.btl_android.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ItemHolder> {

    Context context;
    ArrayList<SanPham> mangSanPham;

    public NewProductAdapter(Context context, ArrayList<SanPham> mangSanPham) {
        this.context = context;
        this.mangSanPham = mangSanPham;
    }

    @androidx.annotation.NonNull
    @Override
    public NewProductAdapter.ItemHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newproduct, null);
        return new NewProductAdapter.ItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewProductAdapter.ItemHolder holder, int position) {
        SanPham sanPham = mangSanPham.get(position);
        holder.nameSP.setText(sanPham.getTen_san_pham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.priceSP.setText("Giá: " + decimalFormat.format(sanPham.getGia()) + "đ");
        Picasso.get().load(sanPham.getHinh_anh())
                .resize(500, 500)
                .placeholder(R.drawable.ic_imagnot)
                .error(R.drawable.ic_imageerror)
                .into(holder.hinhSP);

        Picasso.get().setIndicatorsEnabled(true); // Hiển thị chỉ báo trạng thái
        Picasso.get().setLoggingEnabled(true);    // Bật chế độ logging

        Log.d("ImageURL", sanPham.getHinh_anh());

    }

    @Override
    public int getItemCount() {return mangSanPham.size();}

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView hinhSP;
        public TextView nameSP, priceSP;

        public ItemHolder(@android.support.annotation.NonNull View itemView) {
            super(itemView);
            hinhSP = itemView.findViewById(R.id.iv_sanpham);
            nameSP = itemView.findViewById(R.id.tv_name);
            priceSP = itemView.findViewById(R.id.tv_price);
        }
    }
}
