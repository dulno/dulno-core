package com.dulno.core.stamp;

public enum StampAssignmentRole {
  UNASSIGNED,
  COLLECTION_REWARD,
  VALUE_CREDIT,
  VALUE_DEBIT,
  MEMBER_ISSUE,
  MEMBER_REVOKE;

  public boolean isUnassigned() {
    return this == UNASSIGNED;
  }

  public boolean isCollectionReward() {
    return this == COLLECTION_REWARD;
  }

  public boolean isValueCredit() {
    return this == VALUE_CREDIT;
  }

  public boolean isValueDebit() {
    return this == VALUE_DEBIT;
  }

  public boolean isMemberIssue() {
    return this == MEMBER_ISSUE;
  }

  public boolean isMemberRevoke() {
    return this == MEMBER_REVOKE;
  }
}
