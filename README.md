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
Once the user enters the correct information, they are taken to the home page of the app. \
<img src="https://github.com/user-attachments/assets/ae22f8f8-b0de-4ea3-9dcc-7af4e94c8951" width="50%">

#### Register
If the user does not have an account, they are able to create an account.\
After account creation, the user is taken back to the login page and they are prompted to login.\
After login, the user can how access the system fully.\
<img src="https://github.com/user-attachments/assets/04fd1a18-433a-4c80-b3f5-3985895cb91b" width="50%">

#### View Scrolls
Users can view all of the scrolls uploaded to the VSAS.\
This is shown on the homepage.\
<img src="https://github.com/user-attachments/assets/d3af59ea-fc65-4dc7-991b-75f75e6f0048" width="50%">

#### Filter Scrolls
If users wish to find a specific scroll, they are able to filter by Scroll ID, Uploader Username, Scroll Title and/or Upload Date.\
After pressing the Filter button, the scrolls with the specified attributes will be displayed.\
<img src="https://github.com/user-attachments/assets/34e57e71-d151-4287-b67f-d77a9969b50c" width="50%">

#### Preview and Download Scrolls
Users can preview a scroll, which ashows al of the scroll details as well as the contents of the scroll itself.\
Users can download a scroll after previewing it. The scroll will be sent to their downloads folder. \
<img src="https://github.com/user-attachments/assets/32a0cb7e-5aed-4db9-81b4-65e25071cafa" width="50%">

#### Line by Line Filtering
Users can go through the scroll line by line. This can be opened from the preview screen.\
Users can enter a line number and view the line previous or after it. \
<img src="https://github.com/user-attachments/assets/77274d42-5a12-4c6a-8eb0-3332c59412c2" width="50%">

#### User Profile
Users can see all of the scrolls they have uplaoded to the VSAS.\
From this, they are able to upload, update and delete scrolls.\
<img src="https://github.com/user-attachments/assets/ed36e818-8407-470e-a933-b93aaca85f09" width="50%">

#### Uploading Scrolls
To upload a scroll, users input the title and brosw their files for the desired scroll.\
This scroll will then be available to view. \
<img src="https://github.com/user-attachments/assets/80fbe2b1-c667-4306-b95f-ea8da8a0843c" width="50%">

#### Updating Scrolls
Users can only edit the title of a scroll and the scroll itself.\
Once the update is confirmed by the user, the details will be updated. \
<img src="https://github.com/user-attachments/assets/80fbe2b1-c667-4306-b95f-ea8da8a0843c" width="50%">

#### Deleting Scrolls
Users are able to deelte scrolls they have uploaded.\
Before deleting, a confirmation screen will be displayed ensuring users delete the correct scroll. \
<img src="https://github.com/user-attachments/assets/966b2fa1-8ba3-48ac-9e83-93edbf575587" width="50%">

#### Editing User
Users are able to update all of their information, including their password.
<img src="https://github.com/user-attachments/assets/c271cb33-1427-489f-9b22-b06dd3800e96" width="50%">

### As an Admin
#### Login
When the app is first open, the admin is prompted to log in. If the admin enters the wrong username or password, both fields will refresh with a warning message.\
Once the admin enters the correct information, they are taken to the home page of the app. \
<img src="https://github.com/user-attachments/assets/ae22f8f8-b0de-4ea3-9dcc-7af4e94c8951" width="50%">

#### View Scrolls
Admins can view all of the scrolls uploaded to the VSAS.\
This is shown on the homepage.\
<img src="https://github.com/user-attachments/assets/4981aaa9-d068-43f9-a87d-b2753e073ebd" width="50%">

#### Filter Scrolls
If admins wish to find a specific scroll, they are able to filter by Scroll ID, Uploader Username, Scroll Title and/or Upload Date.\
After pressing the Filter button, the scrolls with the specified attributes will be displayed.\
<img src="https://github.com/user-attachments/assets/804332ca-a580-403c-a6e8-f75fdabc506e" width="50%">

#### Preview and Download Scrolls
Admins can preview a scroll, which ashows al of the scroll details as well as the contents of the scroll itself.\
Admins can download a scroll after previewing it. The scroll will be sent to their downloads folder.\
<img src="https://github.com/user-attachments/assets/95ba12c9-08da-4027-9a1d-5dc94fee29d6" width="50%">

#### Line by Line Filtering
Admins can go through the scroll line by line. This can be opened from the preview screen.\
Admins can enter a line number and view the line previous or after it. \
<img src="https://github.com/user-attachments/assets/f1bd6143-9429-4baa-9be3-43403be0ae64" width="50%">

#### Scroll Statistics
Admins are able to view the downloads, uploads and amount of views each scroll has. \
<img src="https://github.com/user-attachments/assets/733c8d04-d971-4ad2-83c9-199bd654299e" width="50%">

#### Add User
Admins are able to add a user to the VSAS but entering the new user's details.\
<img src="https://github.com/user-attachments/assets/bd9a15eb-f158-40ba-a92e-0c5e273a7fb6" width="50%">\
<img src="https://github.com/user-attachments/assets/3ca20f23-759e-4e16-b7df-55e23fcea56c" width="50%">\

#### Delete User
Admins are abe to delete users, a confirmation message will appear ensuring the admin deletes the correct user.
<img src="https://github.com/user-attachments/assets/d67f1d82-ad53-428e-bec5-b4ad1217b335" width="50%">

### As a Guest
#### Login
When the app is first open, the user is prompted to log in. If the user does not have an account, they can continue as a guest.\
This means that they are only able to view and search for scrolls, they cannot preview or download scrolls.

#### View Scrolls
Guest users can view all of the scrolls uploaded to the VSAS.\
This is shown on the homepage. \
<img src="https://github.com/user-attachments/assets/cc49c09b-22d4-4049-bb05-7a725a522bec" width="50%">

#### Filter Scrolls
If guest users wish to find a specific scroll, they are able to filter by Scroll ID, Uploader Username, Scroll Title and/or Upload Date.\
After pressing the Filter button, the scrolls with the specified attributes will be displayed.\
<img src="https://github.com/user-attachments/assets/367c863c-568b-4451-9cae-022d1ba0194a" width="50%">


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
