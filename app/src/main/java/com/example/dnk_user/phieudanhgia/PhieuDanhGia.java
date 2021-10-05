package com.example.dnk_user.phieudanhgia;

public class PhieuDanhGia {
    private int mapdg;
    private int mahv;
    private int malop;
    private Double diem;
    private String ngaydg;
    private int luotthich;
    private String binhluan;
    public PhieuDanhGia(){}

    public PhieuDanhGia(int mapdg, int mahv, int malop, Double diem, String ngaydg, int luotthich, String binhluan) {
        this.mapdg = mapdg;
        this.mahv = mahv;
        this.malop = malop;
        this.diem = diem;
        this.ngaydg = ngaydg;
        this.luotthich = luotthich;
        this.binhluan = binhluan;
    }

    public int getMapdg() {
        return mapdg;
    }

    public void setMapdg(int mapdg) {
        this.mapdg = mapdg;
    }

    public int getMahv() {
        return mahv;
    }

    public void setMahv(int mahv) {
        this.mahv = mahv;
    }

    public int getMalop() {
        return malop;
    }

    public void setMalop(int malop) {
        this.malop = malop;
    }

    public Double getDiem() {
        return diem;
    }

    public void setDiem(Double diem) {
        this.diem = diem;
    }

    public String getNgaydg() {
        return ngaydg;
    }

    public void setNgaydg(String ngaydg) {
        this.ngaydg = ngaydg;
    }

    public int getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(int luotthich) {
        this.luotthich = luotthich;
    }

    public String getBinhluan() {
        return binhluan;
    }

    public void setBinhluan(String binhluan) {
        this.binhluan = binhluan;
    }


}
