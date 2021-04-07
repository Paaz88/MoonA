This program gets a file, containing a path to images, and checks if every one of them is allowed to park.
Assumption: Only a valid license plate gets the decision, others won't and also won't be pushed into the db (wrong parsing, wrong image, not a valid number etc).
Note: While the given license plates are IL, the instruction related to non-IL license plates.

The input file was named: plated.txt, its path in a local variable named: PLATE_FILE_PATH, located in the MainClass.

The user should point to the DB SQLITE path: in DBUtils, by editing the variable name: URL.
In order to easily retrieve data from the db, there are 2 tables: TimeTable & DecisionTable:
* TimeTable - Every valid plate will be written into it, with a timestamp. So evey vehicle that reaches will be written.
* DecisionTable - Only a valid plate that wasn't inserted before, will be inserted. No need to check parking ability twice, no need to duplicate rows.

The project supposed to be built with maven, with the attached pom.xml file.

Program:
The program connects SQLITE, creates tables if not exist, reads from a file all the urls and loop on all image urls:
1. Creates a vehicle obj with all the relevant details about the car. Later they will be updated.
2. Sends a post request with the image, and receives the response.
3. Checks the HTTP code and validate it. If fail, prints the error and continue to the next vehicle.
4. Gets the necessary data from the JSON object. Extracts the number:
    a. It should be 6-8 characters.
    b. It can contain alphabet chars.
    c. It should have at least 1 number.
    d. It should be the with the largest font in the image.
5. If the plate was already checked, no need to check it again, inert the data only to the "Time Table".
5. Parking ability is checked, according to the instructions.
6. Every *valid* vehicle data is pushed to the db. Others should be "scanned again" or resent by the user.


Classes:
DBUtils - handles with all the db connections including: creates a table, insert request etc.
FileReaderPlates - responsible for reading data from the attached file: plates.txt
LicensePlatePostRequest - responsible for sending HTTP Post request to the API
MainClass - the main program
StringUtils - contains method that deal with Strings.
VehicleData - object that summarize all the relevant data about a vehicle

** Every method in the program contains a detailed javadoc explaining the method's functionality. There are also in-methods comments.
