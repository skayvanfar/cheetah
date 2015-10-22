package comparator;

import utils.FileUtil;

import java.io.File;
import java.util.Comparator;

/**
 * Created by Saeed on 10/22/2015.
 */
public class FileNameComparator implements Comparator<File> {
    @Override
    public int compare(File file1, File file2) {
        return FileUtil.getFileName(file1).compareToIgnoreCase(FileUtil.getFileName(file2));
    }
}
