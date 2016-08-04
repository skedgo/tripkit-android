package com.skedgo.android.common.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.skedgo.android.common.util.LogUtils.LOGE;
import static com.skedgo.android.common.util.LogUtils.makeTag;

public class FileUtils {

  private static final String TAG = makeTag("FileUtils");

  public static File[] listAllFiles(Context context, String dirName) {
    File dir = getInternalDir(context, dirName);
    if (dir == null) {
      return null;
    }

    return dir.listFiles(new FileFilter() {

      @Override
      public boolean accept(File file) {
        return file.isFile();
      }
    });
  }

  public static void writeTextToFile(File file, String text) throws IOException {
    FileOutputStream outputStream = new FileOutputStream(file);
    outputStream.write(text.getBytes());
    outputStream.close();
  }

  public static String readFileAsText(File file) {
    try {
      return readFileStreamAsText(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      LOGE(TAG, "Error while readFileAsText()", e);
    }

    return null;
  }

  public static String readFileStreamAsText(InputStream inputStream) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(inputStream));

      StringBuilder textBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        textBuilder.append(line).append('\n');
      }

      return textBuilder.toString();
    } catch (Exception e) {
      LOGE(TAG, "Error while readFileStreamAsText()", e);
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }

        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  public static File getInternalDir(Context context, String directoryName) {
    File internalDir = new File(context.getFilesDir(), directoryName);
    if (!internalDir.exists()) {
      if (!internalDir.mkdirs()) {
        LOGE(TAG, "Error while creating internal directory: " + directoryName);
        return null;
      }
    }

    return internalDir;
  }
}