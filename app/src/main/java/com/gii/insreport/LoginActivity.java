package com.gii.insreport;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        final Button mResetPasswordButton = (Button) findViewById(R.id.reset_password_button);
        final Button mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mResetPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        final Button signOutButton = (Button) findViewById(R.id.sign_out_button);
        if (InsReport.ref.getAuth() != null) {
            mEmailView.setVisibility(View.GONE);
            mPasswordView.setVisibility(View.GONE);
            mEmailSignInButton.setVisibility(View.GONE);
            mResetPasswordButton.setVisibility(View.GONE);
            mChangePasswordButton.setEnabled(true);
            signOutButton.setEnabled(true);
            signOutButton.setText("Выйти (" + InsReport.ref.getAuth().getProviderData().get("email").toString() + ")");
            signOutButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    InsReport.ref.unauth();
                    mEmailView.setVisibility(View.VISIBLE);
                    mPasswordView.setVisibility(View.VISIBLE);
                    signOutButton.setEnabled(false);
                    mChangePasswordButton.setEnabled(false);
                    mEmailSignInButton.setVisibility(View.VISIBLE);
                    mResetPasswordButton.setVisibility(View.VISIBLE);
                }
            });
            mChangePasswordButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassword();
                }
            });
        }

        loadPassword();
    }

    private void savePassword(String email, String password) {
        SharedPreferences preferences = getSharedPreferences("emailPasswordInsurance", MODE_PRIVATE);
        SharedPreferences.Editor edit= preferences.edit();
        edit.putString("email", email);
        edit.putString("password", password);
        edit.commit();
    }



    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }



    //-----------
    private void changePassword() {
        if (InsReport.ref.getAuth() == null)
            return;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final EditText newPassword1 = new EditText(this);
        final EditText newPassword2 = new EditText(this);
        final LinearLayout layout = new LinearLayout(this);
        newPassword1.setHint("Новый пароль");
        newPassword2.setHint("Повторите новый пароль");
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(newPassword1);
        layout.addView(newPassword2);

        final SharedPreferences preferences = getSharedPreferences("emailPasswordRecircle", MODE_PRIVATE);
        Toast.makeText(getApplicationContext(), preferences.getString("password", ""),
                Toast.LENGTH_LONG).show();

        builder.setTitle("Смена пароля")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (newPassword1.getText().toString().equals(newPassword2.getText().toString()) &&
                                isPasswordValid(newPassword1.getText().toString())) {
                            InsReport.ref.changePassword(InsReport.ref.getAuth().getProviderData().get("email").toString(),
                                    preferences.getString("password", ""), newPassword1.getText().toString(),
                                    new Firebase.ResultHandler() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(getApplicationContext(), "Пароль сменён",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onError(FirebaseError firebaseError) {
                                            Toast.makeText(getApplicationContext(), firebaseError.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else
                        if (!isPasswordValid(newPassword1.getText().toString()))
                            Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_password),
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Пароли не совпадают",
                                    Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        }).show();
    }

    String email = "";
    String password = "";

    private void resetPassword() {
        mEmailView.setError(null);
        email = mEmailView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            InsReport.ref.resetPassword(email, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), "Проверьте почту, пароль сброшен",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), "Не удалось сбросить пароль",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            loginFirebase(email,password);
        }
    }

    boolean signedUp = false;
    Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {
            savePassword(email, password);
            Map<String, String> map = new HashMap<String, String>();
            map.put("provider", authData.getProvider());
            if(authData.getProviderData().containsKey("displayName")) {
                map.put("displayName", authData.getProviderData().get("displayName").toString());
            }
            if(authData.getProviderData().containsKey("email")) {
                map.put("email", authData.getProviderData().get("email").toString());
            }
            InsReport.ref.child("users").child(authData.getUid()).setValue(map);
            finish();
        }
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            switch (firebaseError.getCode()) {
                case FirebaseError.USER_DOES_NOT_EXIST:
                    if (!signedUp)
                        signUp(email,password);
                    signedUp = true;
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    Toast.makeText(getApplicationContext(),
                            "Неверный пароль", Toast.LENGTH_LONG)
                            .show();
                    break;
                default:
                    // handle other errors
                    break;
            }

            showProgress(false);
        }
    };

    private void loginFirebase(String email, String password) {
        InsReport.ref.authWithPassword(email,password,authResultHandler);
    }

    private void loadPassword() {
        SharedPreferences preferences = getSharedPreferences("emailPasswordRecircle", MODE_PRIVATE);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView.setText(preferences.getString("email", ""));
        mPasswordView.setText(preferences.getString("password", ""));
    }

    private void signUp(final String email, final String password) {
        Log.e("Firebase","Registered user:" + " trying to sign up");
        InsReport.ref.createUser(email,password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast.makeText(getApplicationContext(), "Регистрация ОК",
                                Toast.LENGTH_LONG).show();
                        Log.e("Firebase","Registered user:" + result.get("uid") + ", giving free license");
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error]
                        Toast.makeText(getApplicationContext(), "Ошибка регистрации",
                                Toast.LENGTH_LONG).show();
                        Log.e("Firebase","Registering user ERROR: " + firebaseError.toString());
                    }
                }
        );

        InsReport.ref.authWithPassword(email,password,authResultHandler);
    }
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
