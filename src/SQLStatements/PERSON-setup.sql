-- Use to set up the tables
-- Need to generate insert statements

CREATE TABLE People(
	ID INT,
	FirstName VARCHAR(30),
	LastName VARCHAR(30),
	Infected INT,
	TimeReported INT,
	PRIMARY KEY(ID) 
);

CREATE TABLE Locations(
	ID INT,
	Longitude DOUBLE (9, 6),
	Latitude DOUBLE(9, 6),
	TimeAndDate INT,
	Person INT,
	PRIMARY KEY(ID),
	FOREIGN KEY(Person) REFERENCES People(ID)
);

CREATE TABLE Interactions(
	ID INT,
	Person1 INT,
	Person2 INT,
	Location INT,
	Radius FLOAT,
	Rank FLOAT,
	FOREIGN KEY(Person1) REFERENCES People(ID),
	FOREIGN KEY(Person2) REFERENCES People(ID),
	FOREIGN KEY(Location) REFERENCES Locations(ID)
);