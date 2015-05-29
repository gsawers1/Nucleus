-- Use to set up the tables
-- Need to generate insert statements

CREATE TABLE Person(
	ID INT,
	FirstName VARCHAR(30),
	LastName VARCHAR(30),
	INT Infected,
	DATE LowerInfectionTime,
	DATE UpperInfectionTime,
	DATE TimeReported,
	PRIMARY KEY(No) 
);

CREATE TABLE Location(
	ID INT,
	Longitude INT,
	Latitude INT,
	Person INT,
	PRIMARY KEY(ID),
	FOREIGN KEY(Person) REFERENCES Person(ID)
);

CREATE TABLE Interaction(
	ID INT,
	Person1 INT,
	Person2 INT,
	Location INT,
	Radius FLOAT,
	Rank INT,
	FOREIGN KEY(Person1) REFERENCES Person(ID),
	FOREIGN KEY(Person2) REFERENCES Person(ID)
);