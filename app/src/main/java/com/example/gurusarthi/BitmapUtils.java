package com.example.gurusarthi;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BitmapUtils {

    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        File imageFile = saveBitmapToFile(context, bitmap);
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imageFile);
    }

    private static File saveBitmapToFile(Context context, Bitmap bitmap) {
        File imagesDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_images");
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile = new File(imagesDir, imageFileName + ".jpg");

        try {
            OutputStream os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
