package com.dulno.core.ntag.lrp;

import com.dulno.core.ntag.crypto.Crypto;
import lombok.Setter;

import javax.crypto.Cipher;
import java.util.Arrays;
import java.util.LinkedList;

public class LRPCipher {
  private static final int BLOCKSIZE_BYTES = 16;
  private static final int NIBBLE_BITS = 4;

  private LRPMultiCipher multiCipher;
  private byte[] key;
  @Setter
  private long counter = 0;
  //
  @Setter
  private Integer counterSize = 8; // This is the number of **nibbles** in the counter. NOTE - the standard says that the counter size is variable, but testing says that it expects it to be fixed at 8 nibbles long, except for some SDM operations, which then go back and forth between 12 and 16.
  private LRPCMAC mac;

  public LRPCipher(LRPMultiCipher multiCipher, byte[] key) {
    this.multiCipher = multiCipher;
    this.key = key;
    mac = new LRPCMAC(this);
  }

  /**
   * Retrieves the counter as LSB nibbles, but
   * only as many as needed to represent the counter.
   *
   * @return
   */
  public int[] getCounterNibbles() {
    LinkedList<Integer> nibbles = new LinkedList<>();

    int mask = (1 << NIBBLE_BITS) - 1;
    long ctr = counter;

    while (true) {
      if (counterSize == null) { // No fixed counter size - stop when no bytes left
        if (ctr == 0) {
          break;
        }
      } else {
        if (nibbles.size() == counterSize) { // fixed counter size - stop when we reach the size
          break;
        }
      }

      int low = (int) (ctr & mask);
      nibbles.add(0, low);
      ctr = ctr >> 4;
    }

    int[] nibblesAry = new int[nibbles.size()];
    for (int i = 0; i < nibblesAry.length; i++) {
      nibblesAry[i] = nibbles.get(i);
    }

    return nibblesAry;
  }

  public byte[] evalLRP(int[] pieces, boolean isFinal) {
    // Algorithm 3 (pg. 6)
    byte[] y = key;
    for (int piece : pieces) {
      byte[] p = multiCipher.getPlaintext(piece);
      y = Crypto.simpleAesEncrypt(y, p);
    }
    if (isFinal) {
      y = Crypto.simpleAesEncrypt(
        y,
        Crypto.zeroBlock
      );
    }
    return y;
  }

  /**
   * Unpadded encryption/decryption of full 16-byte blocks.
   *
   * @param src       the material to encrypt/decrypt
   * @param cryptMode set to Cipher.DECRYPT_MODE or Cipher.ENCRYPT_MODE depending on purpose
   * @return
   */
  public byte[] cryptFullBlocks(byte[] src, int cryptMode) {
    // Algorithm 4 (pg. 7)
    if ((src.length % BLOCKSIZE_BYTES) != 0) {
      throw new RuntimeException("Bad block size");
    }

    byte[] result = new byte[src.length];
    int numBlocks = src.length / BLOCKSIZE_BYTES;
    for (int i = 0; i < numBlocks; i++) {
      int blockStart = BLOCKSIZE_BYTES * i;
      int[] x = getCounterNibbles();
      byte[] y = evalLRP(x, true);
      byte[] block = Arrays.copyOfRange(src, blockStart, blockStart + BLOCKSIZE_BYTES);
      byte[] resultBlock =
        cryptMode == Cipher.ENCRYPT_MODE
          ? Crypto.simpleAesEncrypt(y, block)
          : Crypto.simpleAesDecrypt(y, block);
      System.arraycopy(resultBlock, 0, result, blockStart, resultBlock.length);

      counter++;
    }

    return result;
  }

  public byte[] cmac(byte[] message) {
    return mac.perform(message, Crypto.CMAC_SIZE);
  }
}
