package gui;

import utils.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Created by Saeed on 9/11/2015.
 */
public class CategotyTreeCellRenderer implements TreeCellRenderer {

    private JCheckBox leafRenderer;
    private DefaultTreeCellRenderer nonLeafRenderer;

    private Color textForeground;
    private Color textBackground;
    private Color selectionForeground;
    private Color selectionBackground;

    public CategotyTreeCellRenderer() {
        leafRenderer = new JCheckBox();
        nonLeafRenderer = new DefaultTreeCellRenderer();

        nonLeafRenderer.setLeafIcon(Utils.createIcon("/images/primo/images/Server16.png"));
        nonLeafRenderer.setOpenIcon(Utils.createIcon("/udemy/swinglearn/images/WebComponent16.png"));
        nonLeafRenderer.setClosedIcon(Utils.createIcon("/udemy/swinglearn/images/WebComponentAdd16.png"));

        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus) {

        if (leaf) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
       //     ServerInfo nodeInfo = (ServerInfo) node.getUserObject();
            String nodeInfo = (String) node.getUserObject();

            if (selected) {
                leafRenderer.setForeground(selectionForeground);
           //     leafRenderer.setBackground(selectionBackground);
            }
            else {
                leafRenderer.setForeground(textForeground);
                leafRenderer.setBackground(textBackground);
            }

            leafRenderer.setText(nodeInfo.toString());
           // leafRenderer.setSelected(nodeInfo.isChecked());
            return leafRenderer;
        }
        else {
            return nonLeafRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
    }

}
