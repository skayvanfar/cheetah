package gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Set;

/**
 * Created by Saeed on 9/10/2015.
 */
public class CategoryPanel extends JPanel {

    private JTree categoryTree;
    private CategotyTreeCellRenderer categotyTreeCellRenderer;
  //  private ServerTreeCellEditor treeCellEditor;

    private Set<Integer> selectedServers;


    public CategoryPanel() {

        categoryTree = new JTree(initTree());
   //     categoryTree.setCellRenderer(categotyTreeCellRenderer);
  //      categoryTree.setCellEditor(treeCellEditor);
        categoryTree.setEditable(true);

        add(new JScrollPane(categoryTree));

    }

    private DefaultMutableTreeNode initTree() {
        // Must get data from database
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("category");

        DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("All Downloads");

   //     DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo("New York", 0, selectedServers.contains(0)));
  //      DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(new ServerInfo("Boston", 1, selectedServers.contains(1)));
  //      DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo("Los Angles", 2, selectedServers.contains(2)));
        DefaultMutableTreeNode server1 = new DefaultMutableTreeNode("Compressed");
        DefaultMutableTreeNode server2 = new DefaultMutableTreeNode("Documents");
        DefaultMutableTreeNode server3 = new DefaultMutableTreeNode("musics");

        branch1.add(server1);
        branch1.add(server2);
        branch1.add(server3);

        DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("Finished");

  //      DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London", 3, selectedServers.contains(3)));
  //      DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Edinburgh", 4, selectedServers.contains(4)));

        DefaultMutableTreeNode server4 = new DefaultMutableTreeNode("Compressed");
        DefaultMutableTreeNode server5 = new DefaultMutableTreeNode("Documents");

        branch2.add(server4);
        branch2.add(server5);

        top.add(branch1);
        top.add(branch2);

        return top;
    }
}
