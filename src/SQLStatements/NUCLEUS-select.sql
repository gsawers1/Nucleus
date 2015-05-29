-- Nucleus select statements

-- Get all the people
SELECT *
FROM Person;

-- Select statement that should represent an interaction
-- May need to tweak longitude and latitude 
SELECT P1.ID, P2.ID, L1.ID, L2.ID
FROM People P1, People P2, Locations L1, Locations L2
WHERE (L1.Longitude - L2.Longitude < 2 AND L1.Longitude - L2.Longitude > -2)
AND (L1.Latitude - L2.Latitude < 2 AND L1.Latitude - L2.Latitude > -2)
AND L1.Person <> L2.Person
AND DATEDIFF(L1.Time, L2.Time) = 0;

-- 