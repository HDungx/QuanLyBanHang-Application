/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
/**
 *
 * @author Nguyen Dung
 */
public class HoaDon {
    Connection con=null;
    Statement st= null;
    ResultSet rs=null;
    String Url = "jdbc:sqlserver://localhost:1433;databaseName=Nhom3_Agile_QLBH;user=nhom3;password=1;"
            + "encrypt=true;trustServerCertificate=true;";
    public int getMaxRow(){
        int row=0;
        try {
            con=DriverManager.getConnection(Url);
            st=con.createStatement();
            rs=st.executeQuery("select max(mahd) from hoadon");
            while(rs.next()){
              row= rs.getInt(1);
            }
            
        } catch (SQLException e) {
            
        }
        return row+1; 
    }
    
    public String[] getNVvalue(String user){
        String[] value = new String[3];
        String sql="select username,password,role from login where username='"+user+"'";
        try {
            con=DriverManager.getConnection(Url);
            PreparedStatement ps= con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                value[0] =rs.getString(1);
                value[1] =rs.getString(2);
                value[2] =rs.getString(3);
            }
        } catch (Exception e) {
        }
        return value;
    }
    
    
    public void addHDCT(int mahd,String masp,String tensp,float donGia,int soluong){
        try {
            con=DriverManager.getConnection(Url);
           PreparedStatement ps= con.prepareStatement("insert into hdct values(?,?,?,?,?)");
           ps.setInt(1, mahd);
           ps.setString(2, masp);
           ps.setString(3, tensp);
           ps.setFloat(4, donGia);
           ps.setInt(5, soluong);
           ps.executeUpdate();
        } catch (Exception e) {
        }
    }   
    
    public int getSoLuong(String masp){
        int sl=0;
        try {
            con=DriverManager.getConnection(Url);
            st=con.createStatement();
            rs=st.executeQuery("select soluong from sanpham where masp='"+masp+"'");
            if(rs.next()){
                sl=rs.getInt(1);
            }
        } catch (Exception e) {
        }
        return sl;
    }
    
    public void updateSL(String masp,int sl){
        String sql="update sanpham set soluong =? where masp=?";
        try {
            con=DriverManager.getConnection(Url);
            PreparedStatement ps=null;
            ps=con.prepareStatement(sql);
            ps.setInt(1, sl);
            ps.setString(2,masp );
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }
}
