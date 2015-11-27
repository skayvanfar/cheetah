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

package utils;

import java.io.*;
import java.util.prefs.*;

/**
 * @author <a href="kayvanfar.sj@gmail.com">Saeed Kayvanfar</a> 9/24/2015
 */
public class PrefObj {

    // Max byte count is 3/4 max string length (see Preferences
    // documentation).
    static private final int pieceLength =
            ((3*Preferences.MAX_VALUE_LENGTH)/4);

    static private byte[] object2Bytes( Object o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        return baos.toByteArray();
    }

    static private byte[][] breakIntoPieces( byte raw[] ) {
        int numPieces = (raw.length + pieceLength - 1) / pieceLength;
        byte pieces[][] = new byte[numPieces][];
        for (int i=0; i<numPieces; ++i) {
            int startByte = i * pieceLength;
            int endByte = startByte + pieceLength;
            if (endByte > raw.length) endByte = raw.length;
            int length = endByte - startByte;
            pieces[i] = new byte[length];
            System.arraycopy( raw, startByte, pieces[i], 0, length );
        }
        return pieces;
    }

    static private void writePieces( Preferences prefs, String key,
                                     byte pieces[][] ) throws BackingStoreException {
        Preferences node = prefs.node( key );
        node.clear();
        for (int i=0; i<pieces.length; ++i) {
            node.putByteArray( ""+i, pieces[i] );
        }
    }

    static private byte[][] readPieces( Preferences prefs, String key )
            throws BackingStoreException {
        Preferences node = prefs.node( key );
        String keys[] = node.keys();
        int numPieces = keys.length;
        byte pieces[][] = new byte[numPieces][];
        for (int i=0; i<numPieces; ++i) {
            pieces[i] = node.getByteArray( ""+i, null );
        }
        return pieces;
    }

    static private byte[] combinePieces( byte pieces[][] ) {
        int length = 0;
        for (int i=0; i<pieces.length; ++i) {
            length += pieces[i].length;
        }
        byte raw[] = new byte[length];
        int cursor = 0;
        for (int i=0; i<pieces.length; ++i) {
            System.arraycopy( pieces[i], 0, raw, cursor, pieces[i].length );
            cursor += pieces[i].length;
        }
        return raw;
    }

    static private Object bytes2Object( byte raw[] )
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return o;
    }

    static public void putObject( Preferences prefs, String key, Object o )
            throws IOException, BackingStoreException, ClassNotFoundException {
        byte raw[] = object2Bytes( o );
        byte pieces[][] = breakIntoPieces( raw );
        writePieces( prefs, key, pieces );
    }

    static public Object getObject( Preferences prefs, String key )
            throws IOException, BackingStoreException, ClassNotFoundException {
        byte pieces[][] = readPieces( prefs, key );
        byte raw[] = combinePieces( pieces );
        Object o = bytes2Object( raw );
        return o;
    }
}
