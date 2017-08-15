package org.sterl.aesgcm;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.AEADBlockCipher;
import org.spongycastle.crypto.modes.GCMBlockCipher;
import org.spongycastle.crypto.params.AEADParameters;
import org.spongycastle.crypto.params.KeyParameter;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private TextView txtOut;
    private EditText txtIn;
    private Button cmdDo;
    private Button cmdVs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOut = (TextView)findViewById(R.id.txtOut);
        txtIn = (EditText)findViewById(R.id.txtInput);
        cmdDo = (Button)findViewById(R.id.cmdEncrypt);
        cmdVs = (Button)findViewById(R.id.cmdBuildInVsBouncycastle);

        cmdDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EncryptTask().execute(txtIn.getText());
            }
        });

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            cmdVs.setEnabled(true);
            cmdVs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new BouncyCastleVsAndroidTask().execute(txtIn.getText());
                }
            });
        }
    }

    // generate a shared secret / key -- like a password -- 256 Bit strong
    volatile SecretKey key;

    private abstract class AbstractEncryptTask extends AsyncTask<CharSequence, Void, String> {
        protected Exception e = null;
        @Override
        protected void onPreExecute() {
            txtOut.setText("Encrypting data ...");
            txtOut.setEnabled(false);
            txtIn.setEnabled(false);
            cmdDo.setEnabled(false);
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
    }

    private class EncryptTask extends AbstractEncryptTask {
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

                // additional authentication data, e.g. user id etc.
                // needs to be the same during encryption and decryption, here a static string
                byte[] aad = "AAD String, e.g. user ID".getBytes();

                // in GCM mode always 12 bytes -- needs to be shared between encryption and decryption
                // should change for each encrypted message / data
                //    -- GCM nonce --
                final byte[] initialVector = new byte[12];
                random.nextBytes(initialVector);

                byte[] messageEncrypted;
                byte[] messageDecrypted;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    // Android 4.4 or newer ...

                    javax.crypto.spec.GCMParameterSpec spec = new javax.crypto.spec.GCMParameterSpec(128, initialVector);
                    // AES/GCM is already a block cypher, no additional padding supported
                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

                    // doEncrypt the given message
                    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
                    cipher.updateAAD(aad); // optional additional security
                    messageEncrypted = cipher.doFinal(params[0].toString().getBytes("UTF-8"));

                    // doDecrypt it again
                    cipher.init(Cipher.DECRYPT_MODE, key, spec);
                    cipher.updateAAD(aad); // optional additional security
                    messageDecrypted = cipher.doFinal(messageEncrypted);

                } else {
                    // Android 4.3 or older ...

                    // combination of Key, Mac size and IV for GCM, basically GCMParameterSpec + SecretKey
                    AEADParameters spec = new AEADParameters(new KeyParameter(key.getEncoded()), 128, initialVector);

                    // with bouncycastle we we have to use the AEADBlockCipher
                    AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
                    cipher.init(true, spec);

                    messageEncrypted = GcmCipherUtil.doEncrypt(cipher, params[0].toString().getBytes("UTF-8"));

                    cipher.init(false, spec);
                    messageDecrypted = GcmCipherUtil.doDecrypt(cipher, messageEncrypted);
                }

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
    }



    private class BouncyCastleVsAndroidTask extends AbstractEncryptTask {
        private static final String TAG = "AndroidVsBc";
        @Override
        protected String doInBackground(CharSequence... data) {
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    final SecureRandom random = new SecureRandom();
                    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                    keyGen.init(256, random);
                    key = keyGen.generateKey();

                    final byte[] initialVector = new byte[12];
                    random.nextBytes(initialVector);
                    final String aad = "my aad";

                    byte[] encrypted = GcmCipherUtil.encryptWithOs(key, initialVector, data[0], aad);

                    Log.d(TAG, "DATA size:                      " + GcmCipherUtil.asBytes(data[0]).length);
                    Log.d(TAG, "OS encrypted size:              " + encrypted.length);
                    byte[] decrypted = GcmCipherUtil.decryptWithBc(key, initialVector, encrypted, aad);
                    Log.d(TAG, "BC decrypted size:              " + decrypted.length);


                    if (!new String(decrypted, "UTF-8").equals(data[0].toString())) {
                        e = new IllegalStateException("Decrypt OS Cipher with Bouncycastle decrypt doesn't match " + new String(decrypted, "UTF-8"));
                    }

                    encrypted = GcmCipherUtil.encryptWithBc(key, initialVector, data[0], aad);
                    Log.d(TAG, "BC encrypted size:              " + encrypted.length);
                    decrypted = GcmCipherUtil.decryptWithOs(key, initialVector, encrypted, aad);
                    Log.d(TAG, "OS decrypted size:              " + decrypted.length);

                    if (!new String(decrypted, "UTF-8").equals(data[0].toString())) {
                        e = new IllegalStateException("Decrypt BC with OS Cipher decrypt doesn't match " + new String(decrypted, "UTF-8"));
                    }

                } else {
                    e = new IllegalStateException("Wrong API version need at least 19 but has " + Build.VERSION.SDK_INT);
                }
            } catch (Exception e) {
                this.e = e;
            }
            return "";
        }
    }
}
