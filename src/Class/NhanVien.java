package Class;

public class NhanVien {

    public String MaNV;
    public String HoTen;
    public int NamSinh;
    public String SDT;
    public int NgayCong;

    public NhanVien(String MaNV, String HoTen, int NamSinh, String SDT, int NgayCong) {
        this.MaNV = MaNV;
        this.HoTen = HoTen;
        this.NamSinh = NamSinh;
        this.SDT = SDT;
        this.NgayCong = NgayCong;
    }

    public NhanVien() {

    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    public int getNamSinh() {
        return NamSinh;
    }

    public void setNamSinh(int NamSinh) {
        this.NamSinh = NamSinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public int getNgayCong() {
        return NgayCong;
    }

    public void setNgayCong(int NgayCong) {
        this.NgayCong = NgayCong;
    }

    public double getLuong() {
        if (NgayCong > 20) {
            return NgayCong * 200000;
        } else {
            return NgayCong * 150000;
        }
    }


}
