package com.dulno.core.ntag.cmac;

import java.util.LinkedList;
import java.util.List;

public abstract class CMAC {
  protected static boolean[] RB_128;

  static {
    RB_128 = new boolean[128];
    RB_128[120] = true;
    RB_128[125] = true;
    RB_128[126] = true;
    RB_128[127] = true;
  }

  public abstract byte[] perform(byte[] message, int length);

  protected boolean[] shiftLeft(boolean[] ary, int shifts) {
    boolean[] newArray = new boolean[ary.length];
    System.arraycopy(ary, shifts, newArray, 0, ary.length - shifts);
    return newArray;
  }

  protected boolean[] msb(boolean[] ary, int bits) {
    boolean[] newAry = new boolean[bits];
    System.arraycopy(ary, 0, newAry, 0, bits);
    return newAry;
  }

  protected byte[] toByteArray(boolean[] ary) {
    byte[] newAry = new byte[ary.length / 8];
    int bitOffset = 0;
    for (int idx = 0; idx < newAry.length; idx++) {
      int b = 0x00;
      for (int bitIdx = 0; bitIdx < 8; bitIdx++) {
        if (ary[bitOffset]) {
          b = b | (1 << (7 - bitIdx));
        }
        bitOffset++;
      }
      newAry[idx] = (byte) b;
    }
    return newAry;
  }

  protected boolean[] xor(boolean[] a1, boolean[] a2) {
    boolean[] result = new boolean[a1.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = a1[i] ^ a2[i];
    }
    return result;
  }

  protected boolean[] toBitArray(byte[] ary) {
    boolean[] bits = new boolean[ary.length * 8];

    int bitOffset = 0;
    for (int idx = 0; idx < ary.length; idx++) {
      int b = ary[idx];
      int comparator = 0b10000000;
      for (int bitIdx = 0; bitIdx < 8; bitIdx++) {
        int result = b & comparator;
        bits[bitOffset] = result != 0;
        comparator = comparator >> 1;
        bitOffset++;
      }
    }

    return bits;
  }

  protected List<boolean[]> groupBlocks(boolean[] data, int groupSize) {
    int fullGroups = data.length / groupSize;
    List<boolean[]> newData = new LinkedList<>();
    for (int grpIdx = 0; grpIdx < fullGroups; grpIdx++) {
      boolean[] group = new boolean[groupSize];
      System.arraycopy(data, grpIdx * groupSize + 0, group, 0, groupSize);
      newData.add(group);
    }

    int remaining = data.length % groupSize;
    if (remaining > 0) {
      int startIdx = fullGroups * groupSize;
      boolean[] group = new boolean[remaining];
      for (int idx = 0; idx < remaining; idx++) {
        group[idx] = data[startIdx];
        startIdx += 1;
      }
      newData.add(group);
    }

    return newData;
  }

  protected boolean[] padblock(boolean[] ary, int length) {
    boolean[] paddedBlock = new boolean[length];
    for (int idx = 0; idx < paddedBlock.length; idx++) {
      if (idx < ary.length) {
        paddedBlock[idx] = ary[idx];
      } else {
        paddedBlock[idx] = idx == ary.length;
      }
    }
    return paddedBlock;
  }
}
