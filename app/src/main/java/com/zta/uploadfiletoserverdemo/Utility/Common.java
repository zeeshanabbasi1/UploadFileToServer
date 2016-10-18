package com.zta.uploadfiletoserverdemo.Utility;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

/**
 * Created by Zee Abbasi on 10/14/2016.
 */

 public class Common {

    /**
     * Checks the given path of file contains Image file extension "jpg", "png", "gif","jpeg"
     * If Contains return TRUE else FALSE
     *
     * @param filePath
     * @return
     */
    public static Boolean isImageFile(String filePath){
        final String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};

        for (String extension : okFileExtensions)
        {
            if (filePath.toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the path of selected file
     * @param uri
     * @return
     */
    public static String getSelectedFilePath(Context context,Uri uri){

        return new String(RealPathUtil.getFilePath(context, uri));

    }
}
