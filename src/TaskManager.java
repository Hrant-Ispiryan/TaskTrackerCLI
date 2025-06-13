import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TaskManager {
    private String filePath;

    public TaskManager(String filePath) {
        this.filePath = filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла: " + e.getMessage());
            }
        }
    }


    public void addTask(String description) {
        List<Task> tasks = readTasksFromFile();
        int newId = 1;
        for (Task t : tasks) {
            if (t.id >= newId) newId = t.id + 1;
        }

        Task newTask = new Task();
        newTask.id = newId;
        newTask.description = description;
        newTask.status = "todo";
        newTask.createdAt = getCurrentTime();
        newTask.updatedAt = getCurrentTime();

        tasks.add(newTask);
        writeTasksToFile(tasks);

        System.out.println("Task added successfully (ID: " + newId + ")");
    }

    public void updateTask(int existing_id, String newDescription) {
        List<Task> tasks = readTasksFromFile();
        boolean found = false;

        for(Task t : tasks){
            if(t.id == existing_id){
                t.description = newDescription;
                t.updatedAt = getCurrentTime();
                writeTasksToFile(tasks);
                System.out.println("Task with ID " + existing_id + " is updated.");
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("Task with ID " + existing_id + " doesn't exist.");
        }
    }

    public void deleteTask(int del_id) {
        List<Task> tasks = readTasksFromFile();
        boolean found = false;

        for (Task t : tasks){
            if (t.id == del_id){
                tasks.remove(t);
                writeTasksToFile(tasks);
                System.out.println("Task with ID " + del_id + " deleted successfully.");
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("Task with ID " + del_id + " doesn't exist.");
        }
    }

    public void markInProgress(int cur_id) {
        List<Task> tasks = readTasksFromFile();
        boolean found = false;
        for (Task t : tasks){
            if (t.id == cur_id){
                t.status = "in progress";
                writeTasksToFile(tasks);
                System.out.println("Task with ID " + cur_id + " marked in progress successfully.");
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("Task with ID " + cur_id + " doesn't exist.");
        }
    }

    public void markDone(int cur_id) {
        List<Task> tasks = readTasksFromFile();
        boolean found = false;
        for (Task t : tasks){
            if (t.id == cur_id){
                t.status = "done";
                writeTasksToFile(tasks);
                System.out.println("Task with ID " + cur_id + " marked done successfully.");
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("Task with ID " + cur_id + " doesn't exist.");
        }
    }


    public void markToDo(int cur_id) {
        List<Task> tasks = readTasksFromFile();
        boolean found = false;
        for (Task t : tasks){
            if (t.id == cur_id){
                t.status = "to do";
                writeTasksToFile(tasks);
                System.out.println("Task with ID " + cur_id + " marked as to do successfully.");
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("Task with ID " + cur_id + " doesn't exist.");
        }
    }

    public void listTasks() {
        List<Task> tasks = readTasksFromFile();
        if(tasks.isEmpty()){
            System.out.println("List of tasks is empty.");
            return;
        }

        for (Task t: tasks){
            System.out.println("[ID: " + t.id + "] " + t.description +
                    " | Status: " + t.status +
                    " | Created at: " + t.createdAt +
                    " | Updated at: " + t.updatedAt);
        }
    }

    public void listTasksByStatus(String status) {
        List<Task> tasks = readTasksFromFile();
        boolean found  = false;
        if(tasks.isEmpty()){
            System.out.println("List of tasks is empty.");
            return;
        }
        for (Task t: tasks){
            if(t.status.equalsIgnoreCase(status)){
                found = true;
                System.out.println("[ID: " + t.id + "] " + t.description +
                        " | Status: " + t.status +
                        " | Created at: " + t.createdAt +
                        " | Updated at: " + t.updatedAt);
            }
        }
        if(!found){
            System.out.println("Tasks with " + status + " are not found");
        }
    }


    private String getCurrentTime() {
        return java.time.LocalDateTime.now().toString();
    }

    private List<Task> readTasksFromFile() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            String data = json.toString().trim();
            if (!data.isEmpty() && !data.equals("[]")) {
                // Примитивный парсинг JSON вручную
                String[] items = data.substring(1, data.length() - 1).split("},\\s*\\{");
                for (int i = 0; i < items.length; i++) {
                    String item = items[i];
                    if (!item.startsWith("{")) item = "{" + item;
                    if (!item.endsWith("}")) item = item + "}";

                    Task task = parseTask(item);
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return tasks;
    }

    private void writeTasksToFile(List<Task> tasks) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("[\n");
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": " + t.id + ",\n");
                writer.write("    \"description\": \"" + t.description + "\",\n");
                writer.write("    \"status\": \"" + t.status + "\",\n");
                writer.write("    \"createdAt\": \"" + t.createdAt + "\",\n");
                writer.write("    \"updatedAt\": \"" + t.updatedAt + "\"\n");
                writer.write("  }" + (i < tasks.size() - 1 ? "," : "") + "\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    private Task parseTask(String json) {
        Task t = new Task();
        json = json.replaceAll("[\\{\\}\"]", "");
        String[] parts = json.split(",");

        for (String part : parts) {
            String[] kv = part.trim().split(":", 2);
            String key = kv[0].trim();
            String value = kv[1].trim();
            switch (key) {
                case "id":
                    t.id = Integer.parseInt(value);
                    break;
                case "description":
                    t.description = value;
                    break;
                case "status":
                    t.status = value;
                    break;
                case "createdAt":
                    t.createdAt = value;
                    break;
                case "updatedAt":
                    t.updatedAt = value;
                    break;
            }
        }
        return t;
    }
}