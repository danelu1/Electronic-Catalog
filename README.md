# Electronic-Catalog
The application contains the following classes: 

The "Catalog" class (the main class  of the entire application) that contains a list of "Course" objects, a list of  "Observer" objects, a list of objects of type "Grade" and the list of objects of type "User".  The methods of this class are: 
  -void addCourse(Course), the method that adds a course received as  a parameter in the catalog (the list of courses we have at hand). When we add a course, we also add the grades received by students to this course, so we  must send notifications using the notifyObservers (Grade)  method for each grade  added. 

  -void removeCourse(Course), the method that removes a course from the catalog (from the list of courses).

  -void testParse(String), the method created specifically for parsing the test file , because  my test files differ. 

  -ArrayList<ArrayList<Grade>> addGradesToCatalog(), a method that adds all  validated notes to the catalog and returns a list of  lists of notes, this containing the students' grades for each  course. Within the method, the  methods from the  "ScoreVisitor" class are also used, in terms of  the combine method (Teacher, Assistant), which we will explain below. 

  -String addStudent(String, String, Course, String), the method by  which they retrieve the login data  of a  student (name, surname, user_name, user_password and user's picture) and the data of the user  its personal from 2 files of type ".json", data that I enter in  the course at which the student must be entered, in the group with the id  received as a parameter. Adding the student is done by parsing the 2 files (the login, respectively the normal one) and parsing the file "courses.json" that contains information about all courses. The student is added to the desired course and group, after which a "String" containing the updated information of the respective course is returned. 

  -String addAssistant(String, Course), a method similar to the previous method, which adds an assistant in the course received as a parameter and returns the updated information in  the form of a String. 

  -String addGroup(String, Course), a method similar to the previous ones, which adds a group in the course received as a parameter and returns the updated information in  the form of a String. 

  -String addGrade(String, Course), the method similar to the previous ones, responsible for adding a new  note  to the course received as a parameter, the note being added at the same time  in the file " grades.json", a file in which all  students' grades are stored. 

  -void gradesParseJSON(String), the method that parses a file received as a parameter by name and adds to the list  of Grade elements (within the  class) each note in  that file. 

  -void coursesParseJSON(String),  the method that parses a file received as a parameter by name (in  our case the file "courses.json") and populates each member of the "Course"  class to  he could form a new   course  to be added to the list  of "Course"  type objects (within the class). 

  -void loginParseJSON(String), method that receives as a parameter the type of each person who wants to enter the  personal account of the catalog and parses each of the files associated with the chosen type. 

  -void addObserver (Observer observer), the method that adds an  object of type "Observer" in the list of  related objects (within the class). 

  -void removeObserver (Observer observer), method that  deletes an "Observer" from  the list mentioned in the description of the previous  method. 

  -void notifyObservers(Grade), method that sends notifications (object type Notification) when adding a new note in the catalog. 

  -User checkUserNamePassword(String, String, String), method responsible for checking the username and password of  each "User" subscribed to the catalog.  This method uses the "loginParseJSON(String)"  function described above, to determine the  type of  user for whom the verification is made. 

  -void open(), the method responsible for opening the catalog (graphical interface for it). 

  -the toString()  method only displays the name of each course in the catalog. 

Abstract class "Course", which contains the necessary attributes  for each course: 

    ->Course name 

    ->The titular teacher  of the respective course  

    ->A collection without duplicates with  the course assistants 

    ->An orderly collection of objects with the grades assigned to the students of this course 

    ->A dictionary in which the IDs of the groups are stored, as  keys, and as values the groups associated with the respective IDs. 

    ->Number of credits of the respective exchange rate. 

    ->Type of course 

    ->The strategy used in the course to determine the best student 

    ->Type of strategy used 

The class has implemented the following internal methods and classes : 

 "getter" and "setter" methods for using the concept of data encapsulation. 

The  static and abstract internal  class  "CourseBuilder" with  which   we will instantiate a  course object, using the design template  "Builder", but implementing the "setter"  methods corresponding to each parameter.  

void addAssistant(String, Assistant), the method that adds an assistant to   the group with the ID   received as parameter, first checking  if another assistant has been assigned to the group respectively. After  adding the assistant, it is added   to the collection of assistants. 

void addStudent(String, Student), adds a student to the group with the ID  received as a parameter, only  if the student has not  already been assigned to that group. 

void addGroup(Group), the method that adds a group to that course . 

The other 2  "addGroup" methods call the previous method. 

Degrees getGrade(Student),  the method that returns the grade of a student received as  a parameter, by calling the getAllStudentGrades()  method, within the same class. 

void addGrade(Grade), the method that adds a grade to the respective course, provided that there are no 2 students with the same grade. 

ArrayList<Student> getAllStudents(), the method that returns all students assigned to the course, iterating both through the dictionary with groups and through each group . 

LinkedHashMap<Student, Grade> getAllStudentGrades(), the method that turns the situation of  each student of the course, iterating through the collection  of "Grade" type objects. 

  Student getBestStudent(), the method that returns the best student of the course according to  the strategy chosen by the teacher. 

The private internal  class  "Snapshot", in which I keep a reference to an ArrayList<Grade> with  which we will perform the   operations specific to the design template "Memento". 

void makeBackup(), method that adds a clone of each "Grade" in the catalog (at that time) in the collection inside the internal class "Snapshot". 

void undo(), method that restores the collection of notes from the catalog to the backup  made, that's if I have been added and other notes meanwhile. 

The "FullCourse" and "PartialCourse" classes are responsible for the effective instantiation of  a "Course"  type object, which inherits the abstract class "Course".   Instantiation is done with the design template  "Builder", with  the help of the internal static classes "FullCourseBuilder" and "PartialCourseBuilder", classes that inherit the abstract internal class "CourseBuilder" within the of the "Course" class.  The only method implemented in these classes is the abstract method from the  "Course" class, that is,  the getGraduatedStudents()  method, which returns a collection of  "Student" type objects, which contains all students  who have switched to  that course depending on the type of course. 

 The abstract class "User" describes a user of the catalog, that is, a student, teacher, assistant or parent. 

The "Student"         class   describes a student-type user, described by his name, surname, username, password and the picture he has in his account.  objects of type "Notification" in which are stored notifications with each grade received by the respective student  at each course separately. 

   The "Assistant" and "Teacher"  classes describe the assistant assigned to one or more  courses (the same applies to the  teacher).  The new  method within these classes is the "accept(Visitor)"  method that calls the "visit"  methods within the "ScoreVisitor" class. 

The "Parent" class is similar to the previous classes, but does  not have any "special" functionality.  

The "UserFactory"  class is responsible for instantiating a  "User" object using the static  method contained in it: getUser (String, String, String). 

 The "Degrees"  class describes the grade received by a student, through  the partial and  final  scores obtained in a certain course.  The class  provides a method of comparing two marks based on  the total score, respectively the name of the student in case of a tie. Another method would be the one that calculates the total  score obtained by the student during  the semester and the  clone  method used in the private  class "Snapshot", described above. 

The "Group"  class describes the  group in which  certain students of  the course are listed.  The class inherits "TreeSet<Student>" in order to  sort the students of the group alphabetically by name. The group contains an "ID" and an assistant.  

The "Notification"  class describes a notifcare that a parent must  receive when his child receives a grade in a certain course.   The class contains a reference to an object of type "Degrees" and references corresponding to the student's parents.  The "update(Notification)"  method adds to the list of notifications of each student the corresponding notification  received. 

In the classes "BestPartialScore", "BestExamScore", "BestTotalScore" we determine the maximum of the "Grade"  collection of  the getBestScore method (ArrayList<Grade>) within the "Strategy" interface.  The maximum is determined for each class according to the  chosen strategy (maximum partial score,  maximum exam score,  maximum total score). 

The "ScoreVisitor" class (which was the  biggest difficulty for me at least) contains 2 dictionaries for the students' grades along   the way, respectively those from the exam.  Class methods: 

void visit(Assistant), the method that reads the notes in a file and processes the dictionary "partialScores", by adding information to it. 

void visit (Teacher), the method that reads the notes in a file and processes the dictionary "examScores", by adding information to it. 

the methods "printGradesAssistant(Assistant)" and "printGradesTeacher(Teacher)" are methods used by the graphical  interface  in  order to display on the "Assistant/Teacher"  pages  the notes to be validated. 

ArrayList<Grade> combine (Teacher, Assistant), the method that returns the  final validated  grades of students by  a teacher and an assistant. In short, the  final notes of a group. 

 The internal class "Tuple" describes a tuplet of the form "Student, String, Double" 

The "TestVisitor"  class is the  class with which I process correctly (because in "ScoreVisitor" it is not the best as they do) the notes from the 2 dictionaries with  the help of the following methods: 

void parseAssistant(String),  the method that parses the offered test  file, extracting from it all the information regarding the "partialScores"  section and adds to the dictionary for  scores  partial all the necessary data. 

void parseTeacher(String),  just like  the  previous method, just like for the dictionary "examScores". 

void visit(Teacher),  the method by which the notes in the dictionary "examScores" are passed to each student  corresponding to the course and to the teacher. 

void visit(Assistant), the method similar to the one  above, only this time  the grades are added to  the students who correspond to the respective assistants.  

 The internal class "Tuple" has the same functionality as the one in "ScoreVisitor" 

The following classes describe the functionality of the graphical interface (made using the Swing  component): 

 The main class  "SelectionPage", where the  user  type  is  selected. I used a "ButtonGroup" to store the components of type "JCheckBox".     The selection button is  inaccessible until  a checkbox is selected. By selecting you can be redirected to one of the login pages, i.e. "StudentLoginPage", "AssistantLoginPage", "TeacherLoginPage" or "ParentLoginPage". On these pages it will be  checked if the  data entered corresponds to  the data in the logging file  corresponding to the user type. In case of negative, the   title label will be  modified with an  appropriate message, and if  so it will be  passed on one of the corresponding  pages 

The "StudentMainPage" page, where you will find the requested information about the student and a picture of the student, or with him. 

The "TeacherMainPage" page, in which, in addition to information and image, the   notes that the teacher will have  to  validate will  also be displayed. 

The "AssistantMainPage" page, similar to the previous  page. 

The "ParentMainPage" page, in which will be displayed the notifications received by the parents of the student to whom grades are granted. 

The "GeneralInformationsPage"  page is the  page that contains information related to all courses, except for grades. 

The "ModifiableInformationsPage"  page is the  page on which changes are made  both to the  page when  pressing some buttons, and to  the registration file  for students, the one that contains the courses  and the one that  contains the  notes of each one. 
