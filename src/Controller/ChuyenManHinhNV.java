/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DanhMuc.DanhMuc;
import View.QLNVPanel;
import View.QLSPPanel;
import View.TKSPPanel;
import View.TTSPPanel;
import View.TrangChuPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Nguyen Dung
 */
public class ChuyenManHinhNV {

    private JPanel root;
    private String kindSelected = "";
    private List<DanhMuc> listItem=null;
    public ChuyenManHinhNV(JPanel jpnRoot) {
        this.root = jpnRoot;
    }

    public void setView(JPanel jpnItem, JLabel jlbItem) {

        kindSelected = "TrangChu";
        jpnItem.setBackground(new Color(83,113,253));
        jlbItem.setBackground(new Color(83,113,253));

        root.removeAll();
        root.setLayout(new BorderLayout());
        root.add(new TrangChuPanel());
        root.validate();
        root.repaint();
    }

    public void setEvent(List<DanhMuc> listItem) {
        this.listItem=listItem;
        for (DanhMuc item : listItem) {
            item.getJlb().addMouseListener(new LabelEvent(item.getKind(),item.getJpn(),item.getJlb()));
        }
    }

    class LabelEvent implements MouseListener {

        private JPanel node;

        private String kind;
        private JPanel jpnItem;
        private JLabel jlbItem;

        public LabelEvent(String kind, JPanel jpnItem, JLabel jlbItem) {
            this.kind = kind;
            this.jpnItem = jpnItem;
            this.jlbItem = jlbItem;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (kind) {
                case "TrangChu":
                    node = new TrangChuPanel();
                    break;
                case "TTSP":
                    node = new TTSPPanel();
                    break;
                case "TKSP":
                    node = new TKSPPanel();
                    break;
                default:
                    node = new TrangChuPanel();
                    break;
            }
            root.removeAll();
            root.setLayout(new BorderLayout());
            root.add(node);
            root.validate();
            root.repaint();
            setChangeBackGroundColor(kind);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected=kind;
            jpnItem.setBackground(new Color(26,42,87));
            jlbItem.setBackground(new Color(26,42,87));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            jpnItem.setBackground(new Color(26,42,87));
            jlbItem.setBackground(new Color(26,42,87));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!kindSelected.equalsIgnoreCase(kind)){
                jpnItem.setBackground(new Color(83,113,253));
                jlbItem.setBackground(new Color(83,113,253));
            }
        }

    }
    
    private void setChangeBackGroundColor(String kind){
        for(DanhMuc item:listItem){
            if(item.getKind().equalsIgnoreCase(kind)){
                item.getJpn().setBackground(new Color(26,42,87));
                item.getJlb().setBackground(new Color(26,42,87));
            }else{
                item.getJpn().setBackground(new Color(83,113,253));
                item.getJlb().setBackground(new Color(83,113,253));
            }
        }
    }
}
