package innotech.td3exo3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InfoActivity extends AppCompatActivity {

    TextView type;
    Calendar calender = Calendar.getInstance();
    int year_x, month_x, day_x;
    VideoView video;
    TextView date;
    ImageView photo;
    ImageView add;
    TextView lieu;
    EditText montant;
    Bitmap photo_bitmap;
    private String photoPath;
    EditText name;
    EditText prename;
    ArrayList<Personne> data = new ArrayList<Personne>();
    PersonAdapter adapter;
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
    //private DatabaseReference myRef2 = mRootReference.child("dossiers");

    private long taille = 0;

    private boolean selectPhotoDone = false;
    private boolean selectVideoDone = false;

    private static int RESULT_LOAD_IMAGE = 1;
    public static StorageReference mStorageRef;
    public static Uri file;
    SessionManager sessionManager;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //database.getInstance().setPersistenceEnabled(true);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance();
        //database.getInstance().setPersistenceEnabled(true);
        mRootReference = database.getReference();

        mChildReference = mRootReference.child("dossiers");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        sessionManager= new SessionManager(getApplication());
        idUser = sessionManager.getIdUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        type = (TextView) findViewById(R.id.type);
        type.setText(getIntent().getStringExtra("type"));

        year_x = calender.get(Calendar.YEAR);
        month_x = calender.get(Calendar.MONTH);
        day_x = calender.get(Calendar.DAY_OF_MONTH);

        date = (TextView) findViewById(R.id.datetext);

        year_x = calender.get(Calendar.YEAR);
        month_x = calender.get(Calendar.MONTH);
        day_x = calender.get(Calendar.DAY_OF_MONTH);
        date.setText(day_x + " - " + (month_x+1) + " - " + year_x);
        lieu = (EditText) findViewById(R.id.lieu);
        montant = (EditText) findViewById(R.id.montant);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDatePicker();
            }
        });
        photo = (ImageView) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        video = (VideoView) findViewById(R.id.video);
        MediaController md = new MediaController(this);
        md.setAnchorView(video);
        video.setMediaController(md);
        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectVideo();
                return false;
            }
        });

        adapter = new PersonAdapter(this,data);

        ListView listView = (ListView) findViewById(R.id.info_perso);
        listView.setAdapter(adapter);

        data.add(new Personne("Nom", "Penom"));

        name = (EditText) findViewById(R.id.name);
        prename = (EditText) findViewById(R.id.prename);
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = name.getText().toString();
                String prenom = prename.getText().toString();
                data.add(new Personne(nom,prenom));
                adapter.notifyDataSetChanged();
            }
        });

