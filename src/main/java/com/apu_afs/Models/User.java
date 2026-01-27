package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.Gender;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;

public abstract class User {
  String ID;
  String username;
  String password;
  String firstName;
  String lastName;
  Gender gender;
  LocalDate dob;
  String email;
  String phoneNumber;
  Role role;

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

  public static final String filePath = "data/users.txt";

  public User(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.username = props.get(columnLookup.get("username")).trim();
    this.password = props.get(columnLookup.get("password")).trim();
    this.firstName = props.get(columnLookup.get("firstName")).trim();
    this.lastName = props.get(columnLookup.get("lastName")).trim();
    this.gender = Gender.fromValue(props.get(columnLookup.get("gender")).trim());
    this.dob = LocalDate.parse(props.get(columnLookup.get("dob")).trim(), Helper.dateTimeFormatter);
    this.email = props.get(columnLookup.get("email")).trim();
    this.phoneNumber = props.get(columnLookup.get("phoneNumber")).trim();
    this.role = Role.fromValue(props.get(columnLookup.get("role")).trim());
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
    this.gender = Gender.fromValue(inputValues.get("gender"));
    this.dob = LocalDate.parse(inputValues.get("dob"), Helper.dateTimeFormatter);
    this.email = inputValues.get("email");
    this.phoneNumber = inputValues.get("phoneNumber");
    this.role = Role.fromValue(inputValues.get("role"));
    this.navOptions = new ArrayList<NavOption>(Arrays.asList(
      new NavOption(Pages.DASHBOARD)
    ));
  }

  public List<String> fetchProps() {
    Map<Role, String> filePaths = Map.ofEntries(
      Map.entry(Role.ADMIN, Admin.filePath),
      Map.entry(Role.ACADEMIC_LEADER, AcademicLeader.filePath),
      Map.entry(Role.LECTURER, Lecturer.filePath),
      Map.entry(Role.STUDENT, Student.filePath)
    );

    List<String> roleUsersData = Data.fetch(filePaths.get(this.getRole()));

    for (String roleUser : roleUsersData) {
      List<String> props = new ArrayList<>(Arrays.asList(roleUser.split(", ")));

      if (props.get(columnLookup.get("id")).equals(this.getID())) {
        return props;
      }
    }

    return new ArrayList<>();
  }

