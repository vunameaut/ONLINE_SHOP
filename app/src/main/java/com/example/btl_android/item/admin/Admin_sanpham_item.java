package com.example.btl_android.item.admin;

public class Admin_sanpham_item {
    private String tenSanPham;
    private String maSanPham;
    private int soLuongTonKho;
    private String hinhAnhSanPham;
    private String moTaSanPham;
    private int giaSanPham;

    // Constructor mặc định
    public Admin_sanpham_item() {
    }

    // Constructor có tham số
    public Admin_sanpham_item(String tenSanPham, String maSanPham, int soLuongTonKho, String hinhAnhSanPham, String moTaSanPham, int giaSanPham) {
        this.tenSanPham = tenSanPham;
        this.maSanPham = maSanPham;
        this.soLuongTonKho = soLuongTonKho;
        this.hinhAnhSanPham = hinhAnhSanPham;
        this.moTaSanPham = moTaSanPham;
        this.giaSanPham = giaSanPham;
    }

    // Getter và Setter cho từng thuộc tính
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuongTonKho() {
        return soLuongTonKho;
    }

    public void setSoLuongTonKho(int soLuongTonKho) {
        this.soLuongTonKho = soLuongTonKho;
    }

    public String getHinhAnhSanPham() {
        return hinhAnhSanPham;
    }

    public void setHinhAnhSanPham(String hinhAnhSanPham) {
        this.hinhAnhSanPham = hinhAnhSanPham;
    }

    public String getMoTaSanPham() {
        return moTaSanPham;
    }

    public void setMoTaSanPham(String moTaSanPham) {
        this.moTaSanPham = moTaSanPham;
    }

    public int getGiaSanPham() {
        return giaSanPham;
    }

    // Cập nhật setter để nhận chuỗi và chuyển sang kiểu int
    public void setGiaSanPham(String giaSanPham) {
        try {
            this.giaSanPham = Integer.parseInt(giaSanPham);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.giaSanPham = 0; // Gán giá trị mặc định nếu có lỗi
        }
    }
}
