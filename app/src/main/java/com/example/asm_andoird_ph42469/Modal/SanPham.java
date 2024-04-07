package com.example.asm_andoird_ph42469.Modal;

public class SanPham {
    private String _id;

    private String ten;
    private int gia;

    private int soLuong;
    private String image;

    public SanPham(String ten, int gia, int soLuong, String image) {
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.image = image;
    }

    public SanPham(String _id, String ten, int gia, int soLuong, String image) {
        this._id = _id;
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.image = image;
    }

    public SanPham(String ten, int gia, int soLuong) {
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public SanPham() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
