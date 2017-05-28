package innotech.td3exo3;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Composant3 extends Fragment {

    private static String uri;

    ImageView photo;
    public static Uri file;

    public Composant3() {
        // Required empty public constructor
    }

    public static Fragment getInstance(String uriPhoto){
        uri = uriPhoto;
        System.out.println("ID dossier Photo : " + uri);
        return new Composant3();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        photo = (ImageView) getActivity().findViewById(R.id.photo);

 //       Uri uri = (Uri) this.getArguments().get("uriPhoto");
        System.out.println("ID dossier Photo : " + uri);
    try {
        Uri selectedImage = Uri.parse(uri);
        System.out.println("ID dossier Photo URI : " + selectedImage.toString());
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String picturePath = c.getString(columnIndex);
        c.close();
        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
        Log.w("image path", picturePath + "");
        photo.setImageBitmap(thumbnail);
        //photo_bitmap = thumbnail;
    }catch (NullPointerException e){
        e.printStackTrace();
    }
        return inflater.inflate(R.layout.fragment_composant3, container, false);
    }
}
