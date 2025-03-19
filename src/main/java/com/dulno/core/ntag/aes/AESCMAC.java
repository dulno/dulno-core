package com.dulno.core.ntag.aes;

import com.dulno.core.ntag.cmac.CMAC;
import com.dulno.core.ntag.crypto.Crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.LinkedList;
import java.util.List;

public class AESCMAC extends CMAC {
  private static final int BLOCKSIZE_BITS = 128;

  private final Cipher cipher;
  private final SecretKeySpec key;
  private boolean[] subkey1;
  private boolean[] subkey2;

  public AESCMAC(Cipher cipher, SecretKeySpec key) {
    this.cipher = cipher;
    this.key = key;
    try {
      subkey1 = generateSubkey1();
      subkey2 = generateSubkey2();
    } catch (InvalidKeyException | InvalidAlgorithmParameterException |
             IllegalBlockSizeException
             | BadPaddingException e) {
      // Should not happen
      e.printStackTrace();
    }
  }

  private boolean[] generateSubkey1() throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    cipher.init(Cipher.ENCRYPT_MODE, key, Crypto.zeroIVPS); // Pg. 24

    boolean[] zeroBlockBits = new boolean[BLOCKSIZE_BITS];
    boolean[] l = toBitArray(cipher.doFinal(toByteArray(zeroBlockBits)));
    boolean[] l_shift = shiftLeft(l, 1);

    if (!msb(l, 1)[0]) {
      return l_shift;
    } else {
      return xor(l_shift, RB_128);
    }
  }

  private boolean[] generateSubkey2() throws InvalidKeyException, InvalidAlgorithmParameterException {
    cipher.init(Cipher.ENCRYPT_MODE, key, Crypto.zeroIVPS); // Pg. 24

    boolean[] k1_shift = shiftLeft(subkey1, 1);
    if (!msb(subkey1, 1)[0]) {
      return k1_shift;
    } else {
      return xor(k1_shift, RB_128);
    }
  }

  public byte[] perform(byte[] message, int lengthBytes) {
    if (message == null) {
      message = new byte[0];
    }
    try {
      cipher.init(Cipher.ENCRYPT_MODE, key, Crypto.zeroIVPS); // Pg. 24

      int length = lengthBytes * 8;
      // Block out message into groups (Steps 2-3)
      boolean[] messageBits = toBitArray(message);
      List<boolean[]> m_list = groupBlocks(messageBits, BLOCKSIZE_BITS);
      if (m_list.size() == 0) {
        m_list = new LinkedList<>();
        m_list.add(new boolean[0]);
      }

      boolean[] last_block = m_list.get(m_list.size() - 1);

      // Use subkeys to finish out last block (Step 4)
      if (last_block.length != BLOCKSIZE_BITS) {
        last_block = xor(padblock(last_block, BLOCKSIZE_BITS), subkey2);
      } else {
        last_block = xor(last_block, subkey1);
      }
      m_list.set(m_list.size() - 1, last_block);

      // Perform the hashing (Steps 5-6)
      boolean[] c_curr = new boolean[BLOCKSIZE_BITS];
      for (boolean[] m_block : m_list) {

        c_curr = toBitArray(cipher.doFinal(toByteArray(xor(c_curr, m_block))));

      }

      // Step 7
      boolean[] t = msb(c_curr, length);

      // Return as a byte array
      return toByteArray(t);
    } catch (IllegalBlockSizeException | BadPaddingException |
             InvalidKeyException | InvalidAlgorithmParameterException e) {
      // Should not occur
      e.printStackTrace();
      return null;
    }
  }
}
