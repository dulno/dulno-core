package com.dulno.core.ntag.lrp;

import com.dulno.core.ntag.cmac.CMAC;
import com.dulno.core.ntag.crypto.Crypto;

import java.util.List;

public class LRPCMAC extends CMAC {
  private static final int BLOCKSIZE_BYTES = 16;
  private static final int BLOCKSIZE_BITS = BLOCKSIZE_BYTES * 8;

  private LRPCipher cipher;
  private boolean[] subkey1;
  private boolean[] subkey2;

  public LRPCMAC(LRPCipher cipher) {
    this.cipher = cipher;
    subkey1 = generateSubkey1();
    subkey2 = generateSubkey2();
  }

  private boolean[] generateSubkey1() {
    boolean[] l = toBitArray(cipher.evalLRP(bytesToNibbles(Crypto.zeroBlock), true));
    boolean[] l_shift = shiftLeft(l, 1);

    if (!msb(l, 1)[0]) {
      return l_shift;
    } else {
      return xor(l_shift, RB_128);
    }
  }

  private boolean[] generateSubkey2() {
    boolean[] k1_shift = shiftLeft(subkey1, 1);
    if (!msb(subkey1, 1)[0]) {
      return k1_shift;
    } else {
      return xor(k1_shift, RB_128);
    }
  }

  public byte[] perform(byte[] message, int lengthBytes) {
    int length = lengthBytes * 8;
    if (message == null) {
      message = new byte[0];
    }
    // Block out message into groups (Steps 2-3)
    int b = BLOCKSIZE_BITS;
    boolean[] messageBits = toBitArray(message);
    List<boolean[]> m_list = groupBlocks(messageBits, b);
    if (m_list.size() == 0) {
      m_list.add(new boolean[0]);
    }

    boolean[] last_block = m_list.get(m_list.size() - 1);

    // Use subkeys to finish out last block (Step 4)
    if (last_block.length != b) {
      last_block = xor(padblock(last_block, b), subkey2);
    } else {
      last_block = xor(last_block, subkey1);
    }
    m_list.set(m_list.size() - 1, last_block);

    // Perform the hashing (Steps 5-6)
    boolean[] c_curr = new boolean[b];
    for (boolean[] m_block : m_list) {
      c_curr = toBitArray(cipher.evalLRP(bytesToNibbles(toByteArray(xor(c_curr, m_block))), true));
    }

    // Step 7
    boolean[] t = msb(c_curr, length);

    // Return as a byte array
    return toByteArray(t);
  }

  /**
   * Converts an array of bytes to an array of nibbles.
   * The result is an int array simply for convenience,
   * both in implementation and in usage of results.
   *
   * @param bytes
   * @return
   */
  private int[] bytesToNibbles(byte[] bytes) {
    int[] results = new int[bytes.length * 2];
    for (int i = 0; i < bytes.length; i++) {
      int bval = bytes[i];
      bval = bval & 0xff;
      int rightNibble = bval & 0xf;
      int leftNibble = bval >> 4;
      results[i * 2] = leftNibble;
      results[i * 2 + 1] = rightNibble;
    }
    return results;
  }
}
