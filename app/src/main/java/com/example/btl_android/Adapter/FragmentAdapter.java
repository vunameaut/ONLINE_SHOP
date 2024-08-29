package com.example.btl_android.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl_android.Model.SanPham;
import com.example.btl_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentAdapter extends BaseAdapter {

    ArrayList<SanPham> sanPhamList;

    Context context;

    @Override
    public int getCount() {
        return sanPhamList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanPhamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        ImageView iVSanPham;
        TextView textViewName, textViewPrice;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_fragment, null);
            holder.iVSanPham = view.findViewById(R.id.iv_sanpham);
            holder.textViewName = view.findViewById(R.id.tv_name);
            holder.textViewPrice = view.findViewById(R.id.tv_price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            SanPham sanPham = sanPhamList.get(position);
            holder.textViewName.setText(sanPham.getTen_san_pham());
            holder.textViewPrice.setText(String.valueOf(sanPham.getGia()));
            Picasso.get().load(sanPham.getHinh_anh())
                    .placeholder(R.drawable.ic_imagnot)
                    .error(R.drawable.ic_imageerror)
                    .into(holder.iVSanPham);
        }

        return view;
    }
}
