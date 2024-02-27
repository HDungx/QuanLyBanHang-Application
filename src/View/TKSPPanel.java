/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Class.SanPham;
import DAO.DBConnection;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Nguyen Dung
 */
public class TKSPPanel extends javax.swing.JPanel {
    
    Connection con=null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    List<SanPham> listSP=new ArrayList<>();
    private TableRowSorter<TableModel> rowSorter = null;
    
    public TKSPPanel() {
        initComponents();
        LoadData();
        //----Sort
        rowSorter = new TableRowSorter<TableModel>(tblTimKiemSP.getModel());
        tblTimKiemSP.setRowSorter(rowSorter);
        //-----------------------------------------------------//
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
            }
        });
    
         tblTimKiemSP.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 20));
        tblTimKiemSP.getTableHeader().setBackground(new Color(83,113,253));
        tblTimKiemSP.getTableHeader().setForeground(Color.white);
        tblTimKiemSP.setRowHeight(26);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        
        tblTimKiemSP.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblTimKiemSP.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblTimKiemSP.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblTimKiemSP.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblTimKiemSP.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
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
            DefaultTableModel model = (DefaultTableModel) tblTimKiemSP.getModel();
            model.setRowCount(0);
            for (SanPham sp : listSP) {
                Object[] row = new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(),
                    sp.getDonGia(),sp.getTinhTrang()};
                model.addRow(row);   
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTimKiemSP = new javax.swing.JTable();
        jtfSearch = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        tblTimKiemSP.setAutoCreateRowSorter(true);
        tblTimKiemSP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblTimKiemSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Tình trạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTimKiemSP.setToolTipText("");
        tblTimKiemSP.setAutoscrolls(false);
        tblTimKiemSP.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblTimKiemSP);
        if (tblTimKiemSP.getColumnModel().getColumnCount() > 0) {
            tblTimKiemSP.getColumnModel().getColumn(0).setMinWidth(80);
            tblTimKiemSP.getColumnModel().getColumn(0).setMaxWidth(80);
            tblTimKiemSP.getColumnModel().getColumn(1).setMinWidth(300);
            tblTimKiemSP.getColumnModel().getColumn(1).setMaxWidth(300);
            tblTimKiemSP.getColumnModel().getColumn(2).setMinWidth(120);
            tblTimKiemSP.getColumnModel().getColumn(2).setMaxWidth(120);
            tblTimKiemSP.getColumnModel().getColumn(3).setMinWidth(140);
            tblTimKiemSP.getColumnModel().getColumn(3).setMaxWidth(140);
            tblTimKiemSP.getColumnModel().getColumn(4).setResizable(false);
        }

        jtfSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jtfSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfSearchActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search (2).png"))); // NOI18N
        jLabel1.setText("Tìm kiếm");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel1.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jtfSearch))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jtfSearch;
    private javax.swing.JTable tblTimKiemSP;
    // End of variables declaration//GEN-END:variables
}
