package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Views.Pages;

public abstract class User {
  String ID;
  String username;
  String password;
  String firstName;
  String lastName;
  String gender;
  LocalDate dob;
  String email;
  String phoneNumber;
  String role;

  List<NavOption> navOptions;

  public static final Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("username", 1),
    Map.entry("password", 2),
    Map.entry("firstName", 3),
    Map.entry("lastName", 4),
    Map.entry("gender", 5),
    Map.entry("dob", 6),
    Map.entry("email", 7),
    Map.entry("phoneNumber", 8),
    Map.entry("role", 9)
  );

  public static final Map<String, String> genderOptions = Map.ofEntries(
    Map.entry("male", "Male"),
    Map.entry("female", "Female")
  );

  public static final Map<String, String> roleOptions = Map.ofEntries(
    Map.entry("admin", "Admin"),
    Map.entry("academic", "Academic Leader"),
    Map.entry("lecturer", "Lecturer"),
    Map.entry("student", "Student")
  );

  public static final String filePath = "data/users.txt";

  public User(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.username = props.get(columnLookup.get("username")).trim();
    this.password = props.get(columnLookup.get("password")).trim();
    this.firstName = props.get(columnLookup.get("firstName")).trim();
    this.lastName = props.get(columnLookup.get("lastName")).trim();
    this.gender = props.get(columnLookup.get("gender")).trim();
    this.dob = LocalDate.parse(props.get(columnLookup.get("dob")).trim(), Helper.dateTimeFormatter);
    this.email = props.get(columnLookup.get("email")).trim();
    this.phoneNumber = props.get(columnLookup.get("phoneNumber")).trim();
    this.role = props.get(columnLookup.get("role")).trim();
    this.navOptions = new ArrayList<NavOption>(Arrays.asList(
      new NavOption(Pages.DASHBOARD)
    ));
  }

  // Using form input values to create a new user instance
  public User(HashMap<String, String> inputValues) {
    String userID;
    // if 'id' key is populated means it is editing an existing user data
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      userID = String.valueOf(idIncrement.getUserID());
    } else {
      userID = inputValues.get("id");
    }

    this.ID = userID;
    this.username = inputValues.get("username");
    this.password = inputValues.get("password");
    this.firstName = inputValues.get("firstName");
    this.lastName = inputValues.get("lastName");
    this.gender = inputValues.get("gender");
    this.dob = LocalDate.parse(inputValues.get("dob").trim(), Helper.dateTimeFormatter);
    this.email = inputValues.get("email");
    this.phoneNumber = inputValues.get("phoneNumber");
    this.role = inputValues.get("role");
    this.navOptions = new ArrayList<NavOption>(Arrays.asList(
      new NavOption(Pages.DASHBOARD)
    ));
  }

  public static User userAuth(String username, String password) {
    List<String> usersData = Data.fetch(User.filePath);

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));

      if (props.get(columnLookup.get("username")).trim().equals(username) && props.get(columnLookup.get("password")).trim().equals(password)) {
        if (props.get(columnLookup.get("role")).trim().equals("admin")) {
          return new Admin(props);
        } else if (props.get(columnLookup.get("role")).trim().equals("academic")) {
          return new AcademicLeader(props);
        } else if (props.get(columnLookup.get("role")).trim().equals("lecturer")) {
          return new Lecturer(props);
        } else {
          return new Student(props);
        }
      }
    }

    return null;
  }

  public static User getUserByMatchingValues(String column, String value) {
    List<String> usersData = Data.fetch(User.filePath);

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));
      
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        if (props.get(columnLookup.get("role")).trim().equals("admin")) {
          return new Admin(props);
        } else if (props.get(columnLookup.get("role")).trim().equals("academic")) {
          return new AcademicLeader(props);
        } else if (props.get(columnLookup.get("role")).trim().equals("lecturer")) {
          return new Lecturer(props);
        } else {
          return new Student(props);
        }
      }
    }

    return null;
  }

  public static List<User> getListOfUsersByMatchingValues(String column, String value) {
    List<String> usersData = Data.fetch(User.filePath);
    List<User> users = new ArrayList<>();

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));
      
      if (props.get(columnLookup.get("role")).trim().equals("admin")) {
        users.add(new Admin(props));
      } else if (props.get(columnLookup.get("role")).trim().equals("academic")) {
        users.add(new AcademicLeader(props));
      } else if (props.get(columnLookup.get("role")).trim().equals("lecturer")) {
        users.add(new Lecturer(props));
      } else {
        users.add(new Student(props));
      }
    }

    users.stream().filter(user -> {
      Map<String, String> userValuesLookup = Map.ofEntries(
        Map.entry("firstName", user.getID()),
        Map.entry("lastName", user.getUsername()),
        Map.entry("gender", user.getGender()),
        Map.entry("email", user.getEmail()),
        Map.entry("phoneNumber", user.getPhoneNumber()),
        Map.entry("role", user.getRole())
      );

      return userValuesLookup.get(column).equals(value);
    }).collect(Collectors.toList());

    return users;
  }

  public static List<User> fetchUsers(String search, List<String> roleConditions) {
    List<String> usersData = Data.fetch(User.filePath);
    List<User> users = new ArrayList<>();

    for (String user : usersData) {
      List<String> props = new ArrayList<String>(Arrays.asList(user.split(", ")));
      
      if (props.get(columnLookup.get("role")).trim().equals("admin")) {
        users.add(new Admin(props));
      } else if (props.get(columnLookup.get("role")).trim().equals("academic")) {
        users.add(new AcademicLeader(props));
      } else if (props.get(columnLookup.get("role")).trim().equals("lecturer")) {
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

  public static Validation validateUser(HashMap<String, String> inputValues) {
    // Cannot be empty field
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"username", "password", "firstName", "lastName", "email", "phoneNumber"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    // Username must be unique
    User existingUser = User.getUserByMatchingValues("username", inputValues.get("username"));
    if (existingUser != null && (inputValues.get("id") == null || !existingUser.getID().equals(inputValues.get("id")))) {
      return new Validation("Username must be unique. '" + inputValues.get("username") + "' already exist", false, "username");
    }

    // Fields must have minimum length of
    Validation usernameMinLengthCheck = Validation.minLengthCheck(new String[] {"username"}, inputValues, 3);
    if (!usernameMinLengthCheck.getSuccess()) {
      return usernameMinLengthCheck;
    }

    Validation passwordMinLengthCheck = Validation.minLengthCheck(new String[] {"password"}, inputValues, 6);
    if (!passwordMinLengthCheck.getSuccess()) {
      return passwordMinLengthCheck;
    }

    // Fields cannot exceeds maximum length of
    Validation maxLengthCheck16 = Validation.maxLengthCheck(new String[] {"phoneNumber"}, inputValues, 16);
    if (!maxLengthCheck16.getSuccess()) {
      return maxLengthCheck16;
    }

    Validation maxLengthCheck32 = Validation.maxLengthCheck(new String[] {"username", "password"}, inputValues, 32);
    if (!maxLengthCheck32.getSuccess()) {
      return maxLengthCheck32;
    }

    Validation maxLengthCheck64 = Validation.maxLengthCheck(new String[] {"firstName", "lastName", "email"}, inputValues, 64);
    if (!maxLengthCheck64.getSuccess()) {
      return maxLengthCheck64;
    }

    // Check if fields match a given regex pattern
    Validation usernameRegexCheck = Validation.regexCheck(new String[] {"username"}, inputValues, "^[A-Za-z][A-Za-z0-9_]*$", "Must only contain Alphabets, Numerics and Underscores");
    if (!usernameRegexCheck.getSuccess()) {
      return usernameRegexCheck;
    }

    Validation passwordOneLowerCase = Validation.regexCheck(new String[] {"password"}, inputValues, ".*[a-z].*", "must contain at least one lowercase alphabet letter");
    if (!passwordOneLowerCase.getSuccess()) {
      return passwordOneLowerCase;
    }

    Validation passwordOneUpperCase = Validation.regexCheck(new String[] {"password"}, inputValues, ".*[A-Z].*", "must contain at least one uppercase alphabet letter");
    if (!passwordOneUpperCase.getSuccess()) {
      return passwordOneUpperCase;
    }

    Validation passwordOneNumeric = Validation.regexCheck(new String[] {"password"}, inputValues, ".*[0-9].*", "must contain at least one numeric");
    if (!passwordOneNumeric.getSuccess()) {
      return passwordOneNumeric;
    }

    Validation passwordOneSpecialCharacter = Validation.regexCheck(new String[] {"password"}, inputValues, ".*[^a-zA-Z0-9].*", "must contain at least one special characters.");
    if (!passwordOneSpecialCharacter.getSuccess()) {
      return passwordOneSpecialCharacter;
    }

    Validation emailRegexCheck = Validation.regexCheck(new String[] {"email"}, inputValues, "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", "must be a valid email address");
    if (!emailRegexCheck.getSuccess()) {
      return emailRegexCheck;
    }

    // Check if Date of birth is valid input and not after the present date
    Validation validDateCheck = Validation.validDateCheck(new String[] {"dob"}, inputValues);
    if (!validDateCheck.getSuccess()) {
      return validDateCheck;
    }

    Validation notAfterToday = Validation.maxDateCheck(new String[] {"dob"}, inputValues, LocalDate.now());
    if (!notAfterToday.getSuccess()) {
      return notAfterToday;
    }

    // Check fields that can only have fixed number of set values
    Validation genderCheck = Validation.fixedValuesCheck(new String[] {"gender"}, inputValues, User.genderOptions.keySet());
    if (!genderCheck.getSuccess()) {
      return genderCheck;
    }

    Validation roleCheck = Validation.fixedValuesCheck(new String[] {"role"}, inputValues, User.roleOptions.keySet());
    if (!roleCheck.getSuccess()) {
      return roleCheck;
    }

    return new Validation("Success! No invalid input", true);
  }

  public String getID() {
    return this.ID;
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

  public String getGender() {
    return this.gender;
  }

  public LocalDate getDob() {
    return this.dob;
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
    return this.navOptions;
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

  public void setGender(String gender) {
    this.gender = gender;
    updateUser();
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
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
    
    // To remove the existing user old records out of the users list
    List<String> updatedUsersData = usersData.stream().filter((userRow) -> {
      List<String> props = new ArrayList<String>(Arrays.asList(userRow.split(", ")));
      return !props.get(0).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    List<String> updatedUserProps = new ArrayList<String>();
    updatedUserProps.add(this.ID);
    updatedUserProps.add(this.username);
    updatedUserProps.add(this.password);
    updatedUserProps.add(this.firstName);
    updatedUserProps.add(this.lastName);
    updatedUserProps.add(this.gender);
    updatedUserProps.add(this.dob.format(Helper.dateTimeFormatter));
    updatedUserProps.add(this.email);
    updatedUserProps.add(this.phoneNumber);
    updatedUserProps.add(this.role);

    updatedUsersData.add(String.join(", ", updatedUserProps));
    Data.save(User.filePath, String.join("\n", updatedUsersData));
  }

  public void deleteUser() {
    List<String> usersData = Data.fetch(User.filePath);
    
    // To remove the existing user old records out of the users list
    List<String> updatedUsersData = usersData.stream().filter((userRow) -> {
      List<String> props = new ArrayList<String>(Arrays.asList(userRow.split(", ")));
      return !props.get(0).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    Data.save(User.filePath, String.join("\n", updatedUsersData));
  }

  // use for debugging remove in production
  public void debug() {
    System.out.println("User: ");
    System.out.println("ID: " + this.ID);
    System.out.println("Username: " + this.username);
    System.out.println("Password: " + this.password);
    System.out.println("First Name: " + this.firstName);
    System.out.println("Last Name: " + this.lastName);
    System.out.println("Gender: " + this.gender);
    System.out.println("Date of Birth" + this.dob);
    System.out.println("Email: " + this.email);
    System.out.println("Phone Number: " + this.phoneNumber);
    System.out.println("Role: " + this.role);
  }
}
