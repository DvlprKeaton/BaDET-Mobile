package com.example.badet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationForm extends AppCompatActivity {

    EditText cnum, fname, pword, cpword;
    TextView login;
    Button register;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        //EditText

        CheckBox checkBoxTerms = findViewById(R.id.checkBoxTerms);

        cnum = findViewById(R.id.cnum);
        fname = findViewById(R.id.fname);
        pword = findViewById(R.id.pword);
        cpword = findViewById(R.id.cpword);

        //TextView
        login = findViewById(R.id.login);

        //Button
        register = findViewById(R.id.btn_register);

        //Database
        DB = new DBHelper(this);

        register.setEnabled(false);

        checkBoxTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    register.setEnabled(true);

                    final Dialog dialog = new Dialog(RegistrationForm.this);
                    dialog.setContentView(R.layout.dialog_terms);
                    dialog.setCancelable(true);

                    TextView textViewTerms = dialog.findViewById(R.id.textViewTerms);
                    textViewTerms.setText("Terms and Conditions\n" +
                            "\n" +
                            "Welcome to our app! By using our app, you agree to be bound by the following terms and conditions:\n" +
                            "\n" +
                            "Use of our app\n" +
                            "Our app is provided for your personal, non-commercial use only. You may not use our app for any illegal or unauthorized purpose.\n" +
                            "\n" +
                            "Use of Location Services\n" +
                            "Our app uses your device's location services to provide location-based features such as maps and directions. By using our app, you agree to allow us to access and use your device's location data. You can turn off location services for our app in your device's settings.\n" +
                            "\n" +
                            "Use of Contact Information\n" +
                            "Our app may access your device's contact information to facilitate certain features, such as inviting friends to use the app. By using our app, you agree to allow us to access and use your device's contact information. We will not share your contact information with third parties without your consent.\n" +
                            "\n" +
                            "Use of SMS Services\n" +
                            "Our app may send SMS messages on your behalf for certain features, such as sending invites to your contacts. By using our app, you agree to allow us to send SMS messages on your behalf. Standard messaging rates may apply.\n" +
                            "\n" +
                            "Registration and Login\n" +
                            "To use certain features of our app, you may be required to register and create an account. You agree to provide accurate and complete information when creating an account. You are responsible for maintaining the confidentiality of your account information and for all activities that occur under your account.\n" +
                            "\n" +
                            "Notifications\n" +
                            "Our app may send push notifications to your device to provide updates, alerts, or other information related to the app. You can turn off push notifications for our app in your device's settings.\n" +
                            "\n" +
                            "Disclaimer of Warranty\n" +
                            "Our app is provided on an \"as is\" and \"as available\" basis without warranties of any kind, either express or implied. We do not warrant that the app will be uninterrupted or error-free.\n" +
                            "\n" +
                            "Limitation of Liability\n" +
                            "We will not be liable for any indirect, incidental, special, consequential, or punitive damages arising out of or in connection with the use of our app.\n" +
                            "\n" +
                            "Changes to Terms and Conditions\n" +
                            "We reserve the right to modify these terms and conditions at any time. Your continued use of the app after any changes to these terms and conditions will constitute your acceptance of such changes.\n" +
                            "\n" +
                            "Governing Law\n" +
                            "These terms and conditions shall be governed by and construed in accordance with the laws of the jurisdiction in which our company is located.\n" +
                            "\n" +
                            "By clicking \"Agree\" or using our app, you agree to these terms and conditions. If you do not agree to these terms and conditions, you may not use our app.");

                    Button buttonClose = dialog.findViewById(R.id.buttonClose);
                    buttonClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = cnum.getText().toString();
                String fullname = fname.getText().toString();
                String password = pword.getText().toString();
                String conpassword = cpword.getText().toString();

                if (!checkBoxTerms.isChecked()) {
                    // Display an error message
                    Toast.makeText(getApplicationContext(), "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                } else {
                    if(contact.equals("") || fullname.equals("") || password.equals("") || conpassword.equals(""))
                        Toast.makeText(RegistrationForm.this, "Please enter all missing fields", Toast.LENGTH_SHORT).show();
                    else{
                        if (password.equals(conpassword)) {

                            String url = "https://badet-portal.online/insertData.php";
                            String type = "register";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(RegistrationForm.this);
                            backgroundWorker.execute(url,type,fullname,contact,password,null);

                        }else{
                            Toast.makeText(RegistrationForm.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }



            }
        });

        String fullText = "Existing User? Click to sign in";
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Click to sign in");
        int endIndex = startIndex + "Click to sign in".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(spannableString);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}