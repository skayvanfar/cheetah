package gui.preference;

import gui.listener.PreferenceCategoryPanelListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Saeed on 9/22/2015.
 */
public class PreferenceCategoryPanel extends JPanel implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked= (JButton) e.getSource();
//        if (mainToolbarListener != null) {
        //          if (clicked == newDownloadButton) {
        //            mainToolbarListener.newDownloadEventOccured();
        //      } else if (clicked == pauseButton) {
        //        mainToolbarListener.pauseEventOccured();
        //  } else if (clicked == resumeButton) {
        //         mainToolbarListener.resumeEventOccured();
        //    } else if (clicked == cancelButton) {
        //       mainToolbarListener.cancelEventOccured();
        // } else if (clicked == clearButton) {
        //     mainToolbarListener.clearEventOccured();
        //  }
        //  }
    }

    private JTree categoryTree;
  //  private CategotyTreeCellRenderer categotyTreeCellRenderer;

    private PreferenceCategoryPanelListener preferenceCategoryPanelListener;

    public PreferenceCategoryPanel() {
        categoryTree = new JTree(initTree());

        categoryTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                preferenceCategoryPanelListener.generalSelectedEventOccured();////???????????????
            }
        });

        categoryTree.setEditable(true);

        setLayout(new BorderLayout()); /////**************88

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
        // Must get data from database
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("category");

        DefaultMutableTreeNode allDownloadsBranch = new DefaultMutableTreeNode("Preferences");

        //     DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo("New York", 0, selectedServers.contains(0)));
        //      DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(new ServerInfo("Boston", 1, selectedServers.contains(1)));
        //      DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo("Los Angles", 2, selectedServers.contains(2)));
        DefaultMutableTreeNode compressedLeaf = new DefaultMutableTreeNode("General");
        DefaultMutableTreeNode documentsLeaf = new DefaultMutableTreeNode("Download");
        DefaultMutableTreeNode musicsLeaf = new DefaultMutableTreeNode("Save To");
        DefaultMutableTreeNode programsLeaf = new DefaultMutableTreeNode("File Types");
        DefaultMutableTreeNode videosLeaf = new DefaultMutableTreeNode("Connection");

        allDownloadsBranch.add(compressedLeaf);
        allDownloadsBranch.add(documentsLeaf);
        allDownloadsBranch.add(musicsLeaf);
        allDownloadsBranch.add(programsLeaf);
        allDownloadsBranch.add(videosLeaf);


        DefaultMutableTreeNode unfinishedBranch = new DefaultMutableTreeNode("Unfinished");

        //      DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London", 3, selectedServers.contains(3)));
        //      DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Edinburgh", 4, selectedServers.contains(4)));

        DefaultMutableTreeNode compressedUnfinishedLeaf = new DefaultMutableTreeNode("Compressed");
        DefaultMutableTreeNode documentsUnfinishedLeaf = new DefaultMutableTreeNode("Documents");
        DefaultMutableTreeNode musicsUnfinishedLeaf = new DefaultMutableTreeNode("musics");
        DefaultMutableTreeNode programsUnfinishedLeaf = new DefaultMutableTreeNode("Programs");
        DefaultMutableTreeNode videosUnfinishedLeaf = new DefaultMutableTreeNode("Videos");


        unfinishedBranch.add(compressedUnfinishedLeaf);
        unfinishedBranch.add(documentsUnfinishedLeaf);
        unfinishedBranch.add(musicsUnfinishedLeaf);
        unfinishedBranch.add(programsUnfinishedLeaf);
        unfinishedBranch.add(videosUnfinishedLeaf);


        DefaultMutableTreeNode finishedBranch = new DefaultMutableTreeNode("Finished");

        //      DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London", 3, selectedServers.contains(3)));
        //      DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Edinburgh", 4, selectedServers.contains(4)));

        DefaultMutableTreeNode compressedFinishedLeaf = new DefaultMutableTreeNode("Compressed");
        DefaultMutableTreeNode documentsFinishedLeaf = new DefaultMutableTreeNode("Documents");
        DefaultMutableTreeNode musicsFinishedLeaf = new DefaultMutableTreeNode("musics");
        DefaultMutableTreeNode programsFinishedLeaf = new DefaultMutableTreeNode("Programs");
        DefaultMutableTreeNode videosFinishedLeaf = new DefaultMutableTreeNode("Videos");


        finishedBranch.add(compressedFinishedLeaf);
        finishedBranch.add(documentsFinishedLeaf);
        finishedBranch.add(musicsFinishedLeaf);
        finishedBranch.add(programsFinishedLeaf);
        finishedBranch.add(videosFinishedLeaf);



        DefaultMutableTreeNode queuesBranch = new DefaultMutableTreeNode("Queues");

        //      DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London", 3, selectedServers.contains(3)));
        //      DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Edinburgh", 4, selectedServers.contains(4)));

        DefaultMutableTreeNode mainDownloadQueueLeaf = new DefaultMutableTreeNode("Main Download Queue");
        DefaultMutableTreeNode synchronizedQueueLeaf = new DefaultMutableTreeNode("Synchronized Queue");

        queuesBranch.add(mainDownloadQueueLeaf);
        queuesBranch.add(synchronizedQueueLeaf);


        top.add(allDownloadsBranch);
        top.add(unfinishedBranch);
        top.add(finishedBranch);
        top.add(queuesBranch);

        return top;
    }
}
