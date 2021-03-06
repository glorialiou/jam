package hu.ait.jam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.ait.jam.data.Profile;

public class EditProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 101;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etInstrument)
    EditText etInstrument;
    @BindView(R.id.etBio)
    EditText etBio;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.spYears)
    Spinner spYears;
    @BindView(R.id.spGenre)
    Spinner spGenre;
    @BindView(R.id.imgAttach)
    ImageView imgAttach;

    private Profile profile;
    private boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profile = (Profile) getIntent().getSerializableExtra(getString(R.string.profile));
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnAttach)
    public void attachClick() {
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePhoto, REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            imgAttach.setImageBitmap(img);
            imgAttach.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btnSend)
    public void sendClick() {
        if (imgAttach.getVisibility() == View.GONE) {
            updateProfile();
        } else {
            try {
                updateProfileWithImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile(String... imageUrl) {
        ArrayList<String> swipedOnMe = new ArrayList<>();
        HashMap<String, String> matches = new HashMap<>();

        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String instrument = etInstrument.getText().toString();
        String years = spYears.getSelectedItem().toString();
        String genre = spGenre.getSelectedItem().toString();
        String bio = etBio.getText().toString();
        String search = etSearch.getText().toString();

        if ("".equals(name)) { etName.setError(getString(R.string.name_error)); }
        if ("".equals(phone)) { etPhone.setError(getString(R.string.phone_error)); }
        if ("".equals(instrument)) { etInstrument.setError(getString(R.string.instrument_error)); }
        if ("".equals(bio)) { etBio.setError(getString(R.string.bio_error)); }
        if ("".equals(search)) { etSearch.setError(getString(R.string.search_error)); }
        if (!("".equals(instrument)) && !("".equals(bio)) && !("".equals(search))){
            edited = true;

            if (profile != null) {
                swipedOnMe = profile.getSwipedOnMe();
                matches = profile.getMatches();
            }

            Profile newProfile = new Profile( swipedOnMe, matches,
                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    name, phone, instrument, years, genre, bio, search
            );

            if (imageUrl != null && imageUrl.length>0) {
                newProfile.setImageUrl(imageUrl[0]);
            }

            String key = FirebaseDatabase.getInstance().
                    getReference().child("posts").push().getKey();

            FirebaseDatabase.getInstance().getReference().
                    child("posts").child(key).setValue(newProfile);

            finish();
        }
    }

    public void updateProfileWithImage() throws Exception {
        imgAttach.setDrawingCacheEnabled(true);
        imgAttach.buildDrawingCache();
        Bitmap bitmap = imgAttach.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInBytes = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")+".jpg";
        StorageReference newImageRef = storageRef.child(newImage);
        StorageReference newImageImagesRef = storageRef.child("images/"+newImage);
        newImageRef.getName().equals(newImageImagesRef.getName());    // true
        newImageRef.getPath().equals(newImageImagesRef.getPath());    // false

        UploadTask uploadTask = newImageImagesRef.putBytes(imageInBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                updateProfile(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (!edited) {
            String key = FirebaseDatabase.getInstance().
                    getReference().child("posts").push().getKey();

            FirebaseDatabase.getInstance().getReference().
                    child("posts").child(key).setValue(profile);

            finish();
        }

        super.onDestroy();
    }
}