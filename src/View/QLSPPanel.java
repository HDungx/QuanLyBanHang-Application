package View;

import Class.SanPham;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class QLSPPanel extends javax.swing.JPanel {

    List<SanPham> listSP = new ArrayList<>();
    private int index = -1;
    private TableRowSorter<TableModel> rowSorter = null;
    PreparedStatement ps = null;
    Connection con = null;

    public QLSPPanel() {
        initComponents();
        LoadData();
        search();
        DecorTable();
        rowSorter = new TableRowSorter<TableModel>(tblQLKHO.getModel());
        tblQLKHO.setRowSorter(rowSorter);
    }

    void DecorTable() {
        tblQLKHO.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 20));
        tblQLKHO.getTableHeader().setBackground(new Color(83, 113, 253));
        tblQLKHO.getTableHeader().setForeground(Color.white);
        tblQLKHO.setRowHeight(26);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblQLKHO.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblQLKHO.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblQLKHO.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblQLKHO.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

    }

    void search() {
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

    void LoadData() {
        try {

            String query = "select * from sanpham";
            con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            listSP.clear();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("masp"));
                sp.setTenSP(rs.getString("tensp"));
                sp.setSoLuong(rs.getInt("soluong"));
                sp.setDonGia(rs.getFloat("dongia"));
                listSP.add(sp);
            }
            DefaultTableModel model = (DefaultTableModel) tblQLKHO.getModel();
            model.setRowCount(0);
            for (SanPham sp : listSP) {
                Object[] row = new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(),
                    sp.getDonGia()};
                model.addRow(row);

            }
            rs.close();
            con.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void filltoTable(int index) {
        txtMaSP.setText(listSP.get(index).getMaSP());
        txtTenSP.setText(listSP.get(index).getTenSP());
        txtSoLuong.setText(String.valueOf(listSP.get(index).getSoLuong()));
        txtDonGia.setText(String.valueOf(listSP.get(index).getDonGia()));
    }

    void addSanPham() {
        try {

            String query = "insert into sanpham values(?,?,?,?)";
            con = DBConnection.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, txtMaSP.getText());
            ps.setString(2, txtTenSP.getText());
            ps.setInt(3, Integer.parseInt(txtSoLuong.getText()));
            ps.setFloat(4, Float.parseFloat(txtDonGia.getText()));
            int kq = ps.executeUpdate();
            if (kq == 1) {
                JOptionPane.showMessageDialog(this, "Success");
            }
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateSP() {
        try {
            String query = "update sanpham set tensp=?,soluong=?,dongia=? where masp=?";
            con = DBConnection.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, txtTenSP.getText());
            ps.setInt(2, Integer.parseInt(txtSoLuong.getText()));
            ps.setFloat(3, Float.parseFloat(txtDonGia.getText()));
            ps.setString(4, txtMaSP.getText());
            int kq = ps.executeUpdate();
            if (kq == 1) {
                JOptionPane.showMessageDialog(this, "Success");
            }
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void removeSP() {
        try {
            String query = "delete from sanpham where masp=?";
            con = DBConnection.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, txtMaSP.getText());
            int kq = ps.executeUpdate();
            if (kq == 1) {
                JOptionPane.showMessageDialog(this, "Success");
            }
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFormSP() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        txtMaSP.setEditable(true);
    }

    //----nút điều hướng sản phẩm ------//
    public void FirtSP() {
        index = 0;
        display();
    }

    public void lastSP() {
        index = listSP.size() - 1;
        display();

    }

    public void prevSP() {
        if (index == 0) {
            lastSP();

        } else {
            index--;
        }
        display();
    }

    public void nextSP() {
        if (index == listSP.size() - 1) {
            FirtSP();
        } else {
            index++;
        }
        display();
    }

    public void display() {
        tblQLKHO.setRowSelectionInterval(index, index);
        filltoTable(index);
    }

    public boolean validateSP() {
        //Mã SP
        if (txtMaSP.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa nhập mã SP");
            txtMaSP.setBackground(Color.yellow);
            txtMaSP.requestFocus();
            return false;
        } else {
            //Check định dạng
            Pattern pSP_TP = Pattern.compile("SP[0-9]{3}");
            Matcher mSP_TP = pSP_TP.matcher(txtMaSP.getText());
            if (!mSP_TP.matches()) {
                txtMaSP.setBackground(Color.pink);
                txtMaSP.requestFocus();
                JOptionPane.showMessageDialog(this, "Nhập chưa đúng định dạng mã SP");
                return false;
            } else {
                //Check trùng
                for (SanPham sp : listSP) {
                    if (txtMaSP.getText().equals(sp.maSP)) {
                        txtMaSP.setBackground(Color.pink);
                        txtMaSP.requestFocus();
                        JOptionPane.showMessageDialog(this, "Đã tồn tại mã SP này");
                        return false;
                    }
                }
                txtMaSP.setBackground(Color.white);
            }
        }

        //Tên SP
        if (txtTenSP.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa nhập tên SP");
            txtTenSP.requestFocus();
            txtTenSP.setBackground(Color.yellow);
            return false;
        } else {
            //Check định dạng
            Pattern pTen_SP = Pattern.compile("(?:\\P{M}\\p{M}*)+", Pattern.UNICODE_CASE);
            Matcher mTen_SP = pTen_SP.matcher(txtTenSP.getText());
            if (!mTen_SP.matches()) {
                JOptionPane.showMessageDialog(this, "Tên SP sai định dạng");
                txtTenSP.setBackground(Color.PINK);
                txtTenSP.requestFocus();
                return false;
            } else {
                txtTenSP.setBackground(Color.WHITE);
            }
        }

        //Đơn giá
        if (txtDonGia.getText().trim().length() == 0) {
            txtDonGia.setBackground(Color.YELLOW);
            JOptionPane.showMessageDialog(this, "Chưa nhập đơn giá");
            txtDonGia.requestFocus();
            return false;
        } else {
            //Check định dạng
            try {
                Integer.parseInt(txtDonGia.getText());
                txtDonGia.setBackground(Color.white);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nhập số thôi nhé");
                txtDonGia.requestFocus();
                txtDonGia.setBackground(Color.pink);
                return false;
            }
            //Đơn giá phải > 1000
            if (Integer.parseInt(txtDonGia.getText()) < 1000) {
                txtDonGia.requestFocus();
                txtDonGia.setBackground(Color.pink);
                JOptionPane.showMessageDialog(this, "Nhập lại đơn giá");
                return false;
            } else {
                txtDonGia.setBackground(Color.white);
            }
        }

        //Số lượng
        if (txtSoLuong.getText().trim().length() == 0) {
            txtSoLuong.setBackground(Color.YELLOW);
            JOptionPane.showMessageDialog(this, "Chưa nhập số lượng SP");
            txtSoLuong.requestFocus();
            return false;
        } else {
            //Check định dạng
            try {
                Integer.parseInt(txtSoLuong.getText());
                txtSoLuong.setBackground(Color.white);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nhập số thôi nhé");
                txtSoLuong.requestFocus();
                txtSoLuong.setBackground(Color.pink);
                return false;
            }
            if (Integer.parseInt(txtSoLuong.getText()) < 0) {
                txtSoLuong.requestFocus();
                txtSoLuong.setBackground(Color.pink);
                JOptionPane.showMessageDialog(this, "Nhập lại số lượng SP");
                return false;
            } else {
                txtSoLuong.setBackground(Color.white);
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnRoot = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        txtDonGia = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnNew2 = new javax.swing.JButton();
        btnAdd2 = new javax.swing.JButton();
        btnUpdate2 = new javax.swing.JButton();
        btnDelete2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQLKHO = new javax.swing.JTable();
        jtfSearch = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnLast = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();

        jpnRoot.setPreferredSize(new java.awt.Dimension(50, 50));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "THÔNG TIN SẢN PHẨM", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(153, 51, 0))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Mã SP:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tên SP:");

        txtMaSP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSPActionPerformed(evt);
            }
        });
        txtMaSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMaSPKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMaSPKeyTyped(evt);
            }
        });

        txtTenSP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Số lượng:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Đơn giá:");

        txtSoLuong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtDonGia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setLayout(new java.awt.GridLayout(2, 0, 10, 10));

        btnNew2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNew2.setText("Làm mới");
        btnNew2.setPreferredSize(new java.awt.Dimension(90, 40));
        btnNew2.setRolloverEnabled(false);
        btnNew2.setSelected(true);
        btnNew2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnNew2);

        btnAdd2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAdd2.setText("Thêm");
        btnAdd2.setPreferredSize(new java.awt.Dimension(90, 40));
        btnAdd2.setRolloverEnabled(false);
        btnAdd2.setSelected(true);
        btnAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd2);

        btnUpdate2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUpdate2.setText("Cập nhật");
        btnUpdate2.setPreferredSize(new java.awt.Dimension(90, 40));
        btnUpdate2.setRolloverEnabled(false);
        btnUpdate2.setSelected(true);
        btnUpdate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate2);

        btnDelete2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDelete2.setText("Xóa");
        btnDelete2.setPreferredSize(new java.awt.Dimension(90, 40));
        btnDelete2.setRolloverEnabled(false);
        btnDelete2.setSelected(true);
        btnDelete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTenSP, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(txtMaSP))
                .addGap(108, 108, 108)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSoLuong)
                    .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3))
                                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tblQLKHO.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblQLKHO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Số lượng", "Đơn giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblQLKHO.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblQLKHO.getTableHeader().setReorderingAllowed(false);
        tblQLKHO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQLKHOMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblQLKHO);

        jtfSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jtfSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfSearchFocusLost(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 51, 255));
        jLabel5.setText("Tìm kiếm:");

        btnLast.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/skip_forward.png"))); // NOI18N
        btnLast.setPreferredSize(new java.awt.Dimension(50, 50));
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
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

        btnPrev.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/previous.png"))); // NOI18N
        btnPrev.setPreferredSize(new java.awt.Dimension(50, 50));
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnFirst.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/skip_backward.png"))); // NOI18N
        btnFirst.setMaximumSize(new java.awt.Dimension(50, 50));
        btnFirst.setMinimumSize(new java.awt.Dimension(50, 50));
        btnFirst.setPreferredSize(new java.awt.Dimension(50, 50));
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnRootLayout = new javax.swing.GroupLayout(jpnRoot);
        jpnRoot.setLayout(jpnRootLayout);
        jpnRootLayout.setHorizontalGroup(
            jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRootLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpnRootLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpnRootLayout.setVerticalGroup(
            jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnRootLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpnRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnFirst.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnRoot, javax.swing.GroupLayout.PREFERRED_SIZE, 1136, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnRoot, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNew2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew2ActionPerformed
        clearFormSP();
        LoadData();
    }//GEN-LAST:event_btnNew2ActionPerformed

    private void btnDelete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete2ActionPerformed
        // TODO add your handling code here:
        removeSP();
        LoadData();
    }//GEN-LAST:event_btnDelete2ActionPerformed

    private void btnUpdate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate2ActionPerformed
        if (validateSP()) {
            UpdateSP();
            LoadData();
        }
    }//GEN-LAST:event_btnUpdate2ActionPerformed

    private void btnAdd2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd2ActionPerformed
        if (validateSP()) {
            addSanPham();
            LoadData();
        }

    }//GEN-LAST:event_btnAdd2ActionPerformed

    private void jtfSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfSearchFocusGained

    }//GEN-LAST:event_jtfSearchFocusGained

    private void jtfSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfSearchFocusLost


    }//GEN-LAST:event_jtfSearchFocusLost

    private void txtMaSPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaSPKeyTyped


    }//GEN-LAST:event_txtMaSPKeyTyped

    private void txtMaSPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaSPKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtMaSPKeyPressed

    private void txtMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaSPActionPerformed
        // TODO add your handling code here:
        if ((tblQLKHO.getModel().getValueAt(1, 1)).equals(txtMaSP.getText())) {
            txtTenSP.setText(String.valueOf(tblQLKHO.getModel().getValueAt(1, 2)));
        }
    }//GEN-LAST:event_txtMaSPActionPerformed

    private void tblQLKHOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQLKHOMouseClicked
        int index = tblQLKHO.getSelectedRow();
        filltoTable(index);
        txtMaSP.setEditable(false);
    }//GEN-LAST:event_tblQLKHOMouseClicked

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        lastSP();

    }//GEN-LAST:event_btnLastActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        nextSP();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        prevSP();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        FirtSP();
    }//GEN-LAST:event_btnFirstActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd2;
    private javax.swing.JButton btnDelete2;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNew2;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnUpdate2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpnRoot;
    private javax.swing.JTextField jtfSearch;
    private javax.swing.JTable tblQLKHO;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSP;
    // End of variables declaration//GEN-END:variables
}
