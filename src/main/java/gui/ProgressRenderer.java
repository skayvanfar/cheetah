package gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by Saeed on 9/10/2015.
 */

// This class renders a JProgressBar in a table cell.
public class ProgressRenderer extends JProgressBar implements TableCellRenderer {

    private NumberFormat percentFormat;

    // Constructor for ProgressRenderer.
    public ProgressRenderer(int min, int max) {
        super(min, max);
        percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
    }

    /* Returns this JProgressBar as the renderer
       for the given table cell. */
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
    {
        // Set JProgressBar's percent complete value.
    //    setValue((int) ((Float) value).floatValue());
   //     setString(((Float) value).floatValue() + "%");

        setString(percentFormat.format(((Float) value).floatValue()));

        return this;
    }
}
