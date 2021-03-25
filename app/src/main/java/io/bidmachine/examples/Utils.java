package io.bidmachine.examples;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class Utils {

    @Nullable
    static String readFile(@NonNull Context context, @NonNull String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            return readInputStream(inputStream);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private static String readInputStream(@NonNull InputStream inputStream) {
        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder(inputStream.available());
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ignore) {

            }
        }
    }

}