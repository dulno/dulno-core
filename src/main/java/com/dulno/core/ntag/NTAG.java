package com.dulno.core.ntag;

import com.dulno.core.ntag.aes.AESCMAC;
import com.dulno.core.ntag.cmac.CMAC;
import com.dulno.core.ntag.cmac.CMACType;
import com.dulno.core.ntag.crypto.Crypto;
import com.dulno.core.ntag.lrp.LRPCMAC;
import com.dulno.core.ntag.lrp.LRPCipher;
import com.dulno.core.ntag.lrp.LRPMultiCipher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.stream.IntStream;

@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NTAG {
  public static NTAG of(
    String key, String picc, String mac, CMACType type
  ) throws Exception {
    return of(HexFormat.of().parseHex(key), HexFormat.of().parseHex(picc),
      HexFormat.of().parseHex(mac), type);
  }

  public static NTAG of(byte[] key, byte[] picc, byte[] mac, CMACType type) {
    byte[] alldata;
    if (type.isLrp()) {
      alldata = Crypto.simpleLrpDecrypt(key, 0, Arrays.copyOfRange(picc, 0, 8),
        Arrays.copyOfRange(picc, 8, 8 + 16));
    } else {
      alldata = Crypto.simpleAesDecrypt(key, picc);
    }
    byte tag = alldata[0];
    int curIdx = 1;
    byte[] uid = new byte[0];
    int counter = -1;
    if ((tag & 0b10000000) != 0) {
      uid = new byte[7];
      System.arraycopy(alldata, 1, uid, 0, 7);
      curIdx += 7;
    }
    if ((tag & 0b01000000) != 0) {
      var data = Arrays.copyOfRange(alldata, curIdx, curIdx + 3);
      counter = IntStream.range(0, data.length)
        .map(i -> ((((int) data[i]) & 0xff) << (8 * i))).sum();
    }
    return create(key, uid, counter, mac, type);
  }

  private final byte[] key;
  @Getter
  private final byte[] uid;
  @Getter
  private final int counter;
  private final byte[] mac;
  private final CMACType type;

  public boolean validate() {
    return Arrays.equals(performShortCMAC(null), mac);
  }

  private byte[] generateLRPSessionKey(byte[] macKey) {
    LRPMultiCipher multiCipher = new LRPMultiCipher(macKey);
    LRPCipher cipher = multiCipher.generateCipher(0);
    byte[] sv = generateLRPSessionVector();
    return cipher.cmac(sv);
  }

  private byte[] generateAESSessionMacKey(byte[] macKey) {
    byte[] sv = generateAESMACSessionVector();
    return Crypto.simpleAesCmac(macKey, sv);
  }

  private byte[] generateAESMACSessionVector() {
    return generateSessionVector(new byte[]{0x3c, (byte) 0xc3, 0x00, 0x01,
      0x00, (byte) 0x80}, null);
  }

  private byte[] generateSessionVector(byte[] prefix, byte[] suffix) {
    byte[] sv = new byte[16];
    System.arraycopy(prefix, 0, sv, 0, prefix.length);
    int svIdx = prefix.length;
    if (uid != null) {
      if (!Arrays.equals(uid, new byte[]{0, 0, 0, 0, 0, 0, 0})) {
        System.arraycopy(uid, 0, sv, svIdx, uid.length);
        svIdx += uid.length;
      }
    }

    if (counter > 0) {
      byte[] readCounterBytes = new byte[]{
        getByteLSB(counter, 0),
        getByteLSB(counter, 1),
        getByteLSB(counter, 2)
      };
      System.arraycopy(readCounterBytes, 0, sv, svIdx, readCounterBytes.length);
      svIdx += readCounterBytes.length;
    }
    if (suffix != null) {
      System.arraycopy(suffix, 0, sv, sv.length - suffix.length, suffix.length);
    }
    return sv;
  }

  private byte getByteLSB(long value, int byteNumber) {
    while (byteNumber > 0) {
      value = value >> 8;
      byteNumber--;
    }
    return (byte) value;
  }

  private byte[] generateLRPSessionVector() {
    return generateSessionVector(new byte[]{0x00, 0x01, 0x00, (byte) 0x80},
      new byte[]{0x1e, (byte) 0xe1});
  }

  private CMAC generateLRPCMAC() {
    LRPMultiCipher multiCipher = new LRPMultiCipher(generateLRPSessionKey(key));
    LRPCipher cipher = multiCipher.generateCipher(0);
    return new LRPCMAC(cipher);
  }

  private CMAC generateAESCMAC() {
    try {
      SecretKeySpec keySpec = new SecretKeySpec(generateAESSessionMacKey(key), "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, Crypto.zeroIVPS);
      AESCMAC mac = new AESCMAC(cipher, keySpec);
      return mac;
    } catch (NoSuchPaddingException | NoSuchAlgorithmException |
             InvalidKeyException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
      return null;
    }
  }

  private byte[] performCMAC(byte[] message) {
    CMAC cmac = type.isLrp() ? generateLRPCMAC() : generateAESCMAC();
    byte[] result = cmac.perform(message, Crypto.CMAC_SIZE);
    return result;
  }

  private byte[] performShortCMAC(byte[] message) {
    return shortenCMAC(performCMAC(message));
  }

  private byte[] shortenCMAC(byte[] originalCMAC) {
    byte[] evens = new byte[originalCMAC.length / 2];
    for (int idx = 0; idx < evens.length; idx++) {
      evens[idx] = originalCMAC[idx * 2 + 1];
    }
    return evens;
  }
}
