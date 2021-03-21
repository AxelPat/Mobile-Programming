package id.umn.ac.uts_32597;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {


    ListView ListMusic; //
    ArrayAdapter<String> musicAdapter;
    String Lagu[]; // to storage song names;
    ArrayList<File> music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Memanggil ListView
        ListMusic = findViewById(R.id.listView);

        //Alert Dialog pada tampilan awal list
        AlertDialog alertBuilder = new AlertDialog.Builder(ListActivity.this)
                .setTitle("Welcome")
                .setMessage("AxelPatria\n00000032597")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ListActivity.this,"Enjoy", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        //Permission menggunakan Dexter
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //Membaca lagu storage

                music = findMusicFiles(Environment.getExternalStorageDirectory());
                Lagu = new String[music.size()];
                for (int i = 0; i <music.size(); i++) {
                    Lagu[i] = music.get(i).getName();
                }

                //Pass adapter array
                musicAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.warnalv, Lagu);
                //Set adapter listview

                ListMusic.setAdapter(musicAdapter);

                ListMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Intent menuju mediaplayer

                        startActivity(new Intent(ListActivity.this, Player.class)
                                .putExtra("songsList", music)
                                .putExtra("position", position));
                    }
                });

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                //Permission
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    //Membuat ArrayList untuk lagu
    private ArrayList<File> findMusicFiles (File file) {
        ArrayList<File> musicfileobject = new ArrayList<>();
        File [] files = file.listFiles();

        for (File currentFiles: files) {

            if (currentFiles.isDirectory() && !currentFiles.isHidden()) {
                musicfileobject.addAll(findMusicFiles(currentFiles));
            } else {
                if (currentFiles.getName().endsWith(".mp3") || currentFiles.getName().endsWith(".mp4a") || currentFiles.getName().endsWith(".wav")) {
                    musicfileobject.add(currentFiles);
                }
            }
        }

        return musicfileobject;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuProfile) {
            Intent profile = new Intent(ListActivity.this, Profile.class);
            startActivity(profile);
            setResult(RESULT_OK, null);
        }else if(item.getItemId() == R.id.menuLogout){
                Intent home = new Intent(ListActivity.this, MainActivity.class);
                startActivity(home);
                setResult(RESULT_OK, null);
                finish();
        }
        return true;
    }
}