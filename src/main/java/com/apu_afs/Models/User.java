package com.apu_afs.Models;

import java.util.*;
import java.util.stream.Collectors;

public class User {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private char gender;
  private String email;
  private String phoneNumber;
  private String role;

  private static final String filePath = "data/users.txt";

  public User(ArrayList<String> props) {
    this.username = props.get(0);
    this.password = props.get(1);
    this.firstName = props.get(2);
    this.lastName = props.get(3);
    this.gender = props.get(4).charAt(0);
    this.email = props.get(5);
    this.phoneNumber = props.get(6);
    this.role = props.get(7);
  }

  public static boolean doesUserExists(String username) {
    ArrayList<String> usersData = Data.fetch(User.filePath);

    boolean isMatching = false;
    for (String user : usersData) {
      ArrayList<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));
      
      isMatching = props.get(0).equals(username);
      if (isMatching) {
        break;
      }
    }

    return isMatching;
  }

  public static User userAuth(String username, String password) {
    ArrayList<String> usersData = Data.fetch(User.filePath);

    for (String user : usersData) {
      ArrayList<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));

      if (props.get(0).equals(username) && props.get(1).equals(password)) {
        return new User(props);
      }
    }

    return null;
  }

  public String getUsername() {
    return username;
  }
  
  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public char getGender() {
    return gender;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getRole() {
    return role;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setGender(char gender) {
    this.gender = gender;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public void updateUser() {
    ArrayList<String> usersData = Data.fetch(User.filePath);
    ArrayList<String> updatedUsersData = usersData.stream().filter((userRow) -> {
      ArrayList<String> props = new ArrayList<String>(Arrays.asList(userRow.split(", ")));
      return !props.get(0).equals(this.username);
    }).collect(Collectors.toCollection(ArrayList::new));

    ArrayList<String> updatedUserProps = new ArrayList<String>();
    updatedUserProps.add(this.username);
    updatedUserProps.add(this.password);
    updatedUserProps.add(this.firstName);
    updatedUserProps.add(this.lastName);
    updatedUserProps.add(String.valueOf(this.gender));
    updatedUserProps.add(this.email);
    updatedUserProps.add(this.phoneNumber);
    updatedUserProps.add(this.role);

    updatedUsersData.add(String.join(", ", updatedUserProps));
    Data.save(User.filePath, String.join("\n", updatedUsersData));
  }
}
