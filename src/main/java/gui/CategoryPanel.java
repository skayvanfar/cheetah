package gui;

import enums.DownloadCategory;
import gui.listener.CategoryPanelListener;
import model.dto.PreferencesDirectoryCategoryDTO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Saeed on 9/10/2015.
 */
class CategoryPanel extends JPanel {

    private JTree categoryTree;
    //  private ServerTreeCellEditor treeCellEditor;

    private List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs;

    private CategoryPanelListener categoryPanelListener;

    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages/messages"); // NOI18N

    public CategoryPanel(List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs) {

        this.preferencesDirectoryCategoryDTOs = preferencesDirectoryCategoryDTOs;

        categoryTree = new JTree(initTree(preferencesDirectoryCategoryDTOs));

        categoryTree.setBackground(new Color(238, 238, 244));
        categoryTree.setOpaque(true);

        categoryTree.setRootVisible(false);
        categoryTree.setShowsRootHandles(true);

     //   DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer(); // if defult want use
        //	treeCellRenderer.setLeafIcon(Utils.createIcon("/udemy/swinglearn/images/Server16.png"));
        //	treeCellRenderer.setOpenIcon(Utils.createIcon("/udemy/swinglearn/images/WebComponent16.png"));
        //	treeCellRenderer.setClosedIcon(Utils.createIcon("/udemy/swinglearn/images/WebComponentAdd16.png"));

        CategortyTreeCellRenderer categortyTreeCellRenderer = new CategortyTreeCellRenderer();
        categoryTree.setCellRenderer(categortyTreeCellRenderer);

        categoryTree.setEditable(false);

        categoryTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //Returns the last path element of the selection.
                //This method is useful only when the selection model allows a single selection.
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) categoryTree.getLastSelectedPathComponent();

                if (node == null)
                    //Nothing is selected.
                    return;

                Object nodeInfo = node.getUserObject();

                if (nodeInfo instanceof PreferencesDirectoryCategoryDTO) {
                    PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO = (PreferencesDirectoryCategoryDTO) nodeInfo;
                    categoryPanelListener.categoryNodeSelected(Arrays.asList(preferencesDirectoryCategoryDTO.getFileExtensions()),
                            DownloadCategory.valueOfByDesc(node.getParent().toString()));
                } else {
                    categoryPanelListener.categoryNodeSelected(null,
                            DownloadCategory.valueOfByDesc(node.toString()));
                }
            }
        });

        expandAllNodes(categoryTree, 0, categoryTree.getRowCount());

        setLayout(new BorderLayout()); /////**************88

        JScrollPane scrollPane = new JScrollPane(categoryTree);
        scrollPane.setMinimumSize(new Dimension(150, 400));
        add(scrollPane, BorderLayout.CENTER);

        Border innerBorder = BorderFactory.createTitledBorder(bundle.getString("categoryPanel.label"));
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

    }

    private DefaultMutableTreeNode initTree(List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs) {

        // Must get data from database
        DefaultMutableTreeNode top = new DefaultMutableTreeNode();

        DefaultMutableTreeNode allDownloadsBranch = new DefaultMutableTreeNode(bundle.getString("categoryPanel.allDownloadsBranch"));
   //     allDownloadsBranch.

        for (PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO : preferencesDirectoryCategoryDTOs) {
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(preferencesDirectoryCategoryDTO);
            allDownloadsBranch.add(leaf);
        }


        DefaultMutableTreeNode unfinishedBranch = new DefaultMutableTreeNode(bundle.getString("categoryPanel.unfinishedBranch"));
        for (PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO : preferencesDirectoryCategoryDTOs) {
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(preferencesDirectoryCategoryDTO);
            unfinishedBranch.add(leaf);
        }


        DefaultMutableTreeNode finishedBranch = new DefaultMutableTreeNode(bundle.getString("categoryPanel.finishedBranch"));
        for (PreferencesDirectoryCategoryDTO preferencesDirectoryCategoryDTO : preferencesDirectoryCategoryDTOs) {
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(preferencesDirectoryCategoryDTO);
            finishedBranch.add(leaf);
        }



        DefaultMutableTreeNode queuesBranch = new DefaultMutableTreeNode(bundle.getString("categoryPanel.queuesBranch"));

        //      DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London", 3, selectedServers.contains(3)));
        //      DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Edinburgh", 4, selectedServers.contains(4)));

        DefaultMutableTreeNode mainDownloadQueueLeaf = new DefaultMutableTreeNode(bundle.getString("categoryPanel.mainDownloadQueueLeaf"));
        DefaultMutableTreeNode synchronizedQueueLeaf = new DefaultMutableTreeNode(bundle.getString("categoryPanel.synchronizedQueueLeaf"));

        queuesBranch.add(mainDownloadQueueLeaf);
        queuesBranch.add(synchronizedQueueLeaf);

        top.add(allDownloadsBranch);
        top.add(unfinishedBranch);
        top.add(finishedBranch);
        top.add(queuesBranch);

        return top;
    }

    public void setTreeModel(List<PreferencesDirectoryCategoryDTO> preferencesDirectoryCategoryDTOs) {
        this.preferencesDirectoryCategoryDTOs = preferencesDirectoryCategoryDTOs;
        categoryTree.setModel(new DefaultTreeModel(initTree(preferencesDirectoryCategoryDTOs), false));
    }

    public void setCategoryPanelListener(CategoryPanelListener categoryPanelListener) {
        this.categoryPanelListener = categoryPanelListener;
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }

        if(tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }
}
