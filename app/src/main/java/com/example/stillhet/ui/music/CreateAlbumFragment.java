 package com.example.stillhet.ui.music;

 import static android.app.Activity.RESULT_OK;

 import android.annotation.SuppressLint;
 import android.app.ProgressDialog;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.net.Uri;
 import android.os.Bundle;
 import android.provider.MediaStore;
 import android.text.TextUtils;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.CheckBox;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.fragment.app.Fragment;
 import androidx.recyclerview.widget.RecyclerView;

 import com.example.stillhet.Adapter.CreateAlbumAdapter;
 import com.example.stillhet.R;
 import com.example.stillhet.StatesForAdapter.MusicState;
 import com.example.stillhet.databinding.FragmentCreateAlbumBinding;
 import com.example.stillhet.Сlasses.Album;
 import com.example.stillhet.Сlasses.Music;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.google.firebase.storage.FirebaseStorage;
 import com.google.firebase.storage.StorageReference;

 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Objects;

 public class CreateAlbumFragment extends Fragment {

    FragmentCreateAlbumBinding binding;

    Button buttonCreate, buttonPhoto;
    ImageView AlbumPhoto;
     private Uri filePath;
     private final int PICK_IMAGE_REQUEST = 22;

    RecyclerView recyclerView;
    ArrayList<MusicState> states;
    ArrayList<MusicState> listForSave;
    CreateAlbumAdapter musicAdapter;
    CheckBox checkBox;

     private ArrayList<String> SongName, Artist, Time, Link;

     EditText albumName, description;

     FirebaseStorage storage;
     StorageReference storageReference;

     FirebaseAuth musicAuth = FirebaseAuth.getInstance();
     FirebaseUser user = musicAuth.getCurrentUser();
     DatabaseReference myMusicDB  = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user.getDisplayName()));
     static int re;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateAlbumBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        buttonCreate = view.findViewById(R.id.createNewAlbumButton);
        buttonPhoto = view.findViewById(R.id.changeAlbumPhotoButton);
        AlbumPhoto = view.findViewById(R.id.changeAlbumPhoto);
        recyclerView = view.findViewById(R.id.newAlbumReView);
        checkBox = view.findViewById(R.id.new_checkBox);

        SongName = new ArrayList<>();
        Artist = new ArrayList<>();
        Time = new ArrayList<>();
        Link = new ArrayList<>();
        states = new ArrayList<>();

        myMusicDB.addValueEventListener(getMusicData);

        buttonPhoto.setOnClickListener(v -> getPhoto());

        buttonCreate.setOnClickListener(v -> {
            albumName = view.findViewById(R.id.NewAlbumName);
            description = view.findViewById(R.id.NewAlbumDescription);

            if(!TextUtils.isEmpty(albumName.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) {
                if (!(musicAdapter.listOfSelectedActivities()).isEmpty()) {
                    if (re == 1) {
                        uploadPhoto();

                        albumName.getText().clear();
                        description.getText().clear();
                        Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getActivity(), "Вы не выбрали изображение для альбома", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getActivity(), "Выберите больше песен", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getActivity(), "Заполните пустые поля", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

     ValueEventListener getMusicData = new ValueEventListener() {
         @SuppressLint("NotifyDataSetChanged")
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (states.size()>0 || SongName.size()>0 || Artist.size()>0 || Time.size()>0 || Link.size()>0)
             {
                 states.clear();
                 SongName.clear();
                 Artist.clear();
                 Time.clear();
                 Link.clear();
             }
             for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                 Music music = dataSnapshot.getValue(Music.class);
                 assert music != null;
                 SongName.add(music.SongName);
                 Artist.add(music.Artist);
                 Time.add(music.Time);
                 Link.add(music.Link);
             }
             for (int i = 0; i < SongName.size(); i ++)
                 states.add(new MusicState(SongName.get(i), Artist.get(i), Time.get(i), Link.get(i)));

             musicAdapter = new CreateAlbumAdapter(CreateAlbumFragment.this.getContext(), states);
             recyclerView.setAdapter(musicAdapter);
         }
         @Override
         public void onCancelled(@NonNull DatabaseError error) {
             Log.w("LogFragment", "loadLog:onCancelled", error.toException());
         }
     };

    private void getPhoto() {
        re = 1;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                 && data != null && data.getData() != null )
         {
             filePath = data.getData();
             try {
                 Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath);
                 AlbumPhoto.setImageBitmap(bitmap);
             }
             catch (IOException e)
             {
                 e.printStackTrace();
             }
         }
     }

     private void uploadPhoto() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Сохранение...");
            progressDialog.show();

            StorageReference ref = storageReference.child("AlbumImages/" + albumName.getText().toString() + description.getText().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Готово", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Ошибка" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Сохранено " + (int) progress + "%");
                    });
            DatabaseReference DB = FirebaseDatabase.getInstance().getReference("Albums");
            String root = description.getText().toString() + albumName.getText().toString() + user.getDisplayName();
            Album album = new Album(albumName.getText().toString(), user.getDisplayName(), description.getText().toString(), albumName.getText().toString() + description.getText().toString());
            DB.child(root).setValue(album);
            DatabaseReference addSongs = FirebaseDatabase.getInstance().getReference("Albums").child(root).child("Songs");
            listForSave = musicAdapter.listOfSelectedActivities();
            addSongs.setValue(listForSave);
        }
    }
}