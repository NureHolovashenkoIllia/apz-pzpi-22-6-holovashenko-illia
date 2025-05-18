CREATE TABLE Users
(
    User_ID       INT AUTO_INCREMENT PRIMARY KEY,
    Name          VARCHAR(50)  NOT NULL,
    Email         VARCHAR(50)  NOT NULL UNIQUE,
    Password      VARCHAR(100) NOT NULL,
    Date_of_Birth DATE,
    Profile_Image Blob,
    User_Type     VARCHAR(10)  NOT NULL DEFAULT 'regular'
);

CREATE TABLE Premium_Feature
(
    Premium_Feature_ID INT PRIMARY KEY AUTO_INCREMENT,
    Name               VARCHAR(50) NOT NULL
);

CREATE TABLE User_Premium_Feature
(
    User_ID            INT,
    Premium_Feature_ID INT,
    PRIMARY KEY (User_ID, Premium_Feature_ID),
    FOREIGN KEY (User_ID) REFERENCES Users (User_ID),
    FOREIGN KEY (Premium_Feature_ID) REFERENCES Premium_Feature (Premium_Feature_ID)
);

CREATE TABLE Payment
(
    Payment_ID     INT PRIMARY KEY AUTO_INCREMENT,
    User_ID        INT            NOT NULL,
    Payment_Method VARCHAR(50)    NOT NULL,
    Payment_Date   DATE           NOT NULL,
    Amount         DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (User_ID) REFERENCES Users (User_ID)
);

CREATE TABLE Subscription_Plan
(
    Subscription_Plan_ID INT PRIMARY KEY AUTO_INCREMENT,
    Name                 VARCHAR(50)    NOT NULL,
    Price                DECIMAL(10, 2) NOT NULL,
    Description          VARCHAR(500)   NOT NULL
);

CREATE TABLE User_Subscription_Plan
(
    User_ID              INT,
    Subscription_Plan_ID INT,
    Start_Date           DATE NOT NULL,
    End_Date             DATE NOT NULL,
    PRIMARY KEY (User_ID, Subscription_Plan_ID),
    FOREIGN KEY (User_ID) REFERENCES Users (User_ID),
    FOREIGN KEY (Subscription_Plan_ID) REFERENCES Subscription_Plan (Subscription_Plan_ID)
);

CREATE TABLE Artists
(
    Artist_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name      VARCHAR(50) NOT NULL,
    Genre     VARCHAR(50),
    Image_URL VARCHAR(255)
);

CREATE TABLE Albums
(
    Album_ID     INT AUTO_INCREMENT PRIMARY KEY,
    Artist_ID    INT,
    Name         VARCHAR(50) NOT NULL,
    Release_Date DATE,
    Image        VARCHAR(255),
    FOREIGN KEY (Artist_ID) REFERENCES Artists (Artist_ID)
);

CREATE TABLE Tracks
(
    Track_ID INT AUTO_INCREMENT PRIMARY KEY,
    Album_ID INT,
    Name     VARCHAR(50) NOT NULL,
    Duration INT         NOT NULL,
    Path     VARCHAR(255),
    FOREIGN KEY (Album_ID) REFERENCES Albums (Album_ID)
);

CREATE TABLE Playlists
(
    Playlist_ID INT AUTO_INCREMENT PRIMARY KEY,
    User_ID     INT,
    Name        VARCHAR(50) NOT NULL,
    Image       Blob,
    FOREIGN KEY (User_ID) REFERENCES Users (User_ID)
);

CREATE TABLE Playlist_Tracks
(
    Playlist_ID INT,
    Track_ID    INT,
    `Order`     INT,
    PRIMARY KEY (Playlist_ID, Track_ID),
    FOREIGN KEY (Playlist_ID) REFERENCES Playlists (Playlist_ID),
    FOREIGN KEY (Track_ID) REFERENCES Tracks (Track_ID)
);

CREATE TABLE Followers
(
    User_ID   INT,
    Artist_ID INT,
    PRIMARY KEY (User_ID, Artist_ID),
    FOREIGN KEY (User_ID) REFERENCES Users (User_ID),
    FOREIGN KEY (Artist_ID) REFERENCES Artists (Artist_ID)
);

CREATE TABLE Likes
(
    User_ID        INT,
    Track_ID       INT,
    Like_Date_Time DATETIME,
    PRIMARY KEY (User_ID, Track_ID),
    FOREIGN KEY (User_ID) REFERENCES Users (User_ID),
    FOREIGN KEY (Track_ID) REFERENCES Tracks (Track_ID)
);