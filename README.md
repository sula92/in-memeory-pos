#### ABOUT THE PROJECT:

Java Application (In Memory) that has been developed in order to manage customers, orders, to allow its customers to order items..

#### TECHNOLOGY STACK:

- [x] Java SE8
- [x] Java FX

#### PREREQUISITES:

You need to install- 

- [x] JavaSE8 

Fisrt of all you need to setup the java enviroment on your pc. Below are the guidence for setup the java enviroment in your pc.

What is JAVA_HOME? By convention, JAVA_HOME is the name of an environment variable on the operating system that points to the installation directory of JDK (Java Development Kit) or JRE (Java Runtime Environment) – thus the name Java Home. For example: 1 JAVA_HOME = c:\Program Files\Java\jdk1.8.0_201

Why is JAVA_HOME needed? To develop Java applications, you need to update the PATH environment variable of the operating system so development tools like Eclipse, NetBeans, Tomcat… can be executed because these programs need JDK/JRE to function. So the PATH environment variable should include JAVA_HOME:

PATH = Other Paths + JAVA_HOME Other paths are set by various programs installed in the operating system. If the PATH environment variable doesn’t contain a path to JRE/JDK, a Java-based program might not be able to run. For example, typing java in the command prompt showing this error: 1 'java' is not recognized as an internal or external command, operable program or batch file. error java command.

How to set JAVA_HOME on Windows 10 Here are the visual steps to properly set value for the JAVA_HOME and update the PATH environment variables in order to setup Java development environment on your computer:

Firstly, you need to identify the Java home directory, which is typically under C:\Program Files\Java directory.

Open the System Environment Variables dialog by typing environment in the search area on Start menu. Click the suggested item Edit the system environment variables: The System Properties dialog appears, click the button Environment Variables.

3.Create the JAVA_HOME environment variable by clicking the New button at the bottom. In the New System Variable form, enter the name and value.

Click OK, and you will see the JAVA_HOME variable is added to the list.

4.Update the PATH system variable. In the Environment Variables dialog, select the Path variable and click Edit:

Then in the Edit environment variable dialog, double click on the empty row just below the last text line, and enter %JAVA_HOME%\bin.

The percent signs tell Windows that it refers to a variable – JAVA_HOME, and the \bin specifies the location of java.exe and javac.exe programs which are used to run and compile Java programs, as well as other tools in the JDK. Click OK button to close all the dialogs, and you’re all set. Now you can open Eclipse or NetBeans to verify. Or open a command prompt and type in javac –version.

So now if you can see the correct java version that you installed.


Runnig the Project:

Please Follow below steps to run the project.

*In order to run the project you must have an ide like intellij, eclipse or netbeans in your pc. You can download eclipse & netbeans for free and you can buy the intellij enterprise edition from their official site.

*Now Implort the project to the ide.

*Now before you run the project make sure to start the server.

*Run the Project using ide.


#### SUPPORT:

Sulakkhana Dissanayake - sulkkanaid@gmail.com Project Link: https:https://github.com/sula92/in-memeory-pos


#### LICENSE:

Distributed under the MIT License. See LICENSE for more information.
 
