BEGIN TRANSACTION;
CREATE TABLE "Users" (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`FirstName`	TEXT NOT NULL,
	`LastName`	TEXT NOT NULL,
	`Nickname`	TEXT NOT NULL UNIQUE,
	`Email`	TEXT,
	`Answers`	TEXT
);
CREATE TABLE "Universities" (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Name`	TEXT NOT NULL UNIQUE,
	`Courses`	TEXT
);
CREATE TABLE "Questionnaires" (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Environment`	INTEGER NOT NULL UNIQUE,
	`Questions`	TEXT NOT NULL,
	`Answers`	TEXT NOT NULL,
	FOREIGN KEY(`Environment`) REFERENCES `Environments`(`ID`) ON DELETE CASCADE
);
CREATE TABLE `MatchCache` (
	`UserID`	INTEGER NOT NULL,
	`MatchedTo`	INTEGER NOT NULL,
	`Percentage`	INTEGER NOT NULL DEFAULT 0 CHECK(Percentage >= 0 and Percentage <= 100),
	PRIMARY KEY(`UserID`,`MatchedTo`),
	FOREIGN KEY(`UserID`) REFERENCES Users(ID) ON DELETE CASCADE,
	FOREIGN KEY(`MatchedTo`) REFERENCES Users(ID) ON DELETE CASCADE
);
CREATE TABLE "Groups" (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Finalized`	INTEGER NOT NULL DEFAULT 0 CHECK(Finalized = 0 or Finalized = 1),
	`TA`	INTEGER,
	`Students`	TEXT NOT NULL,
	`EnvironmentID`	INTEGER NOT NULL,
	FOREIGN KEY(`TA`) REFERENCES Users(ID) ON DELETE RESTRICT,
	FOREIGN KEY(`EnvironmentID`) REFERENCES `Environments`(`ID`) ON DELETE CASCADE
);
CREATE TABLE "Environments" (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Course`	INTEGER NOT NULL,
	`Students`	TEXT,
	`Groups`	TEXT,
	`CloseDate`	TEXT NOT NULL,
	`MaxGroupSize`	INTEGER NOT NULL DEFAULT 2 CHECK(MaxGroupSize > 0),
	`Questionnaire`	INTEGER DEFAULT -1,
	`Password`	TEXT,
	`Private`	INTEGER NOT NULL DEFAULT 0 CHECK(Private = 0 or Private = 1),
	`Name`	TEXT NOT NULL,
	`Owner`	INTEGER NOT NULL,
	FOREIGN KEY(`Course`) REFERENCES `Courses`(`ID`) ON DELETE RESTRICT,
	FOREIGN KEY(`Questionnaire`) REFERENCES `Questionnaire`(`ID`),
	FOREIGN KEY(`Owner`) REFERENCES `Users`(`ID`) ON DELETE CASCADE
);
CREATE TABLE "Courses" (
	`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Name`	TEXT NOT NULL,
	`Instructor`	INTEGER NOT NULL,
	`University`	INTEGER NOT NULL,
	`Environments`	TEXT,
	FOREIGN KEY(`Instructor`) REFERENCES `Users`(`ID`) ON DELETE RESTRICT,
	FOREIGN KEY(`University`) REFERENCES `Universities`(`ID`) ON DELETE RESTRICT
);
CREATE TABLE "Answers" (
	`Questionnaire`	INTEGER NOT NULL,
	`Student`	INTEGER NOT NULL,
	`Questions`	TEXT NOT NULL,
	`Answers`	TEXT NOT NULL,
	PRIMARY KEY(`Questionnaire`,`Student`),
	FOREIGN KEY(`Questionnaire`) REFERENCES Questionnaires(ID) ON DELETE CASCADE,
	FOREIGN KEY(`Student`) REFERENCES `Users`(`ID`) on delete cascade
);
COMMIT;
