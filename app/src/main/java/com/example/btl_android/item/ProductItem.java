package com.example.btl_android.item;

public class ProductItem {
    private String tenSanPham;
    private Long gia; // Sử dụng Long nếu dữ liệu giá là số
    private String hinhAnh;

    public ProductItem() {
        // Constructor mặc định
    }

    // Getters và setters
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Long getGia() {
        return gia;
    }

    public void setGia(Long gia) {
        this.gia = gia;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
