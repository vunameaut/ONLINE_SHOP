package com.example.btl_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.btl_android.item.ProductItem;
import com.example.btl_android.R;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductItem> productList;

    // Constructor
    public ProductAdapter(Context context, List<ProductItem> productList) {
        this.context = context;
        this.productList = productList;
    }

    // Khởi tạo ViewHolder và inflate layout cho mỗi item trong RecyclerView
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    // Gán dữ liệu cho từng item trong RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem product = productList.get(position);
        holder.textViewName.setText(product.getTenSanPham());
        holder.textViewPrice.setText(String.format("%,dđ", product.getGia())); // Định dạng giá

        // Sử dụng Glide để tải ảnh trực tiếp trong onBindViewHolder
        Glide.with(context)
                .load(product.getHinhAnh())
                .override(400, 400)  // Thiết lập kích thước ảnh
                .fitCenter()         // Đặt ảnh vào trung tâm, giữ tỷ lệ
                .into(holder.imageViewProduct);
    }

    // Trả về số lượng item trong danh sách
    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Khởi tạo ViewHolder
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewName, textViewPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.iv_product_image);
            textViewName = itemView.findViewById(R.id.tv_product_name);
            textViewPrice = itemView.findViewById(R.id.tv_product_price);

        }
    }
}
