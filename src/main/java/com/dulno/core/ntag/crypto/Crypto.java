package com.dulno.core.ntag.crypto;

import com.dulno.core.ntag.aes.AESCMAC;
import com.dulno.core.ntag.lrp.LRPCipher;
import com.dulno.core.ntag.lrp.LRPMultiCipher;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public final class Crypto {
  public static final int CMAC_SIZE = 16;
  public static IvParameterSpec zeroIVPS = new IvParameterSpec(new byte[16]);
  public static byte[] zeroBlock = new byte[16];

  public static byte[] simpleAesEncrypt(byte[] key, byte[] data) {
    return simpleAesEncrypt(key, data, Crypto.zeroIVPS);
  }

  public static byte[] simpleAesEncrypt(byte[] key, byte[] data, IvParameterSpec iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
      return cipher.doFinal(data);
    } catch (NoSuchPaddingException | IllegalBlockSizeException |
             BadPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
      // Should not happen
      e.printStackTrace();
      return null;
    }
  }

  public static byte[] simpleAesDecrypt(byte[] key, byte[] data) {
    return simpleAesDecrypt(key, data, Crypto.zeroIVPS);
  }

  public static byte[] simpleAesDecrypt(byte[] key, byte[] data, IvParameterSpec iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
      return cipher.doFinal(data);
    } catch (NoSuchPaddingException | IllegalBlockSizeException |
             BadPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
      // Should not happen
      e.printStackTrace();
      return null;
    }
  }

  public static byte[] simpleAesCmac(byte[] key, byte[] message) {
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    return simpleAesCmac(keySpec, message);
  }

  public static byte[] simpleAesCmac(SecretKeySpec key, byte[] message) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      cipher.init(Cipher.ENCRYPT_MODE, key, Crypto.zeroIVPS);
      AESCMAC mac = new AESCMAC(cipher, key);
      return mac.perform(message, Crypto.CMAC_SIZE);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException |
             InvalidKeyException | InvalidAlgorithmParameterException e) {
      // Should not occur
      e.printStackTrace();
      return null;
    }
  }

  public static byte[] simpleLrpDecrypt(byte[] key, int cipherNum, byte[] counterBytes, byte[] encryptedData) {
    return simpleLrpDecrypt(key, cipherNum, ByteBuffer.wrap(counterBytes).getLong(), counterBytes.length * 2, encryptedData);
  }

  public static byte[] simpleLrpDecrypt(byte[] key, int cipherNum, long counter, Integer counterSize, byte[] encryptedData) {
    LRPMultiCipher lrp = new LRPMultiCipher(key);
    LRPCipher cipher = lrp.generateCipher(cipherNum);
    cipher.setCounter(counter);
    cipher.setCounterSize(counterSize); // Not sure about this
    return cipher.cryptFullBlocks(encryptedData, Cipher.DECRYPT_MODE);
  }
}
