package com.gii.insreport;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Gallery extends AppCompatActivity {
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private long[] arrDate;
    private ImageAdapter imageAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        InsReport.multipleImages.clear();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[] {
                "Camera"
        };

        Cursor imagecursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection,
                selectionArgs, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);

        int numOfForms = Integer.parseInt(InsReport.sharedPref.getString("number_of_photos_to_load","50"));
        this.count = Math.min(imagecursor.getCount(),numOfForms); //to make it faster, we allow only last n photos
        int lastDate = -2;
        int days = 0;
        /*
        for (int i = 0; i < this.count; i++ ) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            Log.e("Gallery", "onCreate: " + id);
            int theDate = imagecursor.getInt(imagecursor.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN));
            if (theDate != lastDate) {
                lastDate = theDate;
                days++;
            }
            if (days == 3) {
                this.count = i; //and we still limit it to two days (today and yesterday)
                break;
            }
        }

*/
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        this.arrDate = new long[this.count];
        this.thumbnailsselection = new boolean[this.count];
        //InsReport.multipleImages.clear();
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int dateColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                        getApplicationContext().getContentResolver(), id,
                        MediaStore.Images.Thumbnails.MINI_KIND, null);
                arrPath[i] = imagecursor.getString(dataColumnIndex);
                arrDate[i] = imagecursor.getLong(dateColumnIndex);
                //InsReport.multipleImages.add(arrPath[i]);
            }

        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        imagecursor.close();

        final Button selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int len = thumbnailsselection.length;
                int cnt = 0;
                String selectImages = "";
                InsReport.multipleImages.clear();
                InsReport.multipleImagesDate.clear();
                for (int i =0; i<len; i++)
                {
                    if (thumbnailsselection[i]){
                        cnt++;
                        selectImages = selectImages + arrPath[i] + "|";
                        InsReport.multipleImages.add(arrPath[i]);
                        InsReport.multipleImagesDate.add(arrDate[i]);
                    }
                }
                if (cnt == 0){
                    Toast.makeText(getApplicationContext(),
                            "Пожалуйста, выберите хотя бы одну фотографию",
                            Toast.LENGTH_LONG).show();
                } else {
                    finish();
                    Log.e("SelectedImages", selectImages);
                }
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]){
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            holder.imageview.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    /*
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
                    startActivity(intent);
                    */
                    thumbnailsselection[id] = !thumbnailsselection[id];
                    holder.checkbox.setChecked(thumbnailsselection[id]);
                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            holder.checkbox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }}
