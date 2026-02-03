package com.apu_afs;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import javax.swing.ImageIcon;

public class Helper {

  public static final String dateFormat = "dd-MM-yyyy";
  public static final SimpleDateFormat simpleFormatter = new SimpleDateFormat(dateFormat); // for java.util.Date
  public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat); // for java.time.LocalDate

  public static String firstLetterUpperCase(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  public static ImageIcon iconResizer(ImageIcon icon, int width, int height) {
    Image transformedImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    ImageIcon newIcon = new ImageIcon(transformedImage);
    return newIcon;
  }

  public static boolean isValidDate(String dateString) {
    try {
      LocalDate.parse(dateString, dateTimeFormatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  public static Date convertLocalDateToDate(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  // use for multiline descriptions like bio
  public static String saveEncode(String input) {
    return input.replace("\n", "|").replace(", ", "~");
  }

  public static String saveDecode(String input) {
    return input.replace("|", "\n").replace("~", ", ");
  }
}
