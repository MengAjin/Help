package com.example.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.Class.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 丽俐 on 2015/8/5.
 */
public class FileUtil {
    public static String saveFile(Context context, Bitmap bmp, int id){
        Person person;
        //图片保存的路径
        String path = null;
        //保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Temp_help");
        if(!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        person = new Person();
        String fileName1 = Integer.toString(id)+".jpg";
        File file = new File(appDir, fileName1);
        path = file.getAbsolutePath();
        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把文件插入到系统图库

        try{
            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        return path;
    }
}
