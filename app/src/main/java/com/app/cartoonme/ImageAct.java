package com.app.cartoonme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;



public class ImageAct extends Activity {
    Bitmap bitmap;
    Uri uri;
    TextView editText1;
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    RecyclerView SpeechRecycler;
    ArrayList<View> viewArrayList;
    EditText editText;
    private int xDelta;
    private int yDelta;
    Button Savebutton;
    Button bubbleBtn;
    Button effectBtn;
    SeekBar SizeBar;
    Button Rotatebutton;
    int BubbleorEffect;
    RelativeLayout relativeLayout;
    private ViewGroup mainLayout;
    ArrayList<MainModel> BubbleModels;
    ArrayList<MainModel> EffectModels;
    MainAdapter mainAdapter;
    Button Deletebutton;
    Button mRetreive_images;
    public static Context context;
    EditText storyText;
    private MainAdapter.RecyclerViewClickListener listener;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    Button ShareButton;
    TextToSpeech textToSpeech;
    String Word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


editText1=findViewById(R.id.openSignUpIntent);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        viewArrayList = new ArrayList<>();
        effectBtn = findViewById(R.id.EffectsButton);
        bubbleBtn = findViewById(R.id.bubblesButton);

        SpeechRecycler = findViewById(R.id.SpeechRec);

        mRetreive_images = findViewById(R.id.Retrieve_images);

