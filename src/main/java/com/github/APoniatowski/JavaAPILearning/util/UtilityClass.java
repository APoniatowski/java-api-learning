package com.github.APoniatowski.JavaAPILearning.util;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Date;

public class UtilityClass {

  public static boolean isCertificateValid(String keystorePath, String keystorePassword, String alias) {
    try {
      KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      try (InputStream keystoreStream = new FileInputStream(keystorePath)) {
        keystore.load(keystoreStream, keystorePassword.toCharArray());
      }

      Certificate cert = keystore.getCertificate(alias);
      if (cert instanceof X509Certificate) {
        X509Certificate x509Cert = (X509Certificate) cert;
        x509Cert.checkValidity(new Date());
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public static String getDefaultKeystorePath() {
    String os = System.getProperty("os.name").toLowerCase();
    String javaHome = System.getProperty("java.home");

    String keystorePath;

    if (os.contains("win")) {
      keystorePath = javaHome + "\\lib\\security\\cacerts";
    } else {
      keystorePath = javaHome + "/lib/security/cacerts";
    }

    return keystorePath;
  }

}
