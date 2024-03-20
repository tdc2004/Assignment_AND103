package com.chinhdev.assignment_and103.model;

public class SanPhamModel {
    private String id;
    private String ten;
    private double gia;
    private int soLuong;
    private boolean tonKho;

    public SanPhamModel(String id, String ten, double gia, int soLuong, boolean tonKho) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.tonKho = tonKho;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public boolean isTonKho() {
        return tonKho;
    }

    public void setTonKho(boolean tonKho) {
        this.tonKho = tonKho;
    }
}