        Integer[] bubbles = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic6,
                R.drawable.pic7};

        Integer[] effects = {R.drawable.effectbahhah, R.drawable.effectboom, R.drawable.effectgameover, R.drawable.effectkapow,
                R.drawable.effectoh, R.drawable.effectsmile, R.drawable.effectwow};

        BubbleModels = new ArrayList<>();
        for (int i = 0; i < bubbles.length; i++) {
            MainModel model = new MainModel(bubbles[i]);
            BubbleModels.add(model);
        }


        EffectModels = new ArrayList<>();
        for (int i = 0; i < effects.length; i++) {
            MainModel model = new MainModel(effects[i]);
            EffectModels.add(model);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(
                ImageAct.this, LinearLayoutManager.HORIZONTAL, false);

        SpeechRecycler.setLayoutManager(layoutManager);
        SpeechRecycler.setItemAnimator(new DefaultItemAnimator());

        ////////////////////////////////////////////////////////
        setOnClickLis();


        effectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLis();
                BubbleorEffect = 1;
                mainAdapter = new MainAdapter(ImageAct.this, EffectModels, mainLayout, listener);
                SpeechRecycler.setAdapter(mainAdapter);
            }
        });
        bubbleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BubbleorEffect = 0;
                mainAdapter = new MainAdapter(ImageAct.this, BubbleModels, mainLayout, listener);
                SpeechRecycler.setAdapter(mainAdapter);

            }
        });




        editText =findViewById(R.id.editTextTextPersonName4);
        Savebutton = findViewById(R.id.button7);
        relativeLayout = findViewById(R.id.main);
        imageView3 = findViewById(R.id.imageView3);
        imageView2 = findViewById(R.id.imageView2);
        Savebutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                for (int i = 0; i < viewArrayList.size(); i++) {

                    viewArrayList.get(i).findViewById(R.id.button8).setVisibility(View.INVISIBLE);
                    viewArrayList.get(i).findViewById(R.id.seekBar).setVisibility(View.INVISIBLE);
                    viewArrayList.get(i).findViewById(R.id.button9).setVisibility(View.INVISIBLE);

                   bitmap= Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
                   Canvas canvas = new Canvas(bitmap);
                   relativeLayout.draw(canvas);
                   imageView3.setImageBitmap(bitmap);


                    storyText = viewArrayList.get(i).findViewById(R.id.editTextTextPersonName4);
                    getTextFromImage(viewArrayList.get(i));
                  ///  String text = editText1.getText().toString();
                    imageView3.setVisibility(View.INVISIBLE);

                    textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                storyText.setEnabled(status == TextToSpeech.SUCCESS);
                                textToSpeech.speak(Word, TextToSpeech.QUEUE_FLUSH, null);
                                textToSpeech.setLanguage(Locale.ENGLISH);

                            } else {
                                Log.e("TTS", "Initialisation Failed!");
                            }
                        }

                    });


                }
                if (getImageUri(ImageAct.this, bitmap) != null) {

                    StorageReference fileReference=mStorageRef.child(UserNameSave.UserName+"Uploads").child(System.currentTimeMillis()+"."+getFileExtension(getImageUri(ImageAct.this, bitmap)));
                    fileReference.putFile(getImageUri(ImageAct.this, bitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);
                            Toast.makeText(ImageAct.this, "upload successful", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadurl = uri;
                                    Upload upload = new Upload(downloadurl.toString());
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(UserNameSave.UserName).child("Uploads").child(uploadId).setValue(upload);

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImageAct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                } else {
                    Toast.makeText(ImageAct.this, "no file selected", Toast.LENGTH_SHORT).show();
                }
                viewArrayList=new ArrayList<>();
            }

        });


        ActivityCompat.requestPermissions(ImageAct.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(ImageAct.this);
            }
        });

        mainLayout = (RelativeLayout) findViewById(R.id.main);


    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    //////////////////////////////////////////////////////////////
    private void setOnClickLis() {
        listener = new MainAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {
                if (BubbleorEffect == 0) {
                    if (pos == 0) {
                        View bub = getLayoutInflater().inflate(R.layout.bubblespeech, null, false);
                        ImageView view = (ImageView) bub.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) bub.findViewById(R.id.button8);
                        Deletebutton = (Button) bub.findViewById(R.id.button9);

                        final int[] rangle = {0};
                        SizeBar = (SeekBar) bub.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                bub.setScaleX(x);
                                bub.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                bub.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) bub.getParent()).removeView(bub);
                            }
                        });
                        viewArrayList.add(bub);
                        mainLayout.addView(bub);



                    } else if (pos == 1) {
                        View kaza = getLayoutInflater().inflate(R.layout.bubblespeech2, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 2) {
                        View kaza = getLayoutInflater().inflate(R.layout.bubblespeech3, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 3) {
                        View kaza = getLayoutInflater().inflate(R.layout.bubblespeech4, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 4) {
                        View kaza = getLayoutInflater().inflate(R.layout.bubblespeech5, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 5) {
                        View kaza = getLayoutInflater().inflate(R.layout.bubblespeech6, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    }

                }

                else if (BubbleorEffect == 1) {
                    if (pos == 0) {
                        View bub = getLayoutInflater().inflate(R.layout.effectlayout, null, false);
                        ImageView view = (ImageView) bub.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) bub.findViewById(R.id.button8);
                        Deletebutton = (Button) bub.findViewById(R.id.button9);
                        bub.setScaleX(0.45f);
                        bub.setScaleY(0.45f);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) bub.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                bub.setScaleX(x);
                                bub.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                bub.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) bub.getParent()).removeView(bub);
                            }
                        });
                        viewArrayList.add(bub);
                        mainLayout.addView(bub);
                    }
                    else if (pos == 1) {
                        View kaza = getLayoutInflater().inflate(R.layout.effectlayout2, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        kaza.setScaleX(0.45f);
                        kaza.setScaleY(0.45f);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 2) {
                        View kaza = getLayoutInflater().inflate(R.layout.effectlayout3, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        kaza.setScaleX(0.45f);
                        kaza.setScaleY(0.45f);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 3) {
                        View kaza = getLayoutInflater().inflate(R.layout.effectlayout4, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        kaza.setScaleX(0.45f);
                        kaza.setScaleY(0.45f);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 4) {
                        View kaza = getLayoutInflater().inflate(R.layout.effectlayout5, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        kaza.setScaleX(0.45f);
                        kaza.setScaleY(0.45f);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 5) {
                        View kaza = getLayoutInflater().inflate(R.layout.effectlayout6, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        kaza.setScaleX(0.45f);
                        kaza.setScaleY(0.45f);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    } else if (pos == 6) {
                        View kaza = getLayoutInflater().inflate(R.layout.effectlayout7, null, false);
                        ImageView view = (ImageView) kaza.findViewById(R.id.imageView2);
                        Rotatebutton = (Button) kaza.findViewById(R.id.button8);
                        Deletebutton = (Button) kaza.findViewById(R.id.button9);
                        kaza.setScaleX(0.45f);
                        kaza.setScaleY(0.45f);
                        final int[] rangle = {0};
                        SizeBar = (SeekBar) kaza.findViewById(R.id.seekBar);
                        view.setOnTouchListener(onTouchListener(Rotatebutton, SizeBar, Deletebutton));
                        SizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float x = progress / 150f;
                                kaza.setScaleX(x);
                                kaza.setScaleY(x);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Rotatebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                kaza.setRotation(20 + rangle[0]);
                                rangle[0] = rangle[0] + 20;

                            }
                        });
                        Deletebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((ViewGroup) kaza.getParent()).removeView(kaza);
                            }
                        });
                        viewArrayList.add(kaza);
                        mainLayout.addView(kaza);
                    }


                }
            }

        };
    }


    private OnTouchListener onTouchListener(Button btn, SeekBar seekBar, Button delbtn) {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                btn.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                delbtn.setVisibility(View.VISIBLE);

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:

                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;

                }
                mainLayout.invalidate();
                return true;
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode==Activity.RESULT_OK) {
            Uri imageuri=CropImage.getPickImageResultUri(this,data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri))
            {
                uri=imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
            else
            {
                startCrop(imageuri);
            }

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                imageView.setImageURI(result.getUri());
            }
        }


    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
    }

    public void getTextFromImage(View v){
        BitmapDrawable drawable = (BitmapDrawable) imageView3.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational()){
        }else
        {
            Frame frame =new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder=new StringBuilder();

            for(int i = 0;i<items.size();i++){
                TextBlock myItem = items.valueAt(i);
                stringBuilder.append(myItem.getValue());
                stringBuilder.append("\n");
            }
            Word=stringBuilder.toString();
        }


    }
}