#lejos-joystick-controller

Remote control your leJOS robot/vehicle/... via a gamepad using a simple event listener API.

See `JoystickController.java` for an example how to use the API. 

#Instructions on how to get started (eclipse and leJOS plugin)
It is assumed that you have set up your leJOS environment (see http://www.lejos.org for details).

1. Clone/Download this project.
1. Download jinput-platform-2.0.5-natives-<your platform>.jar, rename file to .zip and unzip contents into the
root folder of the project.
1. Download jinput-2.0.5.jar from http://repo1.maven.org/maven2/net/java/jinput/jinput/2.0.5/ and put it into a subfolder lib/ (you can also find source code and javadoc there - might help during further development).
1. Download http://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-api/2.5/log4j-api-2.5.jar and http://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-core/2.5/log4j-core-2.5.jar and put it to the lib folder aswell.
1. Choose File -> Import in Eclipse, "Import Existing Project".

Now the project should build and you should be able to try out the gamepad (adapt `JoystickController.java` to
your needs - e.g. use WLAN IP adress if you connect to your brick via WLAN).
