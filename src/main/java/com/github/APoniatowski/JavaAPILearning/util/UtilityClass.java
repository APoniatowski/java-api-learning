package com.github.APoniatowski.JavaAPILearning.util;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

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
      UtilityClass.logError("Certificate validation error: ", e);
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

  private static final Logger logger = Logger.getLogger(UtilityClass.class.getName());

  static {
    try {
      FileHandler fileHandler = new FileHandler("errors.log", true);
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);
      logger.setLevel(Level.ALL);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error setting up file logger", e);
    }
  }

  public static void logError(String message, Throwable ex) {
    if (ex != null) {
      logger.log(Level.SEVERE, message, ex);
    } else {
      logger.log(Level.SEVERE, message);
    }
  }

}
