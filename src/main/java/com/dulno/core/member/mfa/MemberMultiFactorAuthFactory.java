package com.dulno.core.member.mfa;

import com.dulno.core.member.MemberDatabaseTable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class MemberMultiFactorAuthFactory {
  private final MemberMultiFactorAuthDatabaseTable memberMultiFactorAuthDatabaseTable;
  private final MemberDatabaseTable memberDatabaseTable;

  public MemberMultiFactorAuth createAuth(UUID memberId) {
    return MemberMultiFactorAuth.create(memberMultiFactorAuthDatabaseTable,
      memberDatabaseTable, memberId);
  }
}
