Used Java Version = 1.8

BEFORE EXECUTING TEST CASE :

After extract zip file to desired path on your computer, in order to execute test successfully, you must change a 
your system path information in UIControllerTests.java Test class.
In this class , search for this line below
String yourSystemPath = "add_your_file_path_to_project";  //for ex: "C:\\Users\\MSI-PC\\IdeaProjects\\";
and change "add_your_file_path_to_project" to your path where you extract this zip file.
If you want to skip the test just run mvn clean install -DskipTests


- To parse html, JSoup is used. There are other open source libraries, but i found very simple to use this.
- To validate html, i use Nu Validator. Nu Validator is very good open source library in order to validate html,html5 syntax
- I named servlet classes as UIController,HomeController etc, i based project design in MVC design pattern. In this case, Controller classes are 
responsible to receive request and process them with necessary validations.
- I use TemplateManager to handle template processing logic.
- For testing , i choose Mockito and JUnit.I implemented a test case for UIController. I could add more test cases for Manager Class or and
also for other classes.

DISCARDED ALTERNATIVES
 In order to process template expressions (data-if="car.ecoFriendly") , i choosed Groovy, it is very simple to use to get value of 
 object in String ("car.ecoFriendly")
 Reflection can be used here as an altervative approach.


