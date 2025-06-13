# Task Tracker CLI (Java)

A simple command-line based Task Tracker built in Java, with task storage in a local JSON file.  
No external libraries are used — everything is implemented manually using core Java features.

## ✨ Features

- Add a new task
- Update task description
- Delete a task
- Mark a task as "in progress"
- Mark a task as "done"
- List all tasks
- List tasks filtered by status (`todo`, `in progress`, `done`)
- All tasks are saved in a JSON file
- Manual JSON parsing without external libraries

## 📁 Project Structure

- `Task.java`: Model class for representing a task (id, description, status, timestamps)
- `TaskManager.java`: Core logic for managing tasks (reading/writing JSON, performing operations)
- `tasks.json`: Local file where all tasks are stored
- `Main.java`: (optional) Console interface to interact with the task manager

## 📦 Requirements

- Java 8 or later
- No external libraries needed

## 🛠 How to Run

1. Clone the repository or copy the files.
2. Compile the Java files:

   ```bash
   javac TaskManager.java Task.java Main.java


If you don't have Main.java, you can directly call methods from TaskManager in your own main method or from tests.


## 📝 Example

```TaskManager tm = new TaskManager("tasks.json");

tm.addTask("Finish writing report");
tm.addTask("Clean the room");

tm.updateTask(1, "Finish writing final report");

tm.markInProgress(2);
tm.markDone(1);

tm.listTasks();
tm.listTasksByStatus("done");
```

## 📂 JSON Format

Tasks are stored in tasks.json like this:

```
[
  {
    "id": 1,
    "description": "Finish writing final report",
    "status": "done",
    "createdAt": "2025-06-08T19:00:00",
    "updatedAt": "2025-06-08T20:00:00"
  }
]
```

## 📃 License

This project is open-source and free to use for learning and educational purposes.

## 🔗 Project URL

[GitHub Repository][(https://github.com/Hrant-Ispiryan/TaskTrackerCLI.git))]
