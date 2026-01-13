package com.apu_afs.Models;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.apu_afs.Views.Pages;

public abstract class User {
  String username;
  String password;
  String firstName;
  String lastName;
  char gender;
  String email;
  String phoneNumber;
  String role;

  List<NavOption> navOptions;

  public static final String filePath = "data/users.txt";

  public User(List<String> props) {
    this.username = props.get(0).trim();
    this.password = props.get(1).trim();
    this.firstName = props.get(2).trim();
    this.lastName = props.get(3).trim();
    this.gender = props.get(4).trim().charAt(0);
    this.email = props.get(5).trim();
    this.phoneNumber = props.get(6).trim();
    this.role = props.get(7).trim();
    this.navOptions = new ArrayList<NavOption>(Arrays.asList(
      new NavOption(Pages.DASHBOARD)
    ));
  }

  public static User userAuth(String username, String password) {
    List<String> usersData = Data.fetch(User.filePath);

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));

      if (props.get(0).trim().equals(username) && props.get(1).trim().equals(password)) {
        if (props.get(7).trim().equals("admin")) {
          return new Admin(props);
        } else if (props.get(7).trim().equals("academic")) {
          return new AcademicLeader(props);
        } else if (props.get(7).trim().equals("lecturer")) {
          return new Lecturer(props);
        } else {
          return new Student(props);
        }
      }
    }

    return null;
  }

  public static User getUserByUsername(String username) {
    List<String> usersData = Data.fetch(User.filePath);

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));
      
      if (props.get(0).trim().equals(username)) {
        if (props.get(7).trim().equals("admin")) {
          return new Admin(props);
        } else if (props.get(7).trim().equals("academic")) {
          return new AcademicLeader(props);
        } else if (props.get(7).trim().equals("lecturer")) {
          return new Lecturer(props);
        } else {
          return new Student(props);
        }
      }
    }

    return null;
  }

  public static List<User> fetchUsers(String search, List<String> roleConditions) {
    List<String> usersData = Data.fetch(User.filePath);
    List<User> users = new ArrayList<>();

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));
      
      if (props.get(7).trim().equals("admin")) {
        users.add(new Admin(props));
      } else if (props.get(7).trim().equals("academic")) {
        users.add(new AcademicLeader(props));
      } else if (props.get(7).trim().equals("lecturer")) {
        users.add(new Lecturer(props));
      } else {
        users.add(new Student(props));
      }
    }

    users = users.stream().filter(user -> {
      return roleConditions.contains(user.getRole());
    }).filter(user -> {
      return user.getUsername().toLowerCase().contains(search.toLowerCase());
    }).collect(Collectors.toList());

    return users;
  }

  public String getUsername() {
    return this.username;
  }
  
  public String getPassword() {
    return this.password;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public char getGender() {
    return this.gender;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public String getRole() {
    return this.role;
  }

  public List<NavOption> getNavOptions() {
    return navOptions;
  }

  public void setUsername(String username) {
    this.username = username;
    updateUser();
  }

  public void setPassword(String password) {
    this.password = password;
    updateUser();
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
    updateUser();
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
    updateUser();
  }

  public void setGender(char gender) {
    this.gender = gender;
    updateUser();
  }

  public void setEmail(String email) {
    this.email = email;
    updateUser();
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    updateUser();
  }

  public void setRole(String role) {
    this.role = role;
    updateUser();
  }

  public void updateUser() {
    List<String> usersData = Data.fetch(User.filePath);
    List<String> updatedUsersData = usersData.stream().filter((userRow) -> {
      List<String> props = new ArrayList<String>(Arrays.asList(userRow.split(", ")));
      return !props.get(0).trim().equals(this.username);
    }).collect(Collectors.toCollection(ArrayList::new));

    List<String> updatedUserProps = new ArrayList<String>();
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

  // use for debugging remove in production
  public void debug() {
    System.out.println("User: ");
    System.out.println("Username: " + this.username);
    System.out.println("Password: " + this.password);
    System.out.println("First Name: " + this.firstName);
    System.out.println("Last Name: " + this.lastName);
    System.out.println("Gender: " + this.gender);
    System.out.println("Email: " + this.email);
    System.out.println("Phone Number: " + this.phoneNumber);
    System.out.println("Role: " + this.role);
  }
}
