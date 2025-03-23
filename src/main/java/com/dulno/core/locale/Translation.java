package com.dulno.core.locale;

import com.dulno.core.member.Member;
import com.dulno.core.member.MemberDatabaseTable;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
public final class Translation {
  private final MemberDatabaseTable memberDatabaseTable;
  private final Locales locales;

  @Inject
  private Translation(
    MemberDatabaseTable memberDatabaseTable, Locales locales
  ) {
    this.memberDatabaseTable = memberDatabaseTable;
    this.locales = locales;
  }

  /**
   * Translates a locale for a member
   * @param memberId The id of the member
   * @param key The key of the locale
   * @return A future that contains the translated locale
   */
  public CompletableFuture<String> translate(UUID memberId, String key) {
    return memberDatabaseTable.findMember(memberId)
      .thenApply(member -> translate(member, key));
  }

  /**
   * Translates a locale for a member
   * @param member The member
   * @param key The key of the locale
   * @return A future that contains the translated locale
   */
  public String translate(Member member, String key) {
    return translate(member.language(), key);
  }

  /**
   * Translates a locale into a specific language
   * @param language The language
   * @param key The key of the locale
   * @return A future that contains the translated locale
   */
  public String translate(String language, String key) {
    if (!locales.hasLanguage(language)) {
      return key;
    }
    return locales.findLocale(language).get().findText(key);
  }
}
