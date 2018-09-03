package com.example.asus.taskbookslife;

import android.os.Environment;

/**
 * Created by WILANDA on 16/8/2018.
 */

public class CheckPenyimpanan {
    public static boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
