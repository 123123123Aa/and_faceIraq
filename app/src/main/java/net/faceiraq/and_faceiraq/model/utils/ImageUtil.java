package net.faceiraq.and_faceiraq.model.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class ImageUtil {

    private static final String TAG = "ImageUtil";

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        if (image == null) {
            Log.d(TAG, "Bitmap image = null");
            return "";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static byte[] convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap convertToBitmap(byte[] byteArray) throws Exception {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

}
