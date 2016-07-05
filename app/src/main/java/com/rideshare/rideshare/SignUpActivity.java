package com.rideshare.rideshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    /**
     * Keep track of the sign up task to ensure we can cancel it if requested.
     */
    private UserSignUpTask mSignUpTask = null;

    private EditText mNameText;
    private EditText mEmailText;
    private EditText mPasswordText;

    private View mProgressView;
    private View mSignUpFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initFields();

        Button SignUpButton = (Button) findViewById(R.id.sign_up_button);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mSignUpFormView = findViewById(R.id.sign_up_form);
        mProgressView = findViewById(R.id.sign_up_progress);
    }

    private void initFields() {
        mNameText = (EditText) findViewById(R.id.name);
        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
    }

    /**
     * Attempts to sign in or register the account specified by the sign up form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual sign up attempt is made.
     */
    private void attemptSignUp() {
        if (mSignUpTask != null) {
            return;
        }

        // Reset errors.
        mEmailText.setError(null);
        mPasswordText.setError(null);

        // Store values at the time of the sign up attempt.
        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            mNameText.setError(getString(R.string.error_field_required));
            focusView = mNameText;
            cancel = true;
        }

        // Check for a valid password.
        else if (TextUtils.isEmpty(password)) {
            mPasswordText.setError(getString(R.string.error_field_required));
            focusView = mPasswordText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordText;
            cancel = true;
        }

        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            mEmailText.setError(getString(R.string.error_field_required));
            focusView = mEmailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user sign up attempt.
            showProgress(true);
            mSignUpTask = new UserSignUpTask(email, password);
            mSignUpTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the sign up form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous sign up task.
     */
    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserSignUpTask(String email, String password) {
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
            mSignUpTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordText.setError(getString(R.string.error_incorrect_password));
                mPasswordText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mSignUpTask = null;
            showProgress(false);
        }
    }
}
