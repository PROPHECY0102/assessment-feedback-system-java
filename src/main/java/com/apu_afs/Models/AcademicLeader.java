package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class AcademicLeader extends User {
  private String faculty;
  private LocalDate employedAt;


  public static final String filePath = "data/academicLeaders.txt";
  
  public AcademicLeader(List<String> props) {
    super(props);
  }

  public AcademicLeader(HashMap<String, String> inputValues) {
    super(inputValues);
  }

  public static AcademicLeader getAcademicLeaderByMatchingValues(String column, String value) {
    List<String> usersData = Data.fetch(AcademicLeader.filePath);

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));
      
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new AcademicLeader(props);
      }
    }

    return null;
  }

  @Override
  public void updateUser() {
    super.updateUser();
  }
}