  public static User userAuth(String username, String password) {
    List<String> usersData = Data.fetch(User.filePath);

    for (String user : usersData) {
      List<String> props = new ArrayList<>(Arrays.asList(user.split(", ")));

      if (props.get(columnLookup.get("username")).trim().equals(username) && props.get(columnLookup.get("password")).trim().equals(password)) {
        if (props.get(columnLookup.get("role")).trim().equals(Role.ADMIN.getValue())) {
          return new Admin(props);
        } else if (props.get(columnLookup.get("role")).trim().equals(Role.ACADEMIC_LEADER.getValue())) {
          return new AcademicLeader(props);
        } else if (props.get(columnLookup.get("role")).trim().equals(Role.LECTURER.getValue())) {
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
      List<String> props = new ArrayList<>(Arrays.asList(user.split(", ")));
      
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        if (props.get(columnLookup.get("role")).trim().equals(Role.ADMIN.getValue())) {
          return new Admin(props);
        } else if (props.get(columnLookup.get("role")).trim().equals(Role.ACADEMIC_LEADER.getValue())) {
          return new AcademicLeader(props);
        } else if (props.get(columnLookup.get("role")).trim().equals(Role.LECTURER.getValue())) {
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
      List<String> props = new ArrayList<>(Arrays.asList(user.split(", ")));
      
      if (props.get(columnLookup.get("role")).trim().equals(Role.ADMIN.getValue())) {
        users.add(new Admin(props));
      } else if (props.get(columnLookup.get("role")).trim().equals(Role.ACADEMIC_LEADER.getValue())) {
        users.add(new AcademicLeader(props));
      } else if (props.get(columnLookup.get("role")).trim().equals(Role.LECTURER.getValue())) {
        users.add(new Lecturer(props));
      } else {
        users.add(new Student(props));
      }
    }

    users.stream().filter(user -> {
      Map<String, String> userValuesLookup = Map.ofEntries(
        Map.entry("firstName", user.getID()),
        Map.entry("lastName", user.getUsername()),
        Map.entry("gender", user.getGender().getValue()),
        Map.entry("email", user.getEmail()),
        Map.entry("phoneNumber", user.getPhoneNumber()),
        Map.entry("role", user.getRole().getValue())
      );

      return userValuesLookup.get(column).equals(value);
    }).collect(Collectors.toList());

    return users;
  }

  public static List<User> fetchUsers(String search, Set<String> roleConditions) {
    List<String> usersData = Data.fetch(User.filePath);
    List<User> users = new ArrayList<>();

    for (String user : usersData) {
      List<String> props = new ArrayList<>(Arrays.asList(user.split(", ")));
      
      if (props.get(columnLookup.get("role")).trim().equals(Role.ADMIN.getValue())) {
        users.add(new Admin(props));
      } else if (props.get(columnLookup.get("role")).trim().equals(Role.ACADEMIC_LEADER.getValue())) {
        users.add(new AcademicLeader(props));
      } else if (props.get(columnLookup.get("role")).trim().equals(Role.LECTURER.getValue())) {
        users.add(new Lecturer(props));
      } else {
        users.add(new Student(props));
      }
    }

    users = users.stream().filter(user -> {
      return roleConditions.contains(user.getRole().getValue());
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

    return new Validation("Success! No invalid input", true);
  }

  public static Validation validateEditingProfile(HashMap<String, String> inputValues, String username) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"username", "email", "phoneNumber"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    // Username must be unique
    User existingUser = User.getUserByMatchingValues("username", inputValues.get("username"));
    if (existingUser != null && !existingUser.getUsername().equals(username)) {
      return new Validation("Username must be unique. '" + inputValues.get("username") + "' already exist", false, "username");
    }

    // Fields must have minimum length of
    Validation usernameMinLengthCheck = Validation.minLengthCheck(new String[] {"username"}, inputValues, 3);
    if (!usernameMinLengthCheck.getSuccess()) {
      return usernameMinLengthCheck;
    }

    // Fields cannot exceeds maximum length of
    Validation maxLengthCheck16 = Validation.maxLengthCheck(new String[] {"phoneNumber"}, inputValues, 16);
    if (!maxLengthCheck16.getSuccess()) {
      return maxLengthCheck16;
    }

    Validation maxLengthCheck32 = Validation.maxLengthCheck(new String[] {"username"}, inputValues, 32);
    if (!maxLengthCheck32.getSuccess()) {
      return maxLengthCheck32;
    }

    Validation maxLengthCheck64 = Validation.maxLengthCheck(new String[] {"email"}, inputValues, 64);
    if (!maxLengthCheck64.getSuccess()) {
      return maxLengthCheck64;
    }

    // Check if fields match a given regex pattern
    Validation usernameRegexCheck = Validation.regexCheck(new String[] {"username"}, inputValues, "^[A-Za-z][A-Za-z0-9_]*$", "Must only contain Alphabets, Numerics and Underscores");
    if (!usernameRegexCheck.getSuccess()) {
      return usernameRegexCheck;
    }

    Validation emailRegexCheck = Validation.regexCheck(new String[] {"email"}, inputValues, "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", "must be a valid email address");
    if (!emailRegexCheck.getSuccess()) {
      return emailRegexCheck;
    }

    if (!inputValues.get("currentPassword").isEmpty()) {
      User currUser = User.userAuth(username, inputValues.get("currentPassword"));
      if (currUser == null) {
        return new Validation("Your current password is invalid!", false, "currentPassword");
      }

      Validation newPasswordCannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"newPassword"}, inputValues);
      if (!newPasswordCannotBeEmptyCheck.getSuccess()) {
        return newPasswordCannotBeEmptyCheck;
      }

      Validation passwordMinLengthCheck = Validation.minLengthCheck(new String[] {"newPassword"}, inputValues, 6);
      if (!passwordMinLengthCheck.getSuccess()) {
        return passwordMinLengthCheck;
      }

      Validation newPasswordMaxLength32 = Validation.maxLengthCheck(new String[] {"newPassword"}, inputValues, 32);
      if (!newPasswordMaxLength32.getSuccess()) {
        return newPasswordMaxLength32;
      }

      Validation passwordOneLowerCase = Validation.regexCheck(new String[] {"newPassword"}, inputValues, ".*[a-z].*", "must contain at least one lowercase alphabet letter");
      if (!passwordOneLowerCase.getSuccess()) {
        return passwordOneLowerCase;
      }

      Validation passwordOneUpperCase = Validation.regexCheck(new String[] {"newPassword"}, inputValues, ".*[A-Z].*", "must contain at least one uppercase alphabet letter");
      if (!passwordOneUpperCase.getSuccess()) {
        return passwordOneUpperCase;
      }

      Validation passwordOneNumeric = Validation.regexCheck(new String[] {"newPassword"}, inputValues, ".*[0-9].*", "must contain at least one numeric");
      if (!passwordOneNumeric.getSuccess()) {
        return passwordOneNumeric;
      }

      Validation passwordOneSpecialCharacter = Validation.regexCheck(new String[] {"newPassword"}, inputValues, ".*[^a-zA-Z0-9].*", "must contain at least one special characters.");
      if (!passwordOneSpecialCharacter.getSuccess()) {
        return passwordOneSpecialCharacter;
      }
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

  public Gender getGender() {
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

  public Role getRole() {
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

  public void setGender(Gender gender) {
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

  public void setRole(Role role) {
    this.role = role;
    updateUser();
  }

  public void updateUser() {
    List<String> usersData = Data.fetch(User.filePath);
    
    // To remove the existing user old records out of the users list
    List<String> updatedUsersData = usersData.stream().filter((userRow) -> {
      List<String> props = new ArrayList<>(Arrays.asList(userRow.split(", ")));
      return !props.get(columnLookup.get("id")).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    List<String> updatedUserProps = new ArrayList<>();
    updatedUserProps.add(this.ID);
    updatedUserProps.add(this.username);
    updatedUserProps.add(this.password);
    updatedUserProps.add(this.firstName);
    updatedUserProps.add(this.lastName);
    updatedUserProps.add(this.gender.getValue());
    updatedUserProps.add(this.dob.format(Helper.dateTimeFormatter));
    updatedUserProps.add(this.email);
    updatedUserProps.add(this.phoneNumber);
    updatedUserProps.add(this.role.getValue());

    updatedUsersData.add(String.join(", ", updatedUserProps));
    Data.save(User.filePath, String.join("\n", updatedUsersData));
  }

  public void deleteUser() {
    List<String> usersData = Data.fetch(User.filePath);
    
    // To remove the existing user old records out of the users list
    List<String> updatedUsersData = usersData.stream().filter((userRow) -> {
      List<String> props = new ArrayList<>(Arrays.asList(userRow.split(", ")));
      return !props.get(columnLookup.get("id")).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    Data.save(User.filePath, String.join("\n", updatedUsersData));
  }
}
