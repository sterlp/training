package org.sterl.aesgcm;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.AEADBlockCipher;
import org.spongycastle.crypto.modes.GCMBlockCipher;
import org.spongycastle.crypto.params.AEADParameters;
import org.spongycastle.crypto.params.KeyParameter;

import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    // enable BouncyCastleProvider
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

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
                    // the JDK version does for some secret reason Padding, so no action needed here ...
                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

                    // encrypt the given message
                    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
                    cipher.updateAAD(aad); // optional additional security
                    messageEncrypted = cipher.doFinal(params[0].toString().getBytes("UTF-8"));

                    // decrypt it again
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

                    messageEncrypted = encrypt(cipher, initialVector, params[0].toString().getBytes("UTF-8"));

                    cipher.init(false, spec);
                    messageDecrypted = decrypt(cipher, messageEncrypted, 12); // default header in GCM is 12 bit
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
        /** encrypt using bouncycastle */
        private byte[] encrypt(final AEADBlockCipher cipher, final byte[] header, final byte[] data) throws InvalidCipherTextException {
            final int outSize = header.length + cipher.getOutputSize(data.length);
            final byte[] output = new byte[outSize];
            System.arraycopy(header, 0, output, 0, header.length);

            int outOff = header.length;
            outOff += cipher.processBytes(data, 0, data.length, output, outOff);
            cipher.doFinal(output, outOff);
            cipher.reset();
            return output;
        }

        /** decrypt using bouncycastle */
        private byte[] decrypt(final AEADBlockCipher cipher, final byte[] data, final int inOff) throws InvalidCipherTextException {
            final int len = data.length - inOff;
            final int outSize = cipher.getOutputSize(len);
            final byte[] output = new byte[outSize];
            int outOff = cipher.processBytes(data, inOff, len, output, 0);
            outOff += cipher.doFinal(output, outOff);
            cipher.reset();
            if (outOff < output.length) {
                final byte[] temp = new byte[outOff];
                System.arraycopy(output, 0, temp, 0, outOff);
                return temp;
            }
            return output;
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
