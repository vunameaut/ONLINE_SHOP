package com.example.btl_android.Adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.example.btl_android.item.admin.Admin_sanpham_item;

import java.util.ArrayList;
import java.util.List;

public class Admin_sanpham_adapter extends RecyclerView.Adapter<Admin_sanpham_adapter.SanphamViewHolder> implements Filterable {

    private List<Admin_sanpham_item> sanphamList;
    private List<Admin_sanpham_item> sanphamListFull;
    private Context context;

    public Admin_sanpham_adapter(List<Admin_sanpham_item> sanphamList, Context context) {
        this.sanphamList = sanphamList;
        this.sanphamListFull = new ArrayList<>(sanphamList);
        this.context = context;
    }

    @NonNull
    @Override
    public SanphamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);
        return new SanphamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanphamViewHolder holder, int position) {
        Admin_sanpham_item sanpham = sanphamList.get(position);
        holder.tenSanPham.setText(sanpham.getTenSanPham());
        holder.giaSanPham.setText(String.valueOf(sanpham.getGiaSanPham()));
        holder.soLuongTonKho.setText(String.valueOf(sanpham.getSoLuongTonKho()));

        // Load hình ảnh sản phẩm với Glide
        Glide.with(context)
                .load(sanpham.getHinhAnhSanPham())
                .into(holder.hinhAnhSanPham);
    }

    @Override
    public int getItemCount() {
        return sanphamList.size();
    }

    @Override
    public Filter getFilter() {
        return sanphamFilter;
    }

    private final Filter sanphamFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Admin_sanpham_item> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(sanphamListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Admin_sanpham_item item : sanphamListFull) {
                    if (item.getTenSanPham().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sanphamList.clear();
            sanphamList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class SanphamViewHolder extends RecyclerView.ViewHolder {
        public TextView tenSanPham, giaSanPham, soLuongTonKho;
        public ImageView hinhAnhSanPham;

        public SanphamViewHolder(@NonNull View itemView) {
            super(itemView);
            tenSanPham = itemView.findViewById(R.id.textTenSanPham);
            giaSanPham = itemView.findViewById(R.id.textGiaSanPham);
            soLuongTonKho = itemView.findViewById(R.id.textSoLuongTonKho);
            hinhAnhSanPham = itemView.findViewById(R.id.imageHinhAnhSanPham);
        }
    }
}
