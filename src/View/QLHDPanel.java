package View;

import DAO.DBConnection;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class QLHDPanel extends javax.swing.JPanel {

    Vector data = null;
    PreparedStatement ps = null;
    Connection con = null;
    ResultSet rs = null;
    Statement st = null;
    String header[] = {"Mã hóa đơn", "Mã người bán", "Ngày tạo hóa đơn", "Tổng tiền"};
    String header2[] = {"Mã hóa đơn", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng","Tổng tiền"};
    DefaultTableModel mode = new DefaultTableModel(header, 0);
    DefaultTableModel mode2 = new DefaultTableModel(header2, 0);

    public QLHDPanel() {
        initComponents();
        loadHoaDon();
        DecoTable();
        DecoTableHDCT();
    }

    void DecoTable() {
        tblHoaDon.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 20));
        tblHoaDon.getTableHeader().setBackground(new Color(83, 113, 253));
        tblHoaDon.getTableHeader().setForeground(Color.white);
        tblHoaDon.setRowHeight(26);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblHoaDon.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblHoaDon.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblHoaDon.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblHoaDon.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

    }

    void DecoTableHDCT() {
        tblHDCT.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 20));
        tblHDCT.getTableHeader().setBackground(new Color(83, 113, 253));
        tblHDCT.getTableHeader().setForeground(Color.white);
        tblHDCT.setRowHeight(26);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblHDCT.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblHDCT.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblHDCT.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblHDCT.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblHDCT.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    void loadHoaDon() {
        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select * from hoadon");
            while (rs.next()) {
                data = new Vector();
                data.add(rs.getInt(1));
                data.add(rs.getString(2));
                data.add(rs.getDate(3));
                data.add(rs.getFloat(4));
                mode.addRow(data);

            }
            tblHoaDon.setModel(mode);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void loadHDCT() {
        try {
            // DecoTableHDCT();
            int mahd = (int) tblHoaDon.getValueAt(tblHoaDon.getSelectedRow(), 0);
            mode2.setRowCount(0);
            tblHoaDon.getValueAt(tblHoaDon.getSelectedRow(), 1);
            con = DBConnection.getConnection();
            ps = con.prepareStatement("select * from hdct where mahd=?");
            ps.setInt(1, mahd);
            rs = ps.executeQuery();
            while (rs.next()) {
                data = new Vector();
                data.add(rs.getInt(1));
                data.add(rs.getString(2));
                data.add(rs.getString(3));
                data.add(rs.getFloat(4));
                data.add(rs.getInt(5));
                data.add(String.valueOf(rs.getFloat(4)*rs.getInt(5)));
                mode2.addRow(data);
            }
            tblHDCT.setModel(mode2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Root = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHDCT = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(153, 51, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(153, 102, 255));

        tblHoaDon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Hóa Đơn", "Mã Người Bán", "Ngày Tạo Hóa Đơn", "Tổng Tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hóa đơn chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(102, 102, 255))); // NOI18N

        tblHDCT.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblHDCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblHDCT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(226, 226, 226))
        );

        javax.swing.GroupLayout RootLayout = new javax.swing.GroupLayout(Root);
        Root.setLayout(RootLayout);
        RootLayout.setHorizontalGroup(
            RootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RootLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        RootLayout.setVerticalGroup(
            RootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RootLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        //DecoTableHDCT();
        loadHDCT();
    }//GEN-LAST:event_tblHoaDonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Root;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblHDCT;
    private javax.swing.JTable tblHoaDon;
    // End of variables declaration//GEN-END:variables
}
