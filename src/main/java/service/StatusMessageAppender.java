/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2017 Saeed Kayvanfar
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

package service;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 6/10/17.
 */
public class StatusMessageAppender extends AppenderSkeleton {

    private final JTextArea jTextArea;

    private long lineNumber;

    public StatusMessageAppender(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    @Override
    protected void append(LoggingEvent event) {
        lineNumber += 1L;
        //    if(event.getLevel().equals(Level.INFO)){
        jTextArea.append(lineNumber + ". " + event.getMessage().toString() + "\n");
        //   }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}
