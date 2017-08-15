package org.sterl.aesgcm;

import android.annotation.TargetApi;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.AEADBlockCipher;
import org.spongycastle.crypto.modes.GCMBlockCipher;
import org.spongycastle.crypto.params.AEADParameters;
import org.spongycastle.crypto.params.KeyParameter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * As encryption with Bouncy Castle is much harder the code is wrapped into this utility class
 * to make it easier to distinguish.
 *
 * AES/GCM/NoPadding
 *
 * See also:
 * https://github.com/vt-middleware/cryptacular  -- which won't work on android
 * https://www.bouncycastle.org/
 * https://rtyley.github.io/spongycastle/
 * https://blog.heckel.xyz/2014/03/01/cipherinputstream-for-aead-modes-is-broken-in-jdk7-gcm/
 */
public class GcmCipherUtil {
    // enable BouncyCastleProvider
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    private static final int MAC_SIZE = 128;
    private static final String CIPHER = "AES/GCM/NoPadding";

    private GcmCipherUtil() {
        // private
    }


    @TargetApi(24)
    public static byte[] encryptWithOs(SecretKey key, byte[] iv, CharSequence data, CharSequence aad)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        javax.crypto.spec.GCMParameterSpec spec = new javax.crypto.spec.GCMParameterSpec(MAC_SIZE, iv);

        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        if (aad != null) cipher.updateAAD(asBytes(aad)); // optional additional security
        return cipher.doFinal(asBytes(data));
    }
    @TargetApi(24)
    public static byte[] decryptWithOs(SecretKey key, byte[] iv, byte[] data, CharSequence aad)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        javax.crypto.spec.GCMParameterSpec spec = new javax.crypto.spec.GCMParameterSpec(MAC_SIZE, iv);

        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        if (aad != null) cipher.updateAAD(asBytes(aad)); // optional additional security
        return cipher.doFinal(data);
    }

    public static byte[] decryptWithBc(SecretKey key, byte[] iv, byte[] data, CharSequence aad) throws InvalidCipherTextException {
        AEADParameters spec = new AEADParameters(new KeyParameter(key.getEncoded()), MAC_SIZE, iv, asBytes(aad));

        AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
        cipher.init(false, spec);

        return doDecrypt(cipher, data);
    }

    public static byte[] encryptWithBc(SecretKey key, byte[] iv, CharSequence data, CharSequence aad) throws InvalidCipherTextException {
        AEADParameters spec = new AEADParameters(new KeyParameter(key.getEncoded()), MAC_SIZE, iv, asBytes(aad));

        AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
        cipher.init(true, spec);

        return doEncrypt(cipher, asBytes(data));
    }

    /** doEncrypt using bouncycastle */
    public static byte[] doEncrypt(final AEADBlockCipher cipher, final byte[] data) throws InvalidCipherTextException {
        final int outSize = cipher.getOutputSize(data.length);
        final byte[] output = new byte[outSize];

        int offset = cipher.processBytes(data, 0, data.length, output, 0);
        cipher.doFinal(output, offset);
        cipher.reset();
        return output;
    }

    /**
     * Returns the given string as UTF-8 bytes
     * @param value the value to get the UTF-8 bytes
     * @return the bytes or null if value is null
     */
    public static byte[] asBytes(CharSequence value) {
        if (value == null) return null;
        try {
            return value.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 missing.", e);
        }
    }

    /** doDecrypt using bouncycastle */
    public static byte[] doDecrypt(final AEADBlockCipher cipher, final byte[] data) throws InvalidCipherTextException {
        final int len = data.length;
        final int outSize = cipher.getOutputSize(len);
        final byte[] output = new byte[outSize];
        int outOff = cipher.processBytes(data, 0, len, output, 0);
        outOff += cipher.doFinal(output, outOff);
        cipher.reset();
        if (outOff < output.length) {
            final byte[] temp = new byte[outOff];
            System.arraycopy(output, 0, temp, 0, outOff);
            return temp;
        }
        return output;
    }



    /** doEncrypt using bouncycastle */
    public static byte[] doEncrypt2(final AEADBlockCipher cipher, final byte[] header, final byte[] data) throws InvalidCipherTextException {
        final int outSize = header.length + cipher.getOutputSize(data.length);
        final byte[] output = new byte[outSize];
        System.arraycopy(header, 0, output, 0, header.length);

        int outOff = header.length;
        outOff += cipher.processBytes(data, 0, data.length, output, outOff);
        cipher.doFinal(output, outOff);
        cipher.reset();
        return output;
    }
}
