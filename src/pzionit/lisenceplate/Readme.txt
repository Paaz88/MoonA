This program get a file, containing a path to images, and checks if it is allowed to park.
Assumption: Only a valid license plate gets the decision, others won't and also won't be pushed into the db (wrong parsing, wrong image, not a valid number etc).

The input file was named: plated.txt, its path in a local variable named: PLATE_FILE_PATH, located in the MainClass.
The user should point to the DB SQLITE path: in DBUtils, by editing the variable name: URL

The project supposed to be built with maven, with the attached pom.xml file.

Program:
The program connects SQLITE, creates a table if not exists, read from a file all the urls and loop on all image urls:
1. Creates a vehicle obj with all the relevant details about the car.
2. Send a post request with the image, and receive the response.
3. Check the HTTP code and validate it. if fail, print the error and continue to the next vehicle.
4. Get the necessary data from the JSON object. extract the number:
    a. It should be 6-8 characters
    b. It can contain alphabet chars.
    c. It should have at least 1 number.
    d. It should be the with the largest font in the image.
5. Parking ability is checked, according to the instructions.
6. Every *valid* vehicle data is pushed to the db. Others should be "scanned again" or resent.


Classes:
DBUtils - handles with all the db connections including: creates a table, and insert request.
FileReaderPlates - responsible for reading data from the attached file: plates.txt
LicensePlatePostRequest - responsible for sending HTTP Post request to the API
MainClass - the main program
StringUtils - contains method that deal with Strings.
VehicleData - object that summarize all the relevant data about a vehicle

** Every method in the program contains a detailed javadoc explaining the method's functionality. There are also in-methods comments.
