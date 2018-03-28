package com.mobilecoderz.smartmeeting.utils;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtils {

    public static String Tag;
    public final static String SERVICE_DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static boolean accept;
    public static String imageNameLocal;
    public final static String PICK_DATE_FORMAT = "dd/MM/yyyy, hh:mm a";
    public final static String PICK_DATE_MONTH = "dd-MMM-yyyy, hh:mm a";
    public final static String PICK_DATE = "dd/MM/yyyy";

    public static String changedFormat(String serverDate, String format) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat(SERVICE_DATE_FORMAT_1, Locale.US);
            originalFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            SimpleDateFormat targetFormat = new SimpleDateFormat(format);
            targetFormat.setTimeZone(TimeZone.getDefault());
            Date date = new Date(originalFormat.parse(serverDate).getTime() - (5 * 60 * 60 * 1000 + 30 * 60 * 1000));
            String formattedDate = targetFormat.format(date);

            return formattedDate;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static int getDeviceWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    public static void showDialogOK(Context context, String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "NIIT");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("NIIT", "failed to create directory");
                return null;
            }
        }

        // Create a media file name


        return mediaStorageDir;
    }

    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static String getDateAndTimeFromTSDate(String timeStamp) {
        try {
            if (timeStamp.length() > 0) {
                return new SimpleDateFormat("dd/MM/yyyy")
                        .format(new Date((Long.valueOf(timeStamp) * 1000)));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDate(String timeStamp) {
        try {
            if (timeStamp.length() > 0) {
                return new SimpleDateFormat("dd-MMM-yyyy")
                        .format(new Date((Long.valueOf(timeStamp))));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Typeface setSemibold(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/raleway.semibold.ttf");
        return TextFont;
    }

    public static Typeface setBold(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/raleway.bold.ttf");
        return TextFont;
    }

    public static Typeface setMedium(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/raleway.medium.ttf");
        return TextFont;
    }

    public static Typeface setRegular(Context context) {
        Typeface TextFont = Typeface.createFromAsset(context.getAssets(), "fonts/raleway.regular.ttf");
        return TextFont;
    }

    public static void setFragmentT(Fragment fragment, boolean removeStack, FragmentActivity activity, int mContainer) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        ftTransaction.replace(mContainer, fragment);
        ftTransaction.addToBackStack(null);
        //ftTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
     /*   if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer, fragment);
        } else {
            ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }*/
        // Log.e("TAG", "Fragment transition is completetd");
        ftTransaction.commit();
    }

    public static void setFragment(Fragment fragment, boolean removeStack, FragmentActivity activity, FrameLayout mContainer, String tag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (tag != null)
                ftTransaction.replace(mContainer.getId(), fragment, tag);
            else
                ftTransaction.replace(mContainer.getId(), fragment);
        } else {
            if (tag != null)
                ftTransaction.replace(mContainer.getId(), fragment, tag);
            else
                ftTransaction.replace(mContainer.getId(), fragment);

            ftTransaction.addToBackStack(null);

        }
        ftTransaction.commit();
    }


    public static void saveStringPreferences(Context context, String key,
                                             String value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused com.blockWorkout.view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no com.blockWorkout.view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void saveIntPreferences(Context context, String key, int value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }


    public static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static int getIntPreferences(Context context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);

    }

    public static void savePreferencesBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getPreferencesBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void showAlertExit(Context context, int titleId, int messageId,
                                     CharSequence positiveButtontxt,
                                     DialogInterface.OnClickListener positiveListener,
                                     CharSequence negativeButtontxt,
                                     DialogInterface.OnClickListener negativeListener) {
        Dialog dlg = new AlertDialog.Builder(context)
                .setTitle(titleId)
                .setPositiveButton(positiveButtontxt, positiveListener)
                .setNegativeButton(negativeButtontxt, negativeListener)
                .setMessage(messageId).setCancelable(false).create();

        dlg.show();

    }


    public static void showAlert(String message, Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showAlert(String message, Activity context, DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false).setPositiveButton("OK",
                okListener);

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isValidUrl(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

   /* public static boolean isValidUrl(String url) {
       // String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        if( android.util.Patterns.WEB_URL.matcher(url).matches())
            return true;
        else
            return false;*/


    /*public static final Pattern WEB_URL = Pattern.compile(
           "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                   + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                   + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                   + "(?:" + DOMAIN_NAME + ")"
                   + "(?:\\:\\d{1,5})?)" // plus option port number
                   + "(\\/(?:(?:[" + GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"  // plus option query params
   ); // and finally, a word boundary or end of
*/
    public static void showAlertNew(String title, String message,
                                    final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setFragment(Fragment fragment, boolean removeStack, FragmentActivity activity, FrameLayout mContainer) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer.getId(), fragment);
        } else {
            ftTransaction.replace(mContainer.getId(), fragment);
            ftTransaction.addToBackStack(null);
        }
        ftTransaction.commit();
    }


    public static void showAlertOk(String message, Context context) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();


                            }
                        });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ProgressDialog(String tittle, String message, Activity context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle(tittle);

        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(false);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        // After some action
        alertDialog.dismiss();
    }

    public final static boolean isValidPhone(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return android.util.Patterns.PHONE.matcher(target)
                    .matches() && (target.length() >= 10 && target.length() <= 20);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void hideKeyPad(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (activity.getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void SendEmail(Activity context, String To) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, To);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getTimeStamp() {

        long timestamp = (System.currentTimeMillis() / 1000L);
        String tsTemp = "" + timestamp;

        return "" + tsTemp;

    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static boolean isValidPassword(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return (target.length() > 4);
        }
    }

    public static boolean isPasswordValidation(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static String savePreferencesString(Context context, String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

        return key;
    }

    public static String getPreferences(Context context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");

    }

    public static void removePreferences(Activity context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.remove(key);

    }

    public static boolean getPreferencesBoolean(Activity context, String key) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);

    }

    public static String getPreferencesString(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    public static String getDate(Context context, String timestamp_in_string) {
        long dv = Long.parseLong(timestamp_in_string) * 1000;// its need to be in milisecond
        Date df = new Date(dv);
        String vv = new SimpleDateFormat("MMM dd/yyyy,hh:mma").format(df);
        return vv;
    }

    public static String getTime(String timestamp_in_string) {
        long dv = Long.parseLong(timestamp_in_string) * 1000;// its need to be in milisecond
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(dv);
        String date = DateFormat.format("hh:mm:ss", cal).toString();
        return date;

    }

    public static boolean isDateToday(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        Date getDate = calendar.getTime();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startDate = calendar.getTime();

        return getDate.compareTo(startDate) > 0;

    }

    public static void showAlertTitle(String title, String message,
                                      final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getHoursFromMillis(long milliseconds) {
        return "" + (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    }

    public static Bitmap getBitMapFromImageURl(String imagepath, Activity activity) {

        Bitmap bitmapFromMapActivity = null;
        Bitmap bitmapImage = null;
        try {

            File file = new File(imagepath);
            // We need to recyler unused bitmaps
            bitmapImage = reduceImageSize(file, activity);
            int exifOrientation = 0;
            try {
                ExifInterface exif = new ExifInterface(imagepath);
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                int w = bitmapImage.getWidth();
                int h = bitmapImage.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by
                // tess

                Bitmap myBitmap = Bitmap.createBitmap(bitmapImage, 0, 0, w, h,
                        mtx, false);
                bitmapFromMapActivity = myBitmap;
            } else {
                int SCALED_PHOTO_WIDTH = 150;
                int SCALED_PHOTO_HIGHT = 200;
                Bitmap myBitmap = Bitmap.createScaledBitmap(bitmapImage,
                        SCALED_PHOTO_WIDTH, SCALED_PHOTO_HIGHT, true);
                bitmapFromMapActivity = myBitmap;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmapFromMapActivity;

    }

    public static Bitmap reduceImageSize(File f, Context context) {

        Bitmap m = null;
        try {

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_SIZE = 150;

            int width_tmp = o.outWidth, height_tmp = o.outHeight;

            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
            m = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            // Toast.makeText(context,
            // "Image File not found in your phone. Please select another image.",
            // Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {

        }
        return m;
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retreiving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


  /*  public static void dialogSelectDate(final Context context, final TextView textView) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.add(Calendar.YEAR, 0);
                        if (minAdultAge.after(userAge)) {
                            textView.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                        }else {
                            showAlertOk("Please select a valid date of birth.",context);
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
        dpd.setCancelable(false);
    }*/


    public static void Toast(Context context, String string) {

        Toast.makeText(context, "Work in progress", Toast.LENGTH_SHORT).show();

    }


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    /*It is used for dynamic progress bar*/
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    public static int getScreenWidthResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return width;
    }

    public static String getScreenHeightResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return height + "";
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }


    public static int getMonth(long timeStamp) {

        java.text.DateFormat formatter1 = new SimpleDateFormat("MM", Locale.ENGLISH);
        Log.e("##@@Month", formatter1.format(new java.sql.Date((Long.valueOf(timeStamp) * 1000))));

        return Integer.parseInt(formatter1.format(new java.sql.Date((Long.valueOf(timeStamp) * 1000))));

    }

    public static int getYear(long timeStamp) {

        java.text.DateFormat formatter1 = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        Log.e("##@@", formatter1.format(new java.sql.Date((Long.valueOf(timeStamp) * 1000))));

        return Integer.parseInt(formatter1.format(new java.sql.Date((Long.valueOf(timeStamp) * 1000))));

    }

    public static int getDay(long timeStamp) {


        java.text.DateFormat formatter1 = new SimpleDateFormat("dd", Locale.ENGLISH);
        Log.e("##@@Day", formatter1.format(new java.sql.Date((Long.valueOf(timeStamp) * 1000))));

        return Integer.parseInt(formatter1.format(new java.sql.Date((Long.valueOf(timeStamp) * 1000))));

    }

    public static boolean checkPermissionStorage(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            if (result1 == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;

        }
    }

   /* public static  void requestPermissionCamera(Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},AppConstants.PERMISSION_REQUEST_CODE);
        }
    }*/


    public static boolean checkPermissionCamera(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            return false;

        }
    }
  /*  public static   void requestPermissionStorage( Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)||ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},AppConstants.PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},AppConstants.PERMISSION_REQUEST_CODE);
        }
    }*/


    public static boolean checkPermissionUserContact(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            return false;

        }
    }

    public static boolean checkPermissionSendSms(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            return false;

        }
    }

    public static boolean checkPermissionCallPhone(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            return false;

        }
    }

    public static boolean checkPassWordAndConfirmPassword(String password, String confirmPassword) {
        boolean pstatus = false;
        if (confirmPassword != null && password != null) {
            if (password.equals(confirmPassword)) {
                pstatus = true;
            }
        }
        return pstatus;
    }

    /*  public static void deleteDialog(final Activity context) {
          TextView tvYes,tvNo,tvMsg;
          LayoutInflater inflater = LayoutInflater.from(context);
          final Dialog mDialog = new Dialog(context,
                  android.R.style.Theme_Translucent_NoTitleBar);
          mDialog.setCanceledOnTouchOutside(true);
          mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.MATCH_PARENT);
          mDialog.getWindow().setGravity(Gravity.CENTER);
  //        mDialog.getWindow().getAttributes().windowAnimations = R.anim;
          WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
          lp.dimAmount = 0.75f;
          mDialog.getWindow()
                  .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
          mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          mDialog.getWindow();

          View dialoglayout = inflater.inflate(R.layout.logout_dialog, null);
          mDialog.setContentView(dialoglayout);


          tvMsg = (TextView) mDialog.findViewById(R.id.tv_logout);
          tvYes = (TextView) mDialog.findViewById(R.id.tvYes);
          tvNo = (TextView) mDialog.findViewById(R.id.tvNo);
          tvMsg.setText(R.string.are_you_sure_you_want_to_exit_from_the_app);

          tvYes.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  mDialog.dismiss();
                  context.moveTaskToBack(true);

              }
          });
          mDialog.show();
          tvNo.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  mDialog.dismiss();
              }
          });

          mDialog.show();
      }*/
    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isAllZeroNoMobile(String phone) {
        char[] numArray = phone.toCharArray();
        int counter = 0;
        for (int i = 0; i < numArray.length; i++) {
            if (numArray[i] == '0') {
                counter++;
            }
            if ((i == numArray.length - 1 && counter > 0) || (counter > 0 && numArray[i] != '0')) {
                System.out.println("Number of Zeroes: " + counter);

            }
        }
        if (counter == numArray.length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context _context) {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static String printKeyHash(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static String getFileName(String urlSubString) {
       String filePath;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root +  "/iDressApp");
        myDir.mkdirs();
        File file = new File(myDir, "SnagSquare" + urlSubString);
        filePath = String.valueOf(file);
        // File file = new File(myDir, "iDressApp" + imageName.replace(".jpg","") + ".jpg");
        return filePath;
    }

    public static String getImageName(String imagePath) {
        String[] base = imagePath.split("/");
        return  base[base.length - 1];
    }
   /* public static void showDialog(final Context context, final String title, final DialogCallback callback) {
        final TextView tvNo,tvYes,tv_title;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog mDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        mDialog.getWindow().setAttributes(lp);
        final View dialoglayout = inflater.inflate(R.layout.alert_dialog, null);
        mDialog.setContentView(dialoglayout);
        mDialog.setCanceledOnTouchOutside(false);
        tv_title= (TextView) dialoglayout.findViewById(R.id.dialog_title);
        tvNo = (TextView) dialoglayout.findViewById(R.id.dialog_no);
        tvYes = (TextView) dialoglayout.findViewById(R.id.dialog_yes);
        tv_title.setText(title);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (context instanceof HomeActivity && title.startsWith("We need")){
                    ((HomeActivity) context).finish();
                }
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                callback.onYes();
            }
        });
        mDialog.show();
    }*/
}

