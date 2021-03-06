package com.rideshare.rideshare.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rideshare.rideshare.AppController;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.helpers.AuthenticationHelper;
import com.rideshare.rideshare.helpers.ProgressBarHelper;
import com.rideshare.rideshare.helpers.ResponseParserHelper;
import com.rideshare.rideshare.helpers.URLHelper;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

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
        // Reset errors.
        mNameText.setError(null);
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
        } else if (!AuthenticationHelper.isPasswordValid(password)) {
            mPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordText;
            cancel = true;
        }

        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            mEmailText.setError(getString(R.string.error_field_required));
            focusView = mEmailText;
            cancel = true;
        } else if (!AuthenticationHelper.isEmailValid(email)) {
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
            ProgressBarHelper.showProgress(true, mSignUpFormView, mProgressView,
                    getResources().getInteger(android.R.integer.config_shortAnimTime));
            register(name, email, password);
        }
    }

    private void register(final String name, final String email, final String password) {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressBarHelper.showProgress(false, mSignUpFormView, mProgressView,
                        getResources().getInteger(android.R.integer.config_shortAnimTime));

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarHelper.showProgress(false, mSignUpFormView, mProgressView,
                        getResources().getInteger(android.R.integer.config_shortAnimTime));

                ResponseParserHelper.handleError(error, getBaseContext());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
