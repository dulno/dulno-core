package com.dulno.core.member.mfa;

import com.dulno.core.member.MemberDatabaseTable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class MultiFactorAuthFactory {
  private final MultiFactorAuthDatabaseTable multiFactorAuthDatabaseTable;
  private final MemberDatabaseTable memberDatabaseTable;

  public MultiFactorAuth createAuth(UUID memberId) {
    return MultiFactorAuth.create(multiFactorAuthDatabaseTable, memberDatabaseTable,
      memberId);
  }
}
