package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
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

        DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer(); // if defult want use
        //	treeCellRenderer.setLeafIcon(Utils.createIcon("/udemy/swinglearn/images/Server16.png"));
        //	treeCellRenderer.setOpenIcon(Utils.createIcon("/udemy/swinglearn/images/WebComponent16.png"));
        //	treeCellRenderer.setClosedIcon(Utils.createIcon("/udemy/swinglearn/images/WebComponentAdd16.png"));

        //     categoryTree.setCellRenderer(categotyTreeCellRenderer);
  //      categoryTree.setCellEditor(treeCellEditor);
        categoryTree.setEditable(true);

        setLayout(new BorderLayout()); /////**************88

        JScrollPane scrollPane = new JScrollPane(categoryTree);
        scrollPane.setMinimumSize(new Dimension(150, 400));
        add(scrollPane, BorderLayout.CENTER);

        Border innerBorder = BorderFactory.createTitledBorder("Category Download");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

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
