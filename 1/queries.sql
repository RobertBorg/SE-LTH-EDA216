SELECT firstName,
       lastName
FROM Students;


SELECT firstName,
       lastName
FROM Students
ORDER BY lastName,
         firstName;


SELECT *
FROM Students
WHERE pNbr LIKE '75%';


SELECT firstName,
       lastName,
       pNbr
FROM Students
WHERE mod(substr(pNbr,10,1),2) = 0;


SELECT count(pNbr)
FROM Students;


SELECT *
FROM Courses
WHERE courseCode LIKE 'FMA%';


SELECT *
FROM Courses
WHERE credits > 5;


SELECT courseCode
FROM TakenCourses
WHERE pNbr = '790101-1234';


SELECT courseName,
       credits
FROM Courses,
     TakenCourses
WHERE TakenCourses.courseCode = Courses.courseCode
    AND TakenCourses.pNbr = '790101-1234';


SELECT SUM(credits)
FROM Courses,
     TakenCourses
WHERE TakenCourses.courseCode = Courses.courseCode
    AND TakenCourses.pNbr = '790101-1234';


SELECT SUM(TakenCourses.grade)/SUM(Courses.credits)
FROM Courses,
     TakenCourses
WHERE TakenCourses.courseCode = Courses.courseCode
    AND TakenCourses.pNbr = '790101-1234';


SELECT courseCode
FROM TakenCourses,
     Students
WHERE TakenCourses.pNbr = Students.pNbr
    AND firstName = 'Eva'
    AND lastName = 'Alm';


SELECT courseName,
       credits
FROM Courses,
     TakenCourses,
     Students
WHERE TakenCourses.courseCode = Courses.courseCode
    AND TakenCourses.pNbr = Students.pNbr
    AND firstName = 'Eva'
    AND lastName = 'Alm';


SELECT SUM(credits)
FROM Courses,
     TakenCourses,
     Students
WHERE TakenCourses.courseCode = Courses.courseCode
    AND TakenCourses.pNbr = Students.pNbr
    AND firstName = 'Eva'
    AND lastName = 'Alm';


SELECT SUM(TakenCourses.grade)/SUM(Courses.credits)
FROM Courses,
     TakenCourses,
     Students
WHERE TakenCourses.courseCode = Courses.courseCode
    AND TakenCourses.pNbr = Students.pNbr
    AND firstName = 'Eva'
    AND lastName = 'Alm';


SELECT Students.*
FROM Students
WHERE pnbr NOT IN
        (SELECT pnbr
         FROM takencourses);


CREATE VIEW StudentGradeAvg AS
SELECT students.pNbr,
       avg(TakenCourses.grade)
FROM Students,
     takencourses
WHERE students.pnbr = takencourses.pnbr
GROUP BY pnbr;


    (SELECT students.pNbr,
            coalesce(SUM(credits),0)
     FROM students,
          takencourses,
          courses
     WHERE takencourses.pnbr = students.pnbr
         AND takencourses.coursecode = courses.courseCode
     GROUP BY students.pnbr)
UNION
    (SELECT pnbr,
            0
     FROM students
     WHERE pnbr NOT IN
             (SELECT pnbr
              FROM takencourses));


    (SELECT firstname,
            lastname,
            coalesce(SUM(credits),0)
     FROM students,
          takencourses,
          courses
     WHERE takencourses.pnbr = students.pnbr
         AND takencourses.coursecode = courses.courseCode
     GROUP BY students.pnbr)
UNION
    (SELECT firstname,
            lastname,
            0
     FROM students
     WHERE pnbr NOT IN
             (SELECT pnbr
              FROM takencourses));


SELECT DISTINCT Old.firstname,
                Old.lastname,
                Old.pnbr
FROM students OLD, students NEW
WHERE OLD.firstname = NEW.firstname
    AND OLD.lastname = NEW.lastname
    AND OLD.pnbr <> NEW.pnbr;

