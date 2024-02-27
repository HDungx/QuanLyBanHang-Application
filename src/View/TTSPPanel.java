package View;

import Class.HoaDon;
import Class.SanPham;
import DAO.DBConnection;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TTSPPanel extends javax.swing.JPanel {

    List<SanPham> listSP = new ArrayList<>();
    List<SanPham> ListTable = new ArrayList<>();
    HoaDon hd = new HoaDon();
    DefaultTableModel model = null;
    Connection con = null;
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public TTSPPanel() {
        initComponents();
        LoadData();
        getSanPham();
        getthanhtien();
        loadDatatoComboBox();
        tableDecor();
        btnHuyHD.setEnabled(false);
        btnThanhToan.setEnabled(false);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
        cboTenSP.setEnabled(false);
        txtSoluong.setEnabled(false);

    }

    void tableDecor() {
        tblListSP.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 20));
        tblListSP.getTableHeader().setBackground(new Color(83, 113, 253));
        tblListSP.getTableHeader().setForeground(Color.white);
        tblListSP.setRowHeight(50);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblListSP.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblListSP.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblListSP.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblListSP.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblListSP.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    void LoadData() {
        try {
            String query = "select * from sanpham";
            con=DBConnection.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            listSP.clear();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("masp"));
                sp.setTenSP(rs.getString("tensp"));
                sp.setSoLuong(rs.getInt("soluong"));
                sp.setDonGia(rs.getFloat("dongia"));
                listSP.add(sp);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadDatatoComboBox() {
        try {
            con=DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select tensp from sanpham");
            while (rs.next()) {
                String s = rs.getString(1);
                cboTenSP.addItem(s);
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,e);
        }

    }

    void addSPtoTable() {
        SanPham sp = new SanPham();
        HoaDon hd = new HoaDon();
        if (validatesSP()) {
            try {
                String masp = txtMaSP.getText();
                int soluong = hd.getSoLuong(masp);
                int newSL = Integer.parseInt(txtSoluong.getText());
                if (newSL > soluong) {
                    JOptionPane.showMessageDialog(this, "Không đủ hàng trong kho");
                    return;
                } else {
                    if (ListTable.isEmpty()) {
                        sp.tenSP = (String) cboTenSP.getSelectedItem();
                        sp.maSP = txtMaSP.getText();
                        sp.DonGia = Double.parseDouble(txtDonGia.getText());
                        sp.SoLuong = Integer.parseInt(txtSoluong.getText());
                        ListTable.add(sp);
                        fillTableSP();
                        tinhTongTien();
                    } else {
                        for (int i = 0; i < ListTable.size(); i++) {
                            if (cboTenSP.getSelectedItem().equals(ListTable.get(i).tenSP)) {
                                if (newSL > soluong - ListTable.get(i).SoLuong) {
                                    JOptionPane.showMessageDialog(this, "Không đủ hàng trong kho!");
                                    System.out.println(soluong - ListTable.get(i).SoLuong);
                                    return;
                                } else {
                                    ListTable.get(i).SoLuong += Integer.parseInt(txtSoluong.getText());
                                    JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
                                    fillTableSP();
                                    tinhTongTien();
                                    return;
                                }
                            }
                        }
                        sp.tenSP = (String) cboTenSP.getSelectedItem();
                        sp.maSP = txtMaSP.getText();
                        sp.DonGia = Double.parseDouble(txtDonGia.getText());
                        sp.SoLuong = Integer.parseInt(txtSoluong.getText());
                        ListTable.add(sp);
                        fillTableSP();
                        tinhTongTien();
                    }
                    JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
                }
            } catch (Exception e) {
            }
        }
    }

    void deleteSPfromTable() {
        int index = tblListSP.getSelectedRow();
        ListTable.remove(index);
        JOptionPane.showMessageDialog(null, "Xóa sản phẩm thành công!");
    }

    public void UpdateSP() {
        int index = tblListSP.getSelectedRow();
        if (index > -1) {
            SanPham sp = ListTable.get(index);
            if (ListTable.get(index).getSoLuong() == Integer.parseInt(txtSoluong.getText())) {
                JOptionPane.showMessageDialog(null, "Giá trị không đổi!");
                return;
            } else {
                sp.SoLuong = Integer.parseInt(txtSoluong.getText());
                if (ListTable.get(index).getSoLuong() == 0) {
                    ListTable.remove(index);
                }
                JOptionPane.showMessageDialog(this, "Đã cập nhật!");
                cboTenSP.setSelectedIndex(0);
                txtMaSP.setText("");
                txtDonGia.setText("");
                txtSoluong.setText("");
                txtThanhTien.setText("");
            }
            fillTableSP();
            tinhTongTien();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm muốn sửa!");
        }
    }

    public void fillFormSP(int index) {
        cboTenSP.setSelectedItem(ListTable.get(index).getTenSP());
        txtMaSP.setText(ListTable.get(index).getMaSP());
        txtDonGia.setText(String.valueOf(ListTable.get(index).getDonGia()));
        txtSoluong.setText(String.valueOf(ListTable.get(index).getSoLuong()));
    }

    void fillTableSP() {
        DefaultTableModel model = (DefaultTableModel) tblListSP.getModel();
        model.setRowCount(0);
        //Duyệt list bổ sung vào table
        for (SanPham sp : ListTable) {
            Object[] row = new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(),
                sp.getDonGia(), sp.getThanhTien()};
            model.addRow(row);
        }
    }

    void getSanPham() {
        cboTenSP.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for (SanPham sp : listSP) {
                    if (cboTenSP.getSelectedItem().equals(sp.tenSP)) {
                        txtDonGia.setText(String.valueOf(sp.DonGia));
                        txtMaSP.setText(sp.maSP);
                    }
                }
            }
        });
    }

    void getthanhtien() {
        txtSoluong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                float tt = (Float.parseFloat(txtDonGia.getText())) * (Integer.parseInt(txtSoluong.getText()));
                txtThanhTien.setText(String.valueOf(tt));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txtSoluong.getText().trim().length() == 0) {
                    txtThanhTien.setText("");
                } else {
                    float tt = (Float.parseFloat(txtDonGia.getText())) * (Integer.parseInt(txtSoluong.getText()));
                    txtThanhTien.setText(String.valueOf(tt));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                float tt = (Float.parseFloat(txtDonGia.getText())) * (Integer.parseInt(txtSoluong.getText()));
                txtThanhTien.setText(String.valueOf(tt));
            }

        });
    }

    void addHD() {
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("Insert into hoadon values(?,?,?,?)");
            ps.setInt(1, Integer.parseInt(lblMaHD.getText()));
            ps.setString(2, lblTen.getText());
            ps.setDate(3, java.sql.Date.valueOf(lblDate.getText()));
            ps.setFloat(4, Float.parseFloat(lblTongTien.getText()));
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void tinhTongTien() {
        float tt = 0;
        model = (DefaultTableModel) tblListSP.getModel();
        if (model.getColumnCount() > 0) {
            for (int i = 0; i < model.getRowCount(); i++) {
                tt += Float.parseFloat(model.getValueAt(i, 4).toString());
            }
            lblTongTien.setText(String.valueOf(tt));
        }
    }

    void addHDCT() {
        model = (DefaultTableModel) tblListSP.getModel();
        if (model.getRowCount() > 0) {
            int mahd = Integer.parseInt(lblMaHD.getText());
            for (int i = 0; i < model.getRowCount(); i++) {
                String masp = model.getValueAt(i, 0).toString();
                String tensp = model.getValueAt(i, 1).toString();
                int soluong = Integer.parseInt(model.getValueAt(i, 2).toString());
                float dongia = Float.parseFloat(model.getValueAt(i, 3).toString());
                hd.addHDCT(mahd, masp, tensp, dongia, soluong);
                int newSL = hd.getSoLuong(masp) - soluong;
                hd.updateSL(masp, newSL);
            }

            cboTenSP.setSelectedIndex(0);
            txtMaSP.setText("");
            txtDonGia.setText("");
            txtSoluong.setText("");
            txtThanhTien.setText("");
            lblTongTien.setText("0");
            ListTable.clear();

            fillTableSP();
            btnThanhToan.setEnabled(false);
            btnHuyHD.setEnabled(false);
            btnTaoHD.setEnabled(true);
            lblMaHD.setText("0");
            lblTen.setText("");
            lblDate.setText("----/--/--");
        } else {
            JOptionPane.showMessageDialog(null, "Giỏ hàng trống!");
            return;
        }
    }

    boolean validatesSP() {
        if (txtSoluong.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Chưa nhập số lượng");
            txtSoluong.requestFocus();
            txtSoluong.setBackground(Color.YELLOW);
            return false;
        } else if (txtSoluong.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            txtSoluong.requestFocus();
            txtSoluong.setBackground(Color.yellow);
            return false;
        } else {
            txtSoluong.setBackground(Color.white);
        }
        return true;
    }

    void printHD() {
        try {
            String tongtien = lblTongTien.getText();
            MessageFormat header = new MessageFormat("Receipt: " + lblMaHD.getText() + "          Tổng hóa đơn:" + tongtien + "VNĐ");
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            tblListSP.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            Logger.getLogger(TTSPPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        txtSoluong = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtThanhTien = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        cboTenSP = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblTen = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        btnTaoHD = new javax.swing.JButton();
        btnHuyHD = new javax.swing.JButton();
        lblTongTien = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListSP = new javax.swing.JTable();

        jToggleButton1.setText("jToggleButton1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setRequestFocusEnabled(false);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "THÔNG TIN SẢN PHẨM", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setMaximumSize(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Mã SP:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tên SP:");

        txtMaSP.setEditable(false);
        txtMaSP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaSP.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Số lượng:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Đơn giá:");

        txtDonGia.setEditable(false);
        txtDonGia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtDonGia.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        txtSoluong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Thành tiền:");

        txtThanhTien.setEditable(false);
        txtThanhTien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtThanhTien.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0, 10, 10));

        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd);

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.setPreferredSize(new java.awt.Dimension(100, 30));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete);

        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUpdate.setText("Sửa");
        btnUpdate.setPreferredSize(new java.awt.Dimension(100, 30));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate);

        cboTenSP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cboTenSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn sản phẩm" }));
        cboTenSP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTenSPItemStateChanged(evt);
            }
        });
        cboTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTenSPActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 51, 51));
        jLabel8.setText("Mã nhân viên:");

        lblTen.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTen.setForeground(new java.awt.Color(255, 51, 51));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 0));
        jLabel9.setText("Mã Hóa Đơn:");

        lblMaHD.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblMaHD.setForeground(new java.awt.Color(255, 51, 51));
        lblMaHD.setText("0");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 51, 0));
        jLabel10.setText("Ngày:");

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 51, 0));
        lblDate.setText("----/--/--");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(lblTen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 41, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(lblTen, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMaHD))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaSP)
                            .addComponent(cboTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(135, 135, 135)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSoluong)
                            .addComponent(txtDonGia)
                            .addComponent(txtThanhTien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cboTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtSoluong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Tổng Tiền:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("VNĐ");

        btnThanhToan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnThanhToan.setText("Thanh toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnTaoHD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnTaoHD.setText("Tạo Hóa Đơn");
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        btnHuyHD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHuyHD.setText("Hủy Hóa Đơn");
        btnHuyHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyHDActionPerformed(evt);
            }
        });

        lblTongTien.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 51, 51));
        lblTongTien.setText("0");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(153, 102, 255))); // NOI18N
        jPanel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        tblListSP.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblListSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblListSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListSPMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblListSP);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTaoHD)
                        .addGap(18, 18, 18)
                        .addComponent(btnHuyHD)
                        .addGap(47, 47, 47)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        UpdateSP();
        fillTableSP();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        // TODO add your handling code here:
        lblMaHD.setText(String.valueOf(hd.getMaxRow()));
        String user = MainJFormUser.lblTruyCap.getText();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(date);
        lblDate.setText(formatDate);
        lblTen.setText(user);
        btnHuyHD.setEnabled(true);
        btnThanhToan.setEnabled(true);
        btnTaoHD.setEnabled(false);
        btnAdd.setEnabled(true);
        btnDelete.setEnabled(true);
        btnUpdate.setEnabled(true);
        cboTenSP.setEnabled(true);
        txtSoluong.setEnabled(true);
        txtSoluong.setEnabled(false);
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void btnHuyHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyHDActionPerformed
        // TODO add your handling code here:
        lblMaHD.setText("0");
        lblTen.setText("");
        ListTable.clear();
        fillTableSP();
        cboTenSP.setSelectedIndex(0);
        txtMaSP.setText("");
        txtDonGia.setText("");
        txtSoluong.setText("");
        lblDate.setText("----/--/--");
        btnThanhToan.setEnabled(false);
        btnHuyHD.setEnabled(false);
        btnTaoHD.setEnabled(true);
        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        cboTenSP.setEnabled(true);
        txtSoluong.setEnabled(true);
    }//GEN-LAST:event_btnHuyHDActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (validatesSP()) {

            addSPtoTable();
            cboTenSP.setSelectedIndex(0);
            txtMaSP.setText("");
            txtDonGia.setText("");
            txtSoluong.setText("");
            txtThanhTien.setText("");
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

        deleteSPfromTable();
        fillTableSP();
        tinhTongTien();
        cboTenSP.setSelectedIndex(0);
        txtMaSP.setText("");
        txtDonGia.setText("");
        txtSoluong.setText("");
        txtThanhTien.setText("");
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblListSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListSPMouseClicked
        // TODO add your handling code here:
//        txtMaSP.setEnabled(false);
//        txtTenSP.setEnabled(false);
//        txtDonGia.setEnabled(false);
        int index = tblListSP.getSelectedRow();
        fillFormSP(index);
    }//GEN-LAST:event_tblListSPMouseClicked

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
        int kq = JOptionPane.showConfirmDialog(this, "Bạn có muốn in hóa đơn", "In hóa đơn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (kq == JOptionPane.YES_OPTION) {
            printHD();
            addHD();
            addHDCT();
        } else {
            addHD();
            addHDCT();
        }

    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void cboTenSPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTenSPItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_cboTenSPItemStateChanged

    private void cboTenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTenSPActionPerformed
        // TODO add your handling code here:
        if ((cboTenSP.getSelectedIndex() > 0)) {
            txtSoluong.setEnabled(true);
        } else {
            txtSoluong.setEnabled(false);
            txtMaSP.setText("");
            txtDonGia.setText("");
            txtSoluong.setText("");
            txtThanhTien.setText("");
        }
    }//GEN-LAST:event_cboTenSPActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHuyHD;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboTenSP;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblTen;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTable tblListSP;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtSoluong;
    private javax.swing.JTextField txtThanhTien;
    // End of variables declaration//GEN-END:variables
}