/*      FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("scores");
        scoresRef.keepSynced(true);
*/

        myRef = database.getReference("dossiers");
        myRef.keepSynced(true);

        envoyer = (Button) findViewById(R.id.button);
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size()>1 && !lieu.getText().toString().equals("") && !montant.getText().toString().equals("")){
                    Log.i("Send email", "");
                    String[] TO = {"dt_mecharnia@esi.dz"};
                    String[] CC = {""};
                    String listeDesPersonne = "";
                    String message = "Le type d'accedent : " + getIntent().getStringExtra("type") +"\n\nLa date : " + day_x + " - " + (month_x+1) + " - " + year_x + "\nLe lieu d'accident : " + lieu.getText().toString() + "\n";
                    for (int i = 1 ; i < data.size() ; i++){
                        message += "\n\n- Nom de la personne " + (i) + " : " + data.get(i).getName() + " .";
                        message += "\n- Prénomom de la personne " + (i) + " : " + data.get(i).getPrename() + " .";

                        listeDesPersonne += "- Nom de la personne " + (i) + " : " + data.get(i).getName() + " .";
                        listeDesPersonne += "\n- Prénomom de la personne " + (i) + " : " + data.get(i).getPrename() + " .\n";
                    }

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);

                    emailIntent.setData(Uri.parse("mailto:dt_mecharnia@esi.dz"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Déclaration");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                    //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+photoPath));
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(photo.toString()));

                    // Write a message to the database

                    System.out.println("ID dossier : " + idUser);

                    myRef.child(idUser + taille).child("id").setValue(idUser + taille);
                    myRef.child(idUser + taille).child("type").setValue(type.getText().toString());
                    myRef.child(idUser + taille).child("etat").setValue("ouvert");
                    myRef.child(idUser + taille).child("lieu").setValue(lieu.getText().toString());
                    Log.d("--lieu :", lieu.getText().toString());
                    myRef.child(idUser + taille).child("date").setValue(date.getText().toString());
                    myRef.child(idUser + taille).child("liste").setValue(listeDesPersonne);
                    if (selectPhotoDone) {
                        /*MyIntentService.uriFile=uriPhoto;
                        Toast.makeText(InfoActivity.this,uriPhoto.toString(),Toast.LENGTH_LONG).show();
                        file = uriPhoto;
                        try {
                            MyIntentService.strm = getContentResolver().openInputStream(uriPhoto);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent intent1 = new Intent(InfoActivity.this, MyIntentService.class);
                        intent1.putExtra("uri", uriPhoto);
                        startService(intent1);*/
                        myRef.child(idUser + taille).child("photo").setValue(uriPhoto.toString());
                        Log.d("--uri  :", uriPhoto.toString());
                    }

                    //myRef.child(idUser + taille).child("photo").setValue(photoStoragePath.toString());

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
                        Log.d("--uriVideo  :", uriVideo.toString());
                    }
                  //  myRef.child(idUser + taille).child("video").setValue(videoStoragePath.toString());
                    myRef.child(idUser + taille).child("montant").setValue(montant.getText().toString());

                    taille ++;

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Finished sending email.", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(InfoActivity.this, "Il y a pas de client e-mail installé.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(InfoActivity.this, "Il faut remplire tous les champs.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mChildReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taille = dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(InfoActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
          });
    }

    private void showDatePicker() {

        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", year_x);
        args.putInt("month", month_x);
        args.putInt("day", day_x);
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            date.setText(String.valueOf(dayOfMonth) + " - " + String.valueOf(monthOfYear) + " - " + String.valueOf(year));
        }
    };
    public void selectImage() {

        capture = true;
        final CharSequence[] options = {"Prendre une photo", "Choisir depuis la galerie", "Quitter"};
        Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ajouter une photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Prendre une photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choisir depuis la galerie")) {
                    /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALERY_INTENT);
                } else if (options[item].equals("Quitter")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    public void selectVideo() {

        capture = false;
        final CharSequence[] options = {"Filmer une video", "Choisir depuis la galerie", "Quitter"};
        Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ajouter une video");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Filmer une video")) {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {

            if (capture) {
                Date d = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
                String s = format.format(d);

                if (resultCode == InfoActivity.RESULT_OK) {
                    if (requestCode == 1) {
                        File f = new File(Environment.getExternalStorageDirectory().toString());
                        for (File temp : f.listFiles()) {
                            if (temp.getName().equals("temp.jpg")) {
                                f = temp;
                                break;
                            }
                        }
                        try {
                            Bitmap bitmap;
                            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                            photo.setImageBitmap(bitmap);
                            photo_bitmap = bitmap;
                            String path = Environment.getExternalStorageDirectory()
                                    + File.separator
                                    + "Phoenix" + File.separator + "default";
                            f.delete();
                            OutputStream outFile = null;
                            File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                            try {
                                outFile = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                                outFile.flush();
                                outFile.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (requestCode == 2) {
                        Uri selectedImage = data.getData();
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);
                        c.close();
                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                        Log.w("image path", picturePath + "");
                        photo.setImageBitmap(thumbnail);
                        photo_bitmap = thumbnail;

                    }
                    photoPath = "ACC_" + s;
                    createDirectoryAndSaveFile(photo_bitmap, photoPath);

                    System.out.println("ID dossier Photo DATA : " + data.getData());

                    uriPhoto = data.getData();

                    MyIntentService.uriFile=uriPhoto;
                    //Toast.makeText(InfoActivity.this,uriPhoto.toString(),Toast.LENGTH_LONG).show();
                    file = uriPhoto;
                    try {
                        MyIntentService.strm = getContentResolver().openInputStream(uriPhoto);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent intent1 = new Intent(InfoActivity.this, MyIntentService.class);
                    intent1.putExtra("uri", uriPhoto);
                    startService(intent1);

                    /*Uri selectedImage = data.getData();
                    MyIntentService.uriFile=selectedImage;
                    Toast.makeText(InfoActivity.this,selectedImage.toString(),Toast.LENGTH_LONG).show();

                    file = selectedImage;
                    try {
                        MyIntentService.strm = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, MyIntentService.class);
                    intent.putExtra("uri", selectedImage);
                    startService(intent);*/
                }



                //photoStoragePath = mStorage.child("Photos").child(uriPhoto.getLastPathSegment());
                selectPhotoDone = true;
            }
            else {
                video.setVideoURI(data.getData());
                video.setMediaController(new MediaController(this));
                video.requestFocus();
                video.start();

                System.out.println("ID dossier Video DATA : " + data.getData());

                uriVideo = data.getData();
                //photoStoragePath = mStorage.child("Videos").child(uriVideo.getLastPathSegment());
                selectVideoDone = true;
                Log.d("+1+ Video : ", uriVideo.toString());
                Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show();

                Log.d("+2+ Video : ", uriVideo.toString());
                MyIntentService.uriFile=uriVideo;

                file = uriVideo;
                try {
                    MyIntentService.strm = getContentResolver().openInputStream(uriVideo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Intent intent2 = new Intent(InfoActivity.this, MyIntentService.class);
                intent2.putExtra("uri", uriVideo);
                startService(intent2);


            }

        }
    }

/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (capture) {
            Date d = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
            String s = format.format(d);

            if (resultCode == InfoActivity.RESULT_OK) {
                if (requestCode == 1) {
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        Bitmap bitmap;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                        photo.setImageBitmap(bitmap);
                        photo_bitmap = bitmap;
                        String path = Environment.getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();
                        OutputStream outFile = null;
                        File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        try {
                            outFile = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                            outFile.flush();
                            outFile.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 2) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("image path", picturePath + "");
                    photo.setImageBitmap(thumbnail);
                    photo_bitmap = thumbnail;

                }
                photoPath = "ACC_" + s;
                createDirectoryAndSaveFile(photo_bitmap, photoPath);
            }

            photoStoragePath = data.getData();
        }
        else {
            video.setVideoURI(data.getData());
            video.setMediaController(new MediaController(this));
            video.requestFocus();
            video.start();
            videoStoragePath = data.getData();
        }
    }*/

    ////////////// ajoutée ___________________________
    /// _______________________________

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/.AssureMe");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/.AssureMe/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/.AssureMe/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("msg", "g accédé à la fonction de save");
    }
}
