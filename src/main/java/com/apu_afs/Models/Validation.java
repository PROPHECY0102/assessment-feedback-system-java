package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

import com.apu_afs.Helper;

public class Validation {
  private String message; // message for optionpane alerting user of validation outcome or be use internally to debug
  private Boolean success; // boolean whether it is valid or invalid
  private String field;

  public Validation(boolean success) {
    this.success = success;
  }

  // When the validation succeeds no need to assign the field property
  public Validation(String message, boolean success) {
    this.message = message;
    this.success = Boolean.valueOf(success);
  }

  // field property can be use to identify where the issue lies such as textfield responsible to change its appearance later
  public Validation(String message, boolean success, String field) {
    this.message = message;
    this.success = Boolean.valueOf(success);
    this.field = field;
  }

  public String getMessage() {
    return this.message;
  }

  public Boolean getSuccess() {
    return this.success;
  }

  public String getField() {
    return this.field;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public void setField(String field) {
    this.field = field;
  }

  public static Validation isEmptyCheck(String[] columns, HashMap<String, String> inputValues) {
    Validation response = new Validation(true);

    for (String column : columns) {
      if (inputValues.get(column).isEmpty()) {
        response.setMessage(Helper.firstLetterUpperCase(column) + " cannot be empty");
        response.setSuccess(false);
        response.setField(column);
        break;
      }
    }

    return response;
  }

  public static Validation minLengthCheck(String[] columns, HashMap<String, String> inputValues, int minLength) {
    Validation response = new Validation(true);

    for (String column : columns) {
      if (inputValues.get(column).length() < minLength) {
        response.setMessage(Helper.firstLetterUpperCase(column) + " must be at least " + minLength +  " letters long");
        response.setSuccess(false);
        response.setField(column);
        break;
      }
    }

    return response;
  }

  public static Validation maxLengthCheck(String[] columns, HashMap<String, String> inputValues, int maxLength) {
    Validation response = new Validation(true);

    for (String column : columns) {
      if (inputValues.get(column).length() > maxLength) {
        response.setMessage(Helper.firstLetterUpperCase(column) + " cannot exceed " + maxLength +  " characters in length");
        response.setSuccess(false);
        response.setField(column);
        break;
      }
    }

    return response;
  }

  public static Validation regexCheck(String[] columns, HashMap<String, String> inputValues, String regex, String info) {
    Validation response = new Validation(true);

    for (String column : columns) {
      if (!inputValues.get(column).matches(regex)) {
        response.setMessage(Helper.firstLetterUpperCase(column) + " is invalid! " + info);
        response.setSuccess(false);
        response.setField(column);
        break;
      }
    }

    return response;
  }

  public static Validation validDateCheck(String[] columns, HashMap<String, String> inputValues) {
    Validation response = new Validation(true);

    for (String column : columns) {
      if (!Helper.isValidDate(inputValues.get(column))) {
        response.setMessage(Helper.firstLetterUpperCase(column) + " must be a valid date in the format of (dd-MM-yyyy) example: 21-01-2003");
        response.setSuccess(false);
        response.setField(column);
        break;
      }
    }

    return response;
  }

  // only for fields that passed validDateCheck
  public static Validation maxDateCheck(String[] columns, HashMap<String, String> inputValues, LocalDate maxDate) {
    Validation response = new Validation(true);

    for (String column : columns) {
      LocalDate inputDate = LocalDate.parse(inputValues.get(column), Helper.dateTimeFormatter);
      if (inputDate.isAfter(maxDate)) {
        response.setMessage(Helper.firstLetterUpperCase(column) + " must be before " + maxDate.format(Helper.dateTimeFormatter));
        response.setSuccess(false);
        response.setField(column);
        break;
      }
    }

    return response;
  }


}
