# Virtual Scroll Access System

## Index 
- [How To Run](#how-to-run)
- [How To Use The Program](#how-to-use-the-program)
  - [As a User](#as-a-user)
  - [As an Admin](#as-an-admin)
  - [As a Guest](#as-a-guest)
- [How To Test](#how-to-test)


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

#### View Scrolls
Users can view all of the scrolls uploaded to the VSAS.\
This is shown on the homepage.

#### Filter Scrolls
If users wish to find a specific scroll, they are able to filter by Scroll ID, Uploader Username, Scroll Title and/or Upload Date.\
After pressing the Filter button, the scrolls with the specified attributes will be displayed.

#### Preview and Download Scrolls
Users can preview a scroll, which ashows al of the scroll details as well as the contents of the scroll itself.\
Users can download a scroll after previewing it. The scroll will be sent to their downloads folder.

#### Line by Line Filtering
Users can go through the scroll line by line. This can be opened from the preview screen.\
Users can enter a line number and view the line previous or after it.

#### User Profile
Users can see all of the scrolls they have uplaoded to the VSAS.\
From this, they are able to upload, update and delete scrolls.

#### Uploading Scrolls
To upload a scroll, users input the title and brosw their files for the desired scroll.\
This scroll will then be available to view.

#### Updating Scrolls
Users can only edit the title of a scroll and the scroll itself.\
Once the update is confirmed by the user, the details will be updated.

#### Deleting Scrolls
Users are able to deelte scrolls they have uploaded.\
Before deleting, a confirmation screen will be displayed ensuring users delete the correct scroll.

#### Editing User
Users are able to update all of their information, including their password.

### As an Admin
#### Login
When the app is first open, the admin is prompted to log in. If the admin enters the wrong username or password, both fields will refresh with a warning message.\
Once the admin enters the correct information, they are taken to the home page of the app. 

#### View Scrolls
Admins can view all of the scrolls uploaded to the VSAS.\
This is shown on the homepage.

#### Filter Scrolls
If admins wish to find a specific scroll, they are able to filter by Scroll ID, Uploader Username, Scroll Title and/or Upload Date.\
After pressing the Filter button, the scrolls with the specified attributes will be displayed.

#### Preview and Download Scrolls
Admins can preview a scroll, which ashows al of the scroll details as well as the contents of the scroll itself.\
Admins can download a scroll after previewing it. The scroll will be sent to their downloads folder.

#### Line by Line Filtering
Admins can go through the scroll line by line. This can be opened from the preview screen.\
Admins can enter a line number and view the line previous or after it.

#### Scroll Statistics
Admins are able to view the downloads, uploads and amount of views each scroll has.

#### Add User
Admins are able to add a user to the VSAS but entering the new user's details.

#### Delete User
Admins are abe to delete users, a confirmation message will appear ensuring the admin deletes the correct user.

### As a Guest
#### Login
When the app is first open, the user is prompted to log in. If the user does not have an account, they can continue as a guest.\
This means that they are only able to view and search for scrolls, they cannot preview or download scrolls.

#### View Scrolls
Guest users can view all of the scrolls uploaded to the VSAS.\
This is shown on the homepage.

#### Filter Scrolls
If guest users wish to find a specific scroll, they are able to filter by Scroll ID, Uploader Username, Scroll Title and/or Upload Date.\
After pressing the Filter button, the scrolls with the specified attributes will be displayed.


## How To Test
There are two steps to take when testing the program
1. To build the program:
```
gradle clean build
```
2. To test the program:
```
gradle test
```
