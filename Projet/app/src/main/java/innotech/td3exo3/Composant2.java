package innotech.td3exo3;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Composant2 extends Fragment {

    boolean capture = true;
    Button envoyer;
    String s;
    StorageReference photoStoragePath, videoStoragePath;
    Uri uriPhoto, uriVideo;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int GALERY_INTENT = 2;

    private FirebaseDatabase database;
    private DatabaseReference mRootReference;
    private DatabaseReference mChildReference;
    DatabaseReference myRef;

    private long taille = 0;

    private boolean selectPhotoDone = false;
    private boolean selectVideoDone = false;

    private static int RESULT_LOAD_IMAGE = 1;
    public static StorageReference mStorageRef;
    public static Uri file;
    SessionManager sessionManager;
    String idUser;

    public static String uri;
    static Context context;
    VideoView video;

    public Composant2() {
        // Required empty public constructor
    }


    public static Fragment getInstance(Context c,String uriPhoto){
        uri = uriPhoto;
        context = c;
        System.out.println("ID dossier Video : " + uri);
        return new Composant2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        database = FirebaseDatabase.getInstance();
        //database.getInstance().setPersistenceEnabled(true);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mRootReference = database.getReference();

        mChildReference = mRootReference.child("dossiers");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        sessionManager= new SessionManager(getActivity());
        idUser = sessionManager.getIdUser();


        myRef = database.getReference("dossiers");
        myRef.keepSynced(true);


        video = (VideoView) getActivity().findViewById(R.id.videoPlay);

        MediaController md = new MediaController(context);
        md.setAnchorView(video);
//        video.setMediaController(md);

        try {

            //Uri uri = (Uri) this.getArguments().get("uriVideo");
            Uri selectedImage = Uri.parse(uri);
            System.out.println("ID dossier Video URI : " + selectedImage);
            /*video.setVideoURI(selectedImage);
            video.setMediaController(new MediaController(context));
            video.requestFocus();
            //video.start();*/
            video.setVideoURI(selectedImage);
            video.setMediaController(new MediaController(context));
            video.requestFocus();
            video.start();
            video.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    selectVideo();
                    return false;
                }
            });

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if (selectVideoDone) {
                        /*Log.d("+2+ Video : ", uriVideo.toString());
                        MyIntentService.uriFile=uriVideo;
                        Toast.makeText(InfoActivity.this,uriVideo.toString(),Toast.LENGTH_LONG).show();

                        file = uriVideo;
                        try {
                            MyIntentService.strm = getContentResolver().openInputStream(uriVideo);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent intent2 = new Intent(InfoActivity.this, MyIntentService.class);
                        intent2.putExtra("uri", uriVideo);
                        startService(intent2);*/

            myRef.child(idUser + taille).child("video").setValue(uriVideo.toString());
            Toast.makeText(getActivity(), uriVideo.toString(), Toast.LENGTH_SHORT).show();
            Log.d("--uriVideo  :", uriVideo.toString());
        }

        return inflater.inflate(R.layout.fragment_composant2, container, false);
    }



    public void onStart() {
        super.onStart();
        mChildReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taille = dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void selectVideo() {

        capture = false;
        final CharSequence[] options = {"Filmer une video", "Choisir depuis la galerie", "Quitter"};
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ajouter une video");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Filmer une video")) {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                    }
                } else if (options[item].equals("Choisir depuis la galerie")) {
                    /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("video/*");
                    startActivityForResult(intent, GALERY_INTENT);
                } else if (options[item].equals("Quitter")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {

            if (capture) {

            }
            else {
                video.setVideoURI(data.getData());
                video.setMediaController(new MediaController(context));
                video.requestFocus();
                video.start();

                System.out.println("ID dossier Video DATA : " + data.getData());

                uriVideo = data.getData();
                //photoStoragePath = mStorage.child("Videos").child(uriVideo.getLastPathSegment());
                selectVideoDone = true;
                Log.d("+1+ Video : ", uriVideo.toString());

                Log.d("+2+ Video : ", uriVideo.toString());
                MyIntentService.uriFile=uriVideo;

                file = uriVideo;
                try {
                    MyIntentService.strm = getActivity().getContentResolver().openInputStream(uriVideo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Intent intent2 = new Intent(getActivity(), MyIntentService.class);
                intent2.putExtra("uri", uriVideo);
                getActivity().startService(intent2);


            }

        }
    }

}
