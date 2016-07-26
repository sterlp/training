package org.sterl.aesgcm;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class MainActivity extends AppCompatActivity {

    private TextView txtOut;
    private EditText txtIn;
    private Button cmdDo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOut = (TextView)findViewById(R.id.txtOut);
        txtIn = (EditText)findViewById(R.id.txtInput);
        cmdDo = (Button)findViewById(R.id.cmdEncrypt);

        cmdDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EncryptTask().execute(txtIn.getText());
            }
        });
    }

    // generate a shared secret / key -- like a password -- 256 Bit strong
    volatile SecretKey key;
    private class EncryptTask extends AsyncTask<CharSequence, Void, String> {
        Exception e = null;

        @Override
        protected String doInBackground(CharSequence... params) {
            try {
                final SecureRandom random = new SecureRandom();

                // generate a shared secret / key -- like a password -- 256 Bit strong
                if (key == null) {
                    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                    keyGen.init(256, random);
                    key = keyGen.generateKey();
                }

                // in GCM mode always 12 bytes -- needs to be shared between encryption and decryption
                // should change for each encrypted message / data
                final byte[] initialVector = new byte[12];
                random.nextBytes(initialVector);

                // additional authentication data, e.g. user id etc.
                // needs to be the same during encryption and decryption, here a static string
                byte[] aad = "AAD String, e.g. user ID".getBytes();

                GCMParameterSpec spec = new GCMParameterSpec(128, initialVector);

                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

                // encrypt the given message
                cipher.init(Cipher.ENCRYPT_MODE, key, spec);
                cipher.updateAAD(aad);
                byte[] messageEncrypted = cipher.doFinal(params[0].toString().getBytes("UTF-8"));

                // decrypt it again
                cipher.init(Cipher.DECRYPT_MODE, key, spec);
                cipher.updateAAD(aad);
                byte[] messageDecrypted = cipher.doFinal(messageEncrypted);

                // verify that the decrypted message matches the encrypted one
                if (!new String(messageDecrypted, "UTF-8").equals(params[0].toString())) {
                    e = new IllegalStateException("Decrypted message doesn't match source: " + new String(messageDecrypted, "UTF-8"));
                }
                return Base64.encodeToString(messageEncrypted, Base64.DEFAULT);
            } catch (Exception e) {
                this.e = e;
            }
            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            txtOut.setEnabled(true);
            txtIn.setEnabled(true);
            cmdDo.setEnabled(true);

            txtOut.setText(result);

            if (e == null) {
                Snackbar.make(findViewById(android.R.id.content), "Encryption & Decryption finished.", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Encryption failed! " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                txtOut.setText(e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            txtOut.setText("Encrypting data ...");
            txtOut.setEnabled(false);
            txtIn.setEnabled(false);
            cmdDo.setEnabled(false);
        }
    }
}
