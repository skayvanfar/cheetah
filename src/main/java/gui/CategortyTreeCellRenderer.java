/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2015 Saeed Kayvanfar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package gui;

import model.dto.PreferencesDirectoryCategoryDTO;
import utils.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/11/2015
 */
class CategortyTreeCellRenderer implements TreeCellRenderer {

    private JLabel leafRenderer;
 //   private DefaultTreeCellRenderer nonLeafRenderer;

    private Color textForeground;
    private Color textBackground;
    private Color selectionForeground;
    private Color selectionBackground;

    private ResourceBundle defaultPreferencesBundle = java.util.ResourceBundle.getBundle("defaultPreferences"); // NOI18N

    public CategortyTreeCellRenderer() {

        //     leafRenderer = new JCheckBox();
        leafRenderer = new JLabel();
//        nonLeafRenderer = new DefaultTreeCellRenderer();
//
//        nonLeafRenderer.setLeafIcon(Utils.createIcon("/images/primo48/others/all_download.png"));
//        nonLeafRenderer.setOpenIcon(Utils.createIcon("/images/primo48/others/unfinished.png"));
//        nonLeafRenderer.setClosedIcon(Utils.createIcon("/images/primo48/others/finished.png"));

        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object nodeInfo = node.getUserObject();
        if (nodeInfo instanceof PreferencesDirectoryCategoryDTO) {
            PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO = (PreferencesDirectoryCategoryDTO) nodeInfo;
            String iconPath = preferencesDirectoryCategoryDTO.getIconPath();

            if (iconPath != null) {
                leafRenderer.setIcon(Utils.createIcon(iconPath));
            } else {
                leafRenderer.setIcon(Utils.createIcon("/images/primo48/others/unknown.png"));
            }


            if (selected) {
                leafRenderer.setForeground(Color.blue); // selectionForeground
          //      leafRenderer.setBackground(Color.magenta);
                //     leafRenderer.setBackground(selectionBackground);
            }
            else {
                leafRenderer.setForeground(textForeground);
                leafRenderer.setBackground(textBackground);
            }

            leafRenderer.setText(nodeInfo.toString());
        }
        else {
            if (nodeInfo != null) {
                String nodeName = (String)nodeInfo;
                switch (nodeName) {
                    case "All Downloads":
                        leafRenderer.setIcon(Utils.createIcon(defaultPreferencesBundle.getString("categoryTreeCellRenderer.allDownload")));
                        break;
                    case "Unfinished":
                        leafRenderer.setIcon(Utils.createIcon(defaultPreferencesBundle.getString("categoryTreeCellRenderer.unfinished")));
                        break;
                    case "Finished":
                        leafRenderer.setIcon(Utils.createIcon(defaultPreferencesBundle.getString("categoryTreeCellRenderer.finished")));
                        break;
                    case "Queues":
                        leafRenderer.setIcon(Utils.createIcon(defaultPreferencesBundle.getString("categoryTreeCellRenderer.queues")));
                        break;
                }
                leafRenderer.setText(nodeInfo.toString());

                if (selected) {
                    leafRenderer.setForeground(Color.blue); // selectionForeground
                //    leafRenderer.setBackground(Color.magenta);
                    //     leafRenderer.setBackground(selectionBackground);
                }
                else {
                    leafRenderer.setForeground(textForeground);
                    leafRenderer.setBackground(textBackground);
                }
            }
        }
        return leafRenderer;
    }

}
