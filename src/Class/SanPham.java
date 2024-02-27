package Class;

import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Mr.Dung
 */
public class SanPham implements Serializable{
    public String maSP,tenSP;
    public double DonGia;
    public int SoLuong;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, double DonGia, int SoLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.DonGia = DonGia;
        this.SoLuong = SoLuong;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }
    public String getTinhTrang(){
        if(SoLuong==0){
            return "Hết hàng";
        }else{
            return "Còn hàng";
        }
    }
    public float getThanhTien(){
        return (float) (SoLuong*DonGia);
    }
}
