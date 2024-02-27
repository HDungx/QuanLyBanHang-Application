package View;

import Class.NhanVien;
import DAO.DBConnection;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

public class QLNVPanel extends javax.swing.JPanel {

    List<NhanVien> listNV = new ArrayList<>();
    private int index = -1;
    private TableRowSorter<TableModel> rowSorter = null;
    Connection con=null;
    PreparedStatement ps=null;
    Statement st=null;
    ResultSet rs=null;
    public QLNVPanel() {
        initComponents();
        LoadData();
        DecoTable();
        search();
        TinhLuong();
       
        txtLuong.setEnabled(false);
        rowSorter = new TableRowSorter<TableModel>(tblQLNV.getModel());
        tblQLNV.setRowSorter(rowSorter);
    }

    //----------------------------------------------------------------------------------------------//
    //Code Nâng cấp
    void disableDH(){
        if(!(jtfSearch.getText().trim().length() ==0)){
            btnFirst.setEnabled(false);
            btnLast.setEnabled(false);
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        }else{
            btnFirst.setEnabled(true);
            btnLast.setEnabled(true);
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
        }
    }
    
    
    void TinhLuong(){
                txtNgayCong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                float kq;
                if (Integer.parseInt(txtNgayCong.getText()) > 20) {
                    kq = Integer.parseInt(txtNgayCong.getText()) * 200000;
                    txtLuong.setText(String.valueOf(kq));
                } else {
                    kq = Integer.parseInt(txtNgayCong.getText()) * 150000;
                    txtLuong.setText(String.valueOf(kq));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txtNgayCong.getText().trim().length() == 0) {
                    txtLuong.setText("");
                } else {
                    float kq;
                    if (Integer.parseInt(txtNgayCong.getText()) > 20) {
                        kq = Integer.parseInt(txtNgayCong.getText()) * 200000;
                        txtLuong.setText(String.valueOf(kq));
                    } else {
                        kq = Integer.parseInt(txtNgayCong.getText()) * 150000;
                        txtLuong.setText(String.valueOf(kq));
                    }

                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                float kq;
                if (Integer.parseInt(txtNgayCong.getText()) > 20) {
                    kq = Integer.parseInt(txtNgayCong.getText()) * 200000;
                    txtLuong.setText(String.valueOf(kq));
                } else {
                    kq = Integer.parseInt(txtNgayCong.getText()) * 150000;
                    txtLuong.setText(String.valueOf(kq));
                }
            }

        });

    }
    
    void search(){
                jtfSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = jtfSearch.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = jtfSearch.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String text = jtfSearch.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

    }
    
    void DecoTable(){
        tblQLNV.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 20));
        tblQLNV.getTableHeader().setBackground(new Color(83, 113, 253));
        tblQLNV.getTableHeader().setForeground(Color.white);
        tblQLNV.setRowHeight(26);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblQLNV.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblQLNV.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblQLNV.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblQLNV.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblQLNV.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblQLNV.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
    }
    //Load dữ liệu từ database lên bảng
    void LoadData() {
        try { 
            String query = "select * from nhanvien";
            con = DBConnection.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            listNV.clear();
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("manv"));
                nv.setHoTen(rs.getString("tennv"));
                nv.setNamSinh(rs.getInt("namsinh"));
                nv.setSDT(rs.getString("sdt"));
                nv.setNgayCong(rs.getInt("ngaycong"));
                listNV.add(nv);
            }
            DefaultTableModel model = (DefaultTableModel) tblQLNV.getModel();
            model.setRowCount(0);
            for (NhanVien nv : listNV) {
                Object[] row = new Object[]{nv.getMaNV(), nv.getHoTen(), nv.getNamSinh(),
                    nv.getSDT(), nv.getNgayCong(), nv.getLuong()};
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void DeleteNV() {
        if (txtMaNV.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Nhập mã nhân viên");
            txtMaNV.requestFocus();
            return;
        }
        try {
           con = DBConnection.getConnection();
            String sql = "delete from NHANVIEN where manv = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, txtMaNV.getText());
            ps.execute();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
            con.close();
            LoadData();
            clearFormNV();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Error");
        }
    }

    void updateNV() {
        try {
            con=DBConnection.getConnection();
            String sql = "update NHANVIEN set tennv=?,namsinh=?,sdt=?,ngaycong=? ,luong=? where manv=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, txtHoTen.getText());
            ps.setString(2, txtNamSinh.getText());
            ps.setString(3, txtSDT.getText());
            ps.setInt(4, Integer.parseInt(txtNgayCong.getText()));
            ps.setFloat(5, Float.parseFloat(txtLuong.getText()));
            ps.setString(6, txtMaNV.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Cập nhật thành công!");
            con.close();
            ps.close();
            LoadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void themNhanVien() {
        String query = "insert into nhanvien values (?,?,?,?,?,?)";
        try {
            con=DBConnection.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, txtMaNV.getText());
            ps.setString(2, txtHoTen.getText());
            ps.setInt(3, Integer.parseInt(txtNamSinh.getText()));
            ps.setString(4, txtSDT.getText());
            ps.setInt(5, Integer.parseInt(txtNgayCong.getText()));
            ps.setFloat(6, Float.parseFloat(txtLuong.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null,"Thêm nhân viên thành công!");
            LoadData();
            clearFormNV();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(QLNVPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //----------------------------------------------------------------------------------------------//

    public void clearFormNV() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtNamSinh.setText("");
        txtSDT.setText("");
        txtNgayCong.setText("");
        txtLuong.setText("");
        txtMaNV.setEditable(true);
    }

    public void FirtNV() {
        index = 0;
        display();
    }

    public void lastNV() {
        index = listNV.size() - 1;
        display();

    }

    public void prevNV() {
        if (index == 0) {
            lastNV();

        } else {
            index--;
        }
        display();
    }

    public void nextNV() {
        if (index == listNV.size() - 1) {
            FirtNV();
        } else {
            index++;
        }
        display();
    }

    public void fillNhanVienLenForm() {
        txtMaNV.setText(listNV.get(index).getMaNV());
        txtHoTen.setText(listNV.get(index).getHoTen());
        txtNamSinh.setText(String.valueOf(listNV.get(index).getNamSinh()));
        txtSDT.setText(String.valueOf(listNV.get(index).getSDT()));
        txtNgayCong.setText(String.valueOf(listNV.get(index).getNgayCong()));
        txtLuong.setText(String.valueOf(listNV.get(index).getLuong()));
    }

    public void display() {
        tblQLNV.setRowSelectionInterval(index, index);
        fillNhanVienLenForm();
    }

    public boolean validateNV() {
        //Mã NV
        if (txtMaNV.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa nhập mã NV");
            txtMaNV.setBackground(Color.yellow);
            txtMaNV.requestFocus();
            return false;
        } else {
            //Check định dạng
            Pattern pMaNV = Pattern.compile("PS[0-9]{5}");
            Matcher mMaNV = pMaNV.matcher(txtMaNV.getText());
            if (!mMaNV.matches()) {
                JOptionPane.showMessageDialog(this, "Mã NV nhập sai định dạng");
                txtMaNV.requestFocus();
                txtMaNV.setBackground(Color.pink);
                return false;
            } else {
                //Check trùng
                for (NhanVien nv : listNV) {
                    if (txtMaNV.getText().equals(nv.MaNV)) {
                        JOptionPane.showMessageDialog(this, "Đã tồn tại mã NV này");
                        txtMaNV.requestFocus();
                        txtMaNV.setBackground(Color.pink);
                        return false;
                    }
                }
                txtMaNV.setBackground(Color.white);
            }
        }

        //Họ tên
        if (txtHoTen.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa nhập họ tên");
            txtHoTen.requestFocus();
            txtHoTen.setBackground(Color.yellow);
            return false;
        } else {
            //Check định dạng
            Pattern pHoTen = Pattern.compile("(?:\\P{M}\\p{M}*)+", Pattern.UNICODE_CASE);
            Matcher mHoTen = pHoTen.matcher(txtHoTen.getText());
            if (!mHoTen.matches()) {
                JOptionPane.showMessageDialog(this, "Họ tên sai định dạng");
                txtHoTen.setBackground(Color.PINK);
                txtHoTen.requestFocus();
                return false;
            } else {
                txtHoTen.setBackground(Color.WHITE);
            }
        }

        //Năm sinh
        if (txtNamSinh.getText().trim().length() == 0) {
            txtNamSinh.setBackground(Color.YELLOW);
            JOptionPane.showMessageDialog(this, "Chưa nhập năm sinh");
            txtNamSinh.requestFocus();
            return false;
        } else {
            try {
                if (Integer.parseInt(txtNamSinh.getText()) < 2005 && Integer.parseInt(txtNamSinh.getText()) > 1990) {
                    Integer.parseInt(txtNamSinh.getText());
                    txtNamSinh.setBackground(Color.WHITE);
                } else {
                    JOptionPane.showMessageDialog(this, "Chưa đủ tuổi đi làm");
                    txtNamSinh.setBackground(Color.PINK);
                    txtNamSinh.requestFocus();
                    return false;
                }
                //Check định dạng 

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nhập năm sinh phải là số");
                txtNamSinh.setBackground(Color.PINK);
                txtNamSinh.requestFocus();
                return false;
            }

        }

        //Số điện thoại 
        if (txtSDT.getText().trim().length() == 0) {
            txtSDT.setBackground(Color.YELLOW);
            JOptionPane.showMessageDialog(this, "Chưa nhập SĐT");
            txtSDT.requestFocus();
            return false;
        } else {
            //Check định dạng
            try {
                Integer.parseInt(txtSDT.getText());
                txtSDT.setBackground(Color.white);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nhập số thôi nhé");
                txtSDT.requestFocus();
                txtSDT.setBackground(Color.pink);
                return false;
            }
            Pattern pSDT = Pattern.compile("0[0-9]{9}");
            Matcher mSDT = pSDT.matcher(txtSDT.getText());
            if (!mSDT.matches()) {
                txtSDT.requestFocus();
                txtSDT.setBackground(Color.pink);
                JOptionPane.showMessageDialog(this, "Số điện thoại không đúng");
                return false;
            } else {
                txtSDT.setBackground(Color.white);
            }
        }

        //Ngày công
        if (txtNgayCong.getText().trim().length() == 0) {
            txtNgayCong.setBackground(Color.YELLOW);
            JOptionPane.showMessageDialog(this, "Chưa nhập ngày công");
            txtNgayCong.requestFocus();
            return false;
        } else {
            //Check định dạng
            try {
                Integer.parseInt(txtNgayCong.getText());
                txtSDT.setBackground(Color.white);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nhập số thôi nhé");
                txtNgayCong.requestFocus();
                txtNgayCong.setBackground(Color.pink);
                return false;
            }
            if (Integer.parseInt(txtNgayCong.getText()) < 0 || Integer.parseInt(txtNgayCong.getText()) > 30) {
                txtNgayCong.requestFocus();
                txtNgayCong.setBackground(Color.pink);
                JOptionPane.showMessageDialog(this, "Nhập lại ngày công");
                return false;
            } else {
                txtNgayCong.setBackground(Color.white);
            }
        }
        return true;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnNew2 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jpnRoot = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQLNV = new javax.swing.JTable();
        jpnRoot1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtNamSinh = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        txtNgayCong = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnNew1 = new javax.swing.JButton();
        btnAdd1 = new javax.swing.JButton();
        btnUpdate1 = new javax.swing.JButton();
        btnDelete1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jtfSearch = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        btnNew2.setText("Làm mới");
        btnNew2.setPreferredSize(new java.awt.Dimension(85, 30));

        jPasswordField1.setText("jPasswordField1");

        jPasswordField2.setText("jPasswordField2");

        tblQLNV.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblQLNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã NV", "Họ tên", "Năm sinh", "SĐT", "Ngày công", "Lương"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblQLNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQLNVMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblQLNVMouseExited(evt);
            }
        });
        jScrollPane1.setViewportView(tblQLNV);
        if (tblQLNV.getColumnModel().getColumnCount() > 0) {
            tblQLNV.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblQLNV.getColumnModel().getColumn(4).setPreferredWidth(30);
        }

        jpnRoot1.setBackground(new java.awt.Color(204, 204, 255));
        jpnRoot1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "THÔNG TIN NHÂN VIÊN", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(0, 51, 255))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Mã NV:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Họ tên:");

        txtMaNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaNV.setPreferredSize(new java.awt.Dimension(10, 30));

        txtHoTen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtHoTen.setPreferredSize(new java.awt.Dimension(10, 30));

        txtNamSinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNamSinh.setPreferredSize(new java.awt.Dimension(10, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("SĐT:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Ngày công:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Lương:");

        txtSDT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSDT.setPreferredSize(new java.awt.Dimension(10, 30));

        txtNgayCong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNgayCong.setPreferredSize(new java.awt.Dimension(10, 30));
        txtNgayCong.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtNgayCongInputMethodTextChanged(evt);
            }
        });
        txtNgayCong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayCongActionPerformed(evt);
            }
        });

        txtLuong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtLuong.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtLuong.setPreferredSize(new java.awt.Dimension(10, 30));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new java.awt.GridLayout(2, 0, 10, 10));

        btnNew1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNew1.setText("Làm mới");
        btnNew1.setPreferredSize(new java.awt.Dimension(80, 25));
        btnNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnNew1);

        btnAdd1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAdd1.setText("Thêm");
        btnAdd1.setPreferredSize(new java.awt.Dimension(85, 30));
        btnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdd1);

        btnUpdate1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUpdate1.setText("Cập nhật");
        btnUpdate1.setPreferredSize(new java.awt.Dimension(85, 30));
        btnUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnUpdate1);

        btnDelete1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDelete1.setText("Xóa");
        btnDelete1.setPreferredSize(new java.awt.Dimension(85, 30));
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete1);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Năm sinh:");

        javax.swing.GroupLayout jpnRoot1Layout = new javax.swing.GroupLayout(jpnRoot1);
        jpnRoot1.setLayout(jpnRoot1Layout);
        jpnRoot1Layout.setHorizontalGroup(
            jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRoot1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnRoot1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(20, 20, 20)
                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnRoot1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnRoot1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(20, 20, 20)
                        .addComponent(txtNamSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtNgayCong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpnRoot1Layout.setVerticalGroup(
            jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRoot1Layout.createSequentialGroup()
                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnRoot1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnRoot1Layout.createSequentialGroup()
                                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtNgayCong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jpnRoot1Layout.createSequentialGroup()
                                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jpnRoot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNamSinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))))
                    .addGroup(jpnRoot1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnFirst.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/skip_backward.png"))); // NOI18N
        btnFirst.setPreferredSize(new java.awt.Dimension(50, 50));
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/previous.png"))); // NOI18N
        btnPrev.setPreferredSize(new java.awt.Dimension(50, 50));
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/next.png"))); // NOI18N
        btnNext.setPreferredSize(new java.awt.Dimension(50, 50));
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/skip_forward.png"))); // NOI18N
        btnLast.setPreferredSize(new java.awt.Dimension(50, 50));
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        jtfSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jtfSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfSearchFocusLost(evt);
            }
        });
        jtfSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfSearchKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 51, 255));
        jLabel3.setText("Tìm kiếm:");

        javax.swing.GroupLayout jpnRootLayout = new javax.swing.GroupLayout(jpnRoot);
        jpnRoot.setLayout(jpnRootLayout);
        jpnRootLayout.setHorizontalGroup(
            jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRootLayout.createSequentialGroup()
                .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnRootLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jpnRootLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpnRoot1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpnRootLayout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(102, 102, 102)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfSearch)))))
                .addContainerGap())
        );
        jpnRootLayout.setVerticalGroup(
            jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRootLayout.createSequentialGroup()
                .addComponent(jpnRoot1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd1ActionPerformed
        if (validateNV()) {
            themNhanVien();
        }

    }//GEN-LAST:event_btnAdd1ActionPerformed

    private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
        clearFormNV();
    }//GEN-LAST:event_btnNew1ActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        FirtNV();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        prevNV();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        nextNV();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        lastNV();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate1ActionPerformed
        
        updateNV();
        
    }//GEN-LAST:event_btnUpdate1ActionPerformed

    private void tblQLNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQLNVMouseClicked
        index = tblQLNV.getSelectedRow();
        txtMaNV.setEditable(false);
        fillNhanVienLenForm();
    }//GEN-LAST:event_tblQLNVMouseClicked

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // TODO add your handling code here:
        DeleteNV();

    }//GEN-LAST:event_btnDelete1ActionPerformed

    private void jtfSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfSearchFocusGained
     
        
    }//GEN-LAST:event_jtfSearchFocusGained

    private void jtfSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfSearchFocusLost
       
    }//GEN-LAST:event_jtfSearchFocusLost

    private void txtNgayCongInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtNgayCongInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_txtNgayCongInputMethodTextChanged

    private void txtNgayCongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayCongActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtNgayCongActionPerformed

    private void tblQLNVMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQLNVMouseExited
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblQLNVMouseExited

    private void jtfSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfSearchKeyTyped
        // TODO add your handling code here:
        //disableDH();
    }//GEN-LAST:event_jtfSearchKeyTyped

    private void jtfSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfSearchKeyPressed
        // TODO add your handling code here:
        disableDH();
    }//GEN-LAST:event_jtfSearchKeyPressed

    private void jtfSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfSearchKeyReleased
        // TODO add your handling code here:
        disableDH();
    }//GEN-LAST:event_jtfSearchKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd1;
    private javax.swing.JButton btnDelete1;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNew1;
    private javax.swing.JButton btnNew2;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnUpdate1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpnRoot;
    private javax.swing.JPanel jpnRoot1;
    private javax.swing.JTextField jtfSearch;
    private javax.swing.JTable tblQLNV;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtNamSinh;
    private javax.swing.JTextField txtNgayCong;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
