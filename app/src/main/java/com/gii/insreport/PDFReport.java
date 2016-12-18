package com.gii.insreport;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Timur_hnimdvi on 18-Dec-16.
 */
public class PDFReport {
    PdfDocument.Page page;
    PdfDocument.PageInfo pageInfo;
    PdfDocument document;
    Context context;
    Form form;
    int y = 0;
    public PDFReport(Context context, Form form) {
        this.context = context;
        this.form = form;
        document = new PdfDocument();
        page = null;
    }
    public void newPage() {
        if (page != null)
            document.finishPage(page);
        pageInfo = new PdfDocument.PageInfo.Builder
                (595, 842, 1).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        y = 40;
    }
    public void generatePDF() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Toast.makeText(context, "Android version not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        newPage();
        draw();
        document.finishPage(page);

        saveToFile();
    }

    public void saveToFile() {
        String filename = Environment.getExternalStorageDirectory().toString() + "/PDF/report.pdf";
        try {
            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/PDF");
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            else {
                file.delete();
                file.createNewFile();
            }

            FileOutputStream fileos = new FileOutputStream (file);
            document.writeTo(fileos);
            document.close();
            fileos.close();

            if(file.exists()) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filename));
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Report: ");
                    //intentShareFile.putExtra(Intent.EXTRA_TEXT, gii.getContext().getString(R.string.emailBodyXML));
                    context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
            }


        }
        catch (Exception e) {        }
    }
    int leftBound = 30;
    private void draw() {
        Drawable logoIcon = ContextCompat.getDrawable(context, R.drawable.centras_logo_new);
        for (FirebaseUserEmail firebaseUserEmail : InsReport.firebaseUserEmails) {
            if (firebaseUserEmail.id.equals(InsReport.user.getUid()))
                InsReport.savePref("username",firebaseUserEmail.name);
        }


        Paint black = new Paint();
        int defTextSize = 12;

        black.setTextSize(defTextSize);
        black.setStrokeWidth(0.3f);
        black.setStyle(Paint.Style.FILL_AND_STROKE);
        black.setColor(Color.BLACK);
        print("", true, black);
        logoIcon.setBounds(leftBound,y,leftBound+70,(int)(y + 70 * (392f/306f))); logoIcon.draw(canvas);
        print("Акт осмотра места происшествия", true, black);
        print(form.description, true, black);
        print("Дело номер " + form.element("CLAIM_REGID").toString(), true, black);
        print("", false, black);
        print("", false, black);
        print("", false, black);
        print("", false, black);

        Map<String, String> cat = new HashMap<>();

        for (Element element : form.elements) {
            if (cat.get(element.category) == null) {
                cat.put(element.category, description(element.category));
            }
        }


        print(new String[]{"Дата Время ДТП","Время вызова",form.element("INSR_TYPE").description,
                "ФИО Сотрудника"},black);
        print("",false,black);
        print(new String[]{
                form.element("EVENT_DATE").toString(),
                IncidentFormActivity.dateTimeText(form.dateCreated),
                form.element("INSR_TYPE").toStringDirectory(),
                InsReport.sharedPref.getString("username","Имя пользователя не выбрано")
        },black);

        print("",false,black);

        print("", true, black);
        print("Основная информация", true, black);
        print("", true, black);

        outputCategory(canvas,form.elements,"general",black);

        outputObjects(black);
        outputParticipants(black);
        outputOtherPhotos(black);
    }

    private void outputObjects(Paint black) {
        for (Element element : form.objects.elements) {
            newPage();
            print("Информация по объекту " + element.description,true,black);
            print("",true,black);
            print("",true,black);
            outputCategory(canvas,element.elements,"object",black);
            for (Element element1 : element.elements) {
                if (element1.type == Element.ElementType.ePlan) {
                    if (!element1.vText.equals("")) {
                        Element photoElement = new Element();
                        photoElement.description = "Повреждения";
                        photoElement.vText = element1.vText;
                        boolean s = drawPicture(photoElement, 2, 0, black);
                        Log.w(TAG, "outputObjects: returned from drawPicture, fuf");
                    }
                }
            }
            Log.w(TAG, "outputObjects: 111011");
            if (y > canvas.getWidth()/2) {
                Log.w(TAG, "outputObjects: 111011-1");
                newPage();
                Log.w(TAG, "outputObjects: 111011-2");
            }
            print("Фотографии объекта " + element.description,true,black);
            print("",true,black);
            boolean even = false;
            for (Element element1 : element.elements) {
                if (element1.category.equals("photo")) {
                    Log.w(TAG, "outputObjects: a picture found");
                    for (Element photoElement : element1.elements) {
                        if (drawPicture(photoElement,2,(even?0:1),black))
                            even = !even;
                        Log.w(TAG, "outputObjects: picture drawn successfully");
                    }
                }
            }
        }
        Log.w(TAG, "outputObjects: exiting...");
    }

    Canvas canvas;
    private void outputParticipants(Paint black) {
        for (Element element : form.participants.elements) {
            if (y > canvas.getWidth()/2) {
                newPage();
            }
            print("",true,black);
            print("",true,black);
            print("Информация по участнику " + element.description,true,black);
            print("",true,black);
            print("",true,black);
            outputCategory(canvas,element.elements,"participant",black);
        }
    }


    private void outputOtherPhotos(Paint black) {
        for (Element element : form.elements) {
            if (element.category.equals("photo") && element.elements.size() > 0) {
                newPage();
                print(element.description, true, black);
                print("", true, black);
                print("", true, black);
                boolean even = false;
                for (Element photoElement : element.elements) {
                    if (drawPicture(photoElement, 2, (even ? 0 : 1), black))
                        even = !even;
                }
            }
        }
    }


    private void outputCategory(Canvas canvas, ArrayList<Element> elements, String category, Paint paint) {
        for (Element element : elements) {
            if (element.category.equals(category)) {
                if (!element.toStringDirectory().equals("")) {
                    print(element, paint);
                }
            }
        }
    }

    private String description(String category) {
        Map<String,String> desc = new HashMap<>();
        desc.put("general","Основная информация");
        desc.put("description","Описание");
        desc.put("additionalInfo","Дополнительная информация");
        if (desc.get(category) != null)
            return desc.get(category);
        return category;
    }

    private void print(String text, boolean centered, Paint paint) {
        Log.w(TAG, "print: " + text);
        int x = leftBound;

        if (y > canvas.getHeight() * 0.9) {
            y = 20;
            newPage();
        }

        if (centered)
            x = canvas.getWidth()/2 - (int)paint.measureText(text)/2;
        canvas.drawText(text, x, y + paint.getTextSize()/2, paint);
        y += paint.getTextSize() * 1.5;
    }


    private void print(Element element, Paint paint) {
        Log.w(TAG, "print: " + element.description + " : " + element.toStringDirectory());
        if (y > canvas.getHeight() * 0.9) {
            y = 20;
            newPage();
        }

        String text = element.description;
        String text2 = element.toStringDirectory();

        int x = leftBound;
        int y1 = y;
        int x1 = x;
        String[] t = text.split(" ");
        for (String s : t) {
            int k = (int)(paint.measureText(s + " "));
            if (x1 + k > canvas.getWidth()/2.3 + x) {
                x1 = x;
                y1 += paint.getTextSize() * 1.2;
            }
            canvas.drawText(s + " ", x1, y1 + paint.getTextSize()/2, paint);
            x1 += k;
        }

        x = canvas.getWidth()/2;
        int y2 = y;
        x1 = x;
        t = text2.split(" ");
        for (String s : t) {
            int k = (int)(paint.measureText(s + " "));
            if (x1 + k > canvas.getWidth()/2.3 + x) {
                x1 = x;
                y2 += paint.getTextSize() * 1.2;
            }
            canvas.drawText(s + " ", x1, y2 + paint.getTextSize()/2, paint);
            x1 += k;
        }

        y = (int)(Math.max(y1,y2) + paint.getTextSize() * 1.5);
    }
    private String TAG = "PDFReport.java";
    private boolean drawPicture(final Element element, final int columns, final int column, final Paint paint) {
        Log.w(TAG, "drawPicture: " + element.description);
        final AtomicBoolean done = new AtomicBoolean(false);
        final int x = (int)((canvas.getWidth()*0.8) / columns * column) + leftBound;
        final int x1 = (int)((canvas.getWidth()*0.8) / columns * (column+1)) + leftBound;
        final AtomicBoolean successful = new AtomicBoolean(false);
        InsReport.ref.child("images/" + element.vText).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String gotData = snapshot.getValue(String.class);
                        if (gotData != null) {
                            Log.w(TAG, "onDataChange: got image");
                            try {
                                Log.w(TAG, "onDataChange: decoding...");
                                final Bitmap thePicture = CameraAndPictures.decodeBase64(gotData);
                                //InsReport.bitmapsNeedToBeRecycled.add(thePicture);
                                Log.w(TAG, "onDataChange: calculating...");
                                int h = (int)(thePicture.getHeight()/(float)thePicture.getWidth() * (x1 - x - 10));
                                Log.w(TAG, "onDataChange: drawing...");
                                canvas.drawBitmap(thePicture,null,new Rect(x,y,x1-10,y+h),null);
                                Log.w(TAG, "onDataChange: writing...");
                                int xk = x;
                                int y1 = (int)(y + h + paint.getTextSize());
                                String[] t = element.description.split(" ");
                                for (String s : t) {
                                    int k = (int)(paint.measureText(s + " "));
                                    if (xk + k > x1) {
                                        xk = x;
                                        y1 += paint.getTextSize() * 1.2;
                                    }
                                    canvas.drawText(s + " ", xk, y1 + paint.getTextSize()/2, paint);
                                    xk += k;
                                }
                                Log.w(TAG, "onDataChange: setting successful!");
                                successful.set(true);
                                if (column < columns - 1)
                                    y = y1 + (int)(paint.getTextSize())+ 20;
                            } catch (Exception e) {
                                Log.e(TAG, "onDataChange: " + e.getMessage());
                                e.printStackTrace();
                            } finally {
                                Log.w(TAG, "onDataChange: finally countdown");
                                done.set(true);
                            }
                        } else {
                            //no data = no problem
                            Log.w(TAG, "onDataChange: no data ");
                            done.set(true);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e("Firebase", "The read failed 4 - PDFReport: " + firebaseError.getMessage());
                        done.set(true);
                    }
                });
        Log.w(TAG, "drawPicture: listener set.");
        while (!done.get()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
        Log.w(TAG, "drawPicture: listener worked out!");
        return (successful.get());
    }

    private void print(String[] texts, Paint paint) {
        int i=0;
        int max = 0;

        for (String text : texts) {
            int leftBound = (int)(this.leftBound + 10 + i*canvas.getWidth()*0.9f/texts.length);
            int rightBound = (int)(this.leftBound + 10 + (i + 1) * canvas.getWidth()*0.9f/texts.length);
            int y1 = y;
            int x1 = leftBound;
            String[] t = text.split(" ");
            for (String s : t) {
                int k = (int)(paint.measureText(s + " "));
                if (x1 + k > rightBound) {
                    x1 = leftBound;
                    y1 += paint.getTextSize() * 1.2;
                }
                canvas.drawText(s + " ", x1, y1 + paint.getTextSize()/2, paint);
                x1 += k;
            }
            i++;
            if (y1 > max)
                max = y1;
        }

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(0.5f);
        linePaint.setColor(Color.GRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        for (int j = 0; j < texts.length; j++) {
            int leftBound = (int) (this.leftBound + 10 + j * canvas.getWidth() * 0.9f / texts.length);
            int rightBound = (int) (this.leftBound + 10 + (j + 1) * canvas.getWidth() * 0.9f / texts.length);
            canvas.drawRect(leftBound-10, y-10, rightBound-10, (int) (max + paint.getTextSize() * 1.5), linePaint);
        }

        y = (int)(max + paint.getTextSize());
    }

}
