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
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();

        picasso.load(sanPham.getHinh_anh())
                .placeholder(R.drawable.ic_imagnot)
                .error(R.drawable.ic_imageerror)
                .into(holder.hinhSP);

//        picasso.setIndicatorsEnabled(true);
//        picasso.setLoggingEnabled(true);
//        Log.d("Picasso", "Image URL: " + sanPham.getHinh_anh());
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
