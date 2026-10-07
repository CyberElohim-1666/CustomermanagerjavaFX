# Customer Manager

A small desktop application for the ICT261 Customer Manager classroom lab. It stores customer records in memory while the application is open.

## Features

- Enter a customer name and select a province.
- Save valid customers in a table.
- Keep entered values when validation fails.
- Delete the selected customer after confirmation.
- Use **Tab** to move through controls and **Enter** to save a customer.
- Use the **File** menu to clear the form or close the application.

## Requirements

- Java Development Kit (JDK) 11 or later.
- This project was tested with Eclipse Temurin OpenJDK 25.

The application uses Java Swing, which is included with the JDK. No extra libraries or batch files are needed.

## Run the application

Open Command Prompt in this project folder and compile the source code:

```cmd
javac -d out src\main\java\CustomerManagerApp.java
```

Then run the compiled application:

```cmd
java -cp out CustomerManagerApp
```

The `out` folder receives the generated `.class` files. You can compile and run from any folder with:

```cmd
javac -d "%USERPROFILE%\Desktop\customerapp\out" "%USERPROFILE%\Desktop\customerapp\src\main\java\CustomerManagerApp.java"
java -cp "%USERPROFILE%\Desktop\customerapp\out" CustomerManagerApp
```

You can also run the source file directly without creating `.class` files:

```cmd
java src\main\java\CustomerManagerApp.java
```

## Run the smoke test

```cmd
javac -d out src\main\java\CustomerManagerApp.java
java -cp out CustomerManagerApp --smoke-test
```

## Project files

```text
customerapp/
├── src/
│   └── main/
│       └── java/
│           └── CustomerManagerApp.java
├── out/
│   └── .gitkeep
├── README.md
├── .gitignore
└── .gitattributes
```

## Push to GitHub

This folder already has a local Git repository. Run these commands from inside the `customerapp` folder to create a real commit and push it to the supplied GitHub repository.

```cmd
git add src/main/java/CustomerManagerApp.java out/.gitkeep README.md .gitignore .gitattributes
git commit -m "Add Customer Manager desktop application"
git branch -M main
git remote add origin https://github.com/CyberElohim-1666/CustomermanagerjavaFX.git
git push -u origin main
```

If you copy the files into another folder that does not include the hidden `.git` folder, run `git init` there before the commands above.

If Git asks for a name and email before committing, set them with:

```cmd
git config --global user.name "Your Name"
git config --global user.email "your-email@example.com"
```

## Notes

- The customer list is not saved to a database or file. Closing the application removes the current records.
- The `--smoke-test` option checks customer addition, table values, and deletion without opening the desktop window.
