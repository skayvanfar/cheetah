/*
 * Cheetah - A Free Fast Downloader
 *
 * Copyright © 2019 Saeed Kayvanfar
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

package concurrent;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> on 09/08/2019.
 *
 * lets you provide a thread name,
 * sets  a  custom  UncaughtException-Handler  that  writes  a  message  to  a  Logger,  maintains  statistics  on  how  many
 * threads have been created and destroyed, and optionally writes a debug message to the log when a thread is created or
 * terminates.
 */
public class MyAppThread extends Thread {

    public static final String DEFAULT_NAME = "MyAppThread";

    private static volatile boolean debugLifecycle = false;

    private static final AtomicInteger created = new AtomicInteger();

    private static final AtomicInteger alive = new AtomicInteger();

    // Logger
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public MyAppThread(Runnable r) { this(r, DEFAULT_NAME); }

    public MyAppThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t,
                                                  Throwable e) {
                        logger.log(Level.FATAL,
                                "UNCAUGHT in thread " + t.getName(), e);
                    }
                });
    }

    public void run() {
// Copy debug flag to ensure consistent value throughout.
        boolean debug = debugLifecycle;
        if (debug) logger.log(Level.INFO, "Created "+getName());
        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) logger.log(Level.INFO, "Exiting "+getName());
        }
    }

    public static int getThreadsCreated() { return created.get(); }

    public static int getThreadsAlive() { return alive.get(); }

    public static boolean getDebug() { return debugLifecycle; }

    public static void setDebug(boolean b) { debugLifecycle = b; }
}
