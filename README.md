# Aerin_Lab09_Group03 - Virtual Scroll Access System

## How To Run
There are two steps to take when running the program:
1. To build the program:
```
gradle build
```
2. To run the program:
```
gradle run
```

## How To Use The Program
### As a User
#### Login 
When the app is first open, the user is prompted to log in. If the user enters the wrong username or password, both fields will refresh with a warning message.\
Once the user enters the correct information, they are taken to the home page of the app.

#### Register
If the user does not have an account, they are able to create an account.\
After account creation, the user is taken back to the login page and they are prompted to login.\
After login, the user can how access the system fully.

### As a Admin
### Login
When the app is first open, the admin is prompted to log in. If the admin enters the wrong username or password, both fields will refresh with a warning message.\
Once the admin enters the correct information, they are taken to the home page of the app.

### As a Guest
#### Login
When the app is first open, the user is prompted to log in. If the user does not have an account, they can continue as a guest.\
This means that they are only able to view and search for scrolls, they cannot preview or download scrolls.

## How To Test
There are two steps to take when testing the program
1. To build the program:
```
gradle build
```
2. To test the program:
```
gradle test
```
