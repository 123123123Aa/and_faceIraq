package net.faceiraq.and_faceiraq.contact.us;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.faceiraq.and_faceiraq.R;
import net.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 20.04.2017.
 */

public class ContactUsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.activity_contact_us_add_photo_iv)
    ImageView mAddPhotoIv;
    @Bind(R.id.activity_contact_us_photo_iv)
    ImageView mPhotoIv;
    @Bind(R.id.activity_contact_us_delete_photo)
    ImageView mDeletePhoto;
    @Bind(R.id.activity_contact_us_photo_title_tv)
    TextView mPhotoTitleTv;
    @Bind(R.id.image_section)
    RelativeLayout mImageSection;
    @Bind(R.id.add_image_section)
    RelativeLayout mAddImageSection;
    @Bind(R.id.activity_contact_us_content_et)
    EditText mContentEt;
    @Bind(R.id.activity_contact_us_email_et)
    EditText mEmailEt;
    @Bind(R.id.activity_contact_us_subject_et)
    EditText mSubjectEt;

    private static final int SELECT_PICTURE = 1;

    private String filemanagerstring;
    private View focusedView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        setupToolbar();
        setOnChangeListeners();
    }

    private void setupToolbar() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int themeColour = typedValue.data;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(R.string.toolbar_title_contact_us);
        mToolbar.setBackgroundColor(themeColour);
    }

    private void setOnChangeListeners() {
        mContentEt.setOnFocusChangeListener(focusListener);
        mEmailEt.setOnFocusChangeListener(focusListener);
        mSubjectEt.setOnFocusChangeListener(focusListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_contact_us, menu);
        MenuItem mItem = (MenuItem) findViewById(R.id.action_send);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_send:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"marcin.zmija@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, mSubjectEt.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, getEmailContent() + mContentEt.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
                try {
                    startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                focusedView = v;
            } else {
                focusedView  = null;
            }
        }
    };

    private String getEmailContent() {
        if (mEmailEt.getText().toString().equals(""))
            return "";
        else
            return "Prefered contact e-mail: " + mEmailEt.getText().toString() + "\n\n" + "Message:" + "\n";
    }

    @OnClick({R.id.add_image_section, R.id.activity_contact_us_delete_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_image_section:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);
                break;
            case R.id.activity_contact_us_delete_photo:
                mAddImageSection.setVisibility(View.VISIBLE);
                mImageSection.setVisibility(View.GONE);
                mPhotoTitleTv.setText("");
                mPhotoIv.setImageDrawable(null);
                break;


        }
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,
//                "Select Picture"), SELECT_PICTURE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
//                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Glide.with(this)
                        .load(selectedImageUri)
                        .centerCrop()
                        .into(mPhotoIv);
//                    mPhotoIv.setImageBitmap(getBitmapFromUri(selectedImageUri));
                filemanagerstring = selectedImageUri.getPath();
//                    selectedImagePath = getPath(selectedImageUri);
                mAddImageSection.setVisibility(View.GONE);
                mImageSection.setVisibility(View.VISIBLE);
                mPhotoTitleTv.setText(filemanagerstring);
            }
        }
    }


//    public String getPath(Uri uri) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if(cursor!=null)
//        {
//            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        }
//        else return null;
//    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}
