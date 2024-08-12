import java.io.Serializable;

public class Task implements Serializable {
    private String description;
    private boolean isCompleted;

    public Task(String description) {
        this.description = description;
        this.isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void markCompleted() {
        isCompleted = true;
    }

    public void markPending() {
        isCompleted = false;
    }

    @Override
    public String toString() {
        return description + " [" + (isCompleted ? "Done" : "Pending") + "]";
    }

    public boolean isCompleted() {
        
        throw new UnsupportedOperationException("Unimplemented method 'isCompleted'");
    }

    public void setText(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setText'");
    }
}
