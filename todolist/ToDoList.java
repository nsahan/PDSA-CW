
import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private List<Task> tasks;

    public ToDoList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public void editTask(int index, String newDescription) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).setDescription(newDescription);
        }
    }

    public void changeTaskStatus(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task task = tasks.get(index);
            if (task.isCompleted()) {
                task.markPending();
            } else {
                task.markCompleted();
            }
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void clearTasks() {
        throw new UnsupportedOperationException("Unimplemented method 'clearTasks'");
    }
}
