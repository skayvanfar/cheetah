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

import javax.swing.*;
import java.awt.*;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/25/2015
 */
class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "About...", false);

        setLayout(new BorderLayout());

        setBackground(Color.WHITE);

        AboutPanel aboutPanel = new AboutPanel();
        add(aboutPanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(350,250));
        setSize(550, 200);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

}
