-- Insert Statements for mock data

INSERT INTO People
VALUES (1, "Greg", "Sawers", 1, 1435429353);

INSERT INTO People
VALUES (2, "Tobias", "Bleisch", 1, 1262629353);

INSERT INTO People
VALUES (3, "Julio", "Salvador", 1, 1003552566);

INSERT INTO People
VALUES (4, "Tony", "Stark", 1, 1435729353);



SELECT P1.ID AS IDA, P2.ID AS IDB, L1.ID AS LIDA, L1.Latitude AS LatA,
                L1.Longitude AS LongA, L2.Latitude AS LatB, L2.Longitude AS LongB
                L1.TimeAndDate AS TimeA, L2.TimeAndDate AS TimeB
                FROM People P1, People P2, Locations L1, Locations L2
                WHERE (LongA - LongB < .00002 AND LongA - LongB > .00002)
                AND (LatA - LatB < .00002 AND LatA - LatB > .00002)
                AND L1.Person <> L2.Person
                AND TimeA-TimeB BETWEEN 300000 AND -300000
                AND L1.Person = P1.ID
                AND L2.Person = P2.ID;