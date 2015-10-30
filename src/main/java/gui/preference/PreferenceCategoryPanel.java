package gui.preference;

import gui.listener.PreferenceCategoryPanelListener;
import org.apache.log4j.Logger;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by Saeed on 9/22/2015.
 */
class PreferenceCategoryPanel extends JPanel {

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private JTree categoryTree;
  //  private CategortyTreeCellRenderer categotyTreeCellRenderer;

    private PreferenceCategoryPanelListener preferenceCategoryPanelListener;

    public PreferenceCategoryPanel() {
        categoryTree = new JTree(initTree());
        UIManager.put("Tree.rendererFillBackground", false);

        categoryTree.setBackground(new Color(238, 238, 244));
        categoryTree.setOpaque(true);

        PreferenceCategoryTreeCellRenderer preferenceCategoryTreeCellRenderer = new PreferenceCategoryTreeCellRenderer();
        categoryTree.setCellRenderer(preferenceCategoryTreeCellRenderer);

        categoryTree.setRootVisible(false);
   //     categoryTree.setShowsRootHandles(true);

        categoryTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //Returns the last path element of the selection.
                //This method is useful only when the selection model allows a single selection.
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) categoryTree.getLastSelectedPathComponent();

                if (node == null)
                    //Nothing is selected.
                    return;

                String nodeName = (String) node.getUserObject();

                preferenceCategoryPanelListener.nodeSelectedEventOccured(nodeName);////???????????????
            }
        });

        categoryTree.setEditable(false);

        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(categoryTree);
        scrollPane.setMinimumSize(new Dimension(150, 400));
        add(scrollPane, BorderLayout.CENTER);

        Border innerBorder = BorderFactory.createTitledBorder("Category Download");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

    }

    public void setPreferenceCategoryPanelListener(PreferenceCategoryPanelListener preferenceCategoryPanelListener) {
        this.preferenceCategoryPanelListener = preferenceCategoryPanelListener;
    }

    private DefaultMutableTreeNode initTree() {

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Category");
        DefaultMutableTreeNode generalBranch = new DefaultMutableTreeNode("General");
        DefaultMutableTreeNode fileTypesBranch = new DefaultMutableTreeNode("File Types");
        DefaultMutableTreeNode saveToBranch = new DefaultMutableTreeNode("Save To");
        DefaultMutableTreeNode downloadsBranch = new DefaultMutableTreeNode("Downloads");
        DefaultMutableTreeNode connectionBranch = new DefaultMutableTreeNode("Connection");
        DefaultMutableTreeNode proxySocksBranch = new DefaultMutableTreeNode("Proxy / Socks");
        DefaultMutableTreeNode siteLoginsBranch = new DefaultMutableTreeNode("Site Logins");
        DefaultMutableTreeNode dialUpVPNBranch = new DefaultMutableTreeNode("dial Up / VPN");
        DefaultMutableTreeNode soundsBranch = new DefaultMutableTreeNode("Sounds");

        top.add(generalBranch);
        top.add(fileTypesBranch);
        top.add(saveToBranch);
        top.add(downloadsBranch);
        top.add(connectionBranch);
        top.add(proxySocksBranch);
        top.add(siteLoginsBranch);
        top.add(dialUpVPNBranch);
        top.add(soundsBranch);

        return top;
    }
}
