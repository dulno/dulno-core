package com.dulno.core.member.mfa;

import com.dulno.core.member.Member;
import com.dulno.core.member.MemberDatabaseTable;
import com.google.common.collect.Lists;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class MemberMultiFactorAuth {
  private final MemberMultiFactorAuthDatabaseTable memberMultiFactorAuthDatabaseTable;
  private final MemberDatabaseTable memberDatabaseTable;
  private final UUID memberId;

  public CompletableFuture<Void> setup() {
    return memberMultiFactorAuthDatabaseTable.insertAuth(memberId, generateSecret(),
      generateRecoveryCodes());
  }

  private String generateSecret() {
    var secretGenerator = new DefaultSecretGenerator();
    return secretGenerator.generate();
  }

  private List<String> generateRecoveryCodes() {
    var recoveryCodes = new RecoveryCodeGenerator();
    return Lists.newArrayList(recoveryCodes.generateCodes(16));
  }

  public CompletableFuture<byte[]> generateQRCode() {
    return memberDatabaseTable.findMember(memberId)
      .thenCompose(member -> memberMultiFactorAuthDatabaseTable.findAuth(memberId)
        .thenApplyAsync(auth -> buildQRCode(member, auth.secret())));
  }

  private byte[] buildQRCode(Member member, String secret) {
    var data = new QrData.Builder()
      .label(member.email())
      .secret(secret)
      .issuer("Dulno")
      .algorithm(HashingAlgorithm.SHA256)
      .digits(6)
      .period(30)
      .build();
    var generator = new ZxingPngQrGenerator();
    try {
      return generator.generate(data);
    } catch (Exception exception) {
      return new byte[0];
    }
  }

  public CompletableFuture<Boolean> verifyCode(String code) {
    return memberMultiFactorAuthDatabaseTable.authExists(memberId)
      .thenCompose(exists -> verifyCode(code, exists));
  }

  private CompletableFuture<Boolean> verifyCode(
    String code, boolean multiFactorAuthEnabled
  ) {
    if (!multiFactorAuthEnabled) {
      return CompletableFuture.completedFuture(true);
    }
    if (code.contains("-")) {
      return verifyRecoveryCode(code);
    }
    var timeProvider = new SystemTimeProvider();
    var codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA256);
    var verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    return memberMultiFactorAuthDatabaseTable.findAuth(memberId)
      .thenApplyAsync(auth -> verifier.isValidCode(auth.secret(), code));
  }

  public CompletableFuture<Boolean> verifyRecoveryCode(String recoveryCode) {
    return memberMultiFactorAuthDatabaseTable.findAuth(memberId)
      .thenApply(auth -> auth.recoveryCodes().contains(recoveryCode));
  }
}
