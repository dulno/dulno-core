package com.dulno.core.card;

public enum CardType {
  COLLECTION,
  VALUE,
  MEMBER;

  public boolean isCollection() {
    return this == COLLECTION;
  }

  public boolean isValue() {
    return this == VALUE;
  }

  public boolean isMember() {
    return this == MEMBER;
  }
}
