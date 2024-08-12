import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {
    private ToDoList toDoList;
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField taskField;
    private JRadioButton completeButton;
    private JRadioButton pendingButton;
    private ButtonGroup statusGroup;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearButton;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;

    public Main() {
        toDoList = new ToDoList();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        setTitle("To-Do List Application");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("To-Do List", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        taskList.setFont(new Font("Serif", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(taskList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        taskField = new JTextField(20);
        completeButton = new JRadioButton("Complete");
        pendingButton = new JRadioButton("Pending");
        statusGroup = new ButtonGroup();
        statusGroup.add(completeButton);
        statusGroup.add(pendingButton);

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.HOUR_OF_DAY);
        timeSpinner = new JSpinner(timeModel);
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));

        JButton addButton = new JButton("Add Task");
        JButton updateButton = new JButton("Update Task");
        JButton deleteButton = new JButton("Delete Task");

        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear All Tasks");

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Task:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(taskField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(dateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Time:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(timeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(completeButton, gbc);
        gbc.gridy = 4;
        inputPanel.add(pendingButton, gbc);

        gbc.gridy = 5;
        inputPanel.add(addButton, gbc);
        gbc.gridy = 6;
        inputPanel.add(updateButton, gbc);
        gbc.gridy = 7;
        inputPanel.add(deleteButton, gbc);

        
        gbc.gridy = 8;
        inputPanel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(searchField, gbc);
        gbc.gridy = 9;
        inputPanel.add(searchButton, gbc);


        gbc.gridy = 10;
        inputPanel.add(clearButton, gbc);

        panel.add(inputPanel, BorderLayout.SOUTH);
        add(panel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        });


        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTasks();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllTasks();
            }
        });
    }

    private void addTask() {
        String taskDescription = taskField.getText();
        boolean isCompleted = completeButton.isSelected();
        Date taskDate = (Date) dateSpinner.getValue();
        Date taskTime = (Date) timeSpinner.getValue();

        if (!taskDescription.isEmpty()) {
            Task task = new Task(taskDescription);
            if (isCompleted) {
                task.markCompleted();
            } else {
                task.markPending();
            }
            toDoList.addTask(task);
            updateTaskList();
            taskField.setText("");
            statusGroup.clearSelection();

            scheduleReminder(taskDescription, combineDateAndTime(taskDate, taskTime));
        } else {
            JOptionPane.showMessageDialog(this, "Task description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date combineDateAndTime(Date date, Date time) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        try {
            String dateString = dateFormatter.format(date);
            String timeString = timeFormatter.format(time);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString + " " + timeString);
        } catch (Exception e) {
            return null;
        }
    }

    private void scheduleReminder(String taskDescription, Date reminderDateTime) {
        Timer timer = new Timer();
        TimerTask reminderTask = new TimerTask() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Reminder: " + taskDescription, "Task Reminder", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        timer.schedule(reminderTask, reminderDateTime);
    }

    private void updateTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String newDescription = taskField.getText();
            boolean isCompleted = completeButton.isSelected();

            toDoList.editTask(selectedIndex, newDescription);
            if (isCompleted) {
                toDoList.getTasks().get(selectedIndex).markCompleted();
            } else {
                toDoList.getTasks().get(selectedIndex).markPending();
            }
            updateTaskList();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            toDoList.removeTask(selectedIndex);
            updateTaskList();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchTasks() {
        String keyword = searchField.getText().toLowerCase();
        listModel.clear();
        for (Task task : toDoList.getTasks()) {
            if (task.getDescription().toLowerCase().contains(keyword)) {
                listModel.addElement(task.toString());
            }
        }
    }

    private void clearAllTasks() {
        toDoList.getTasks().clear();
        updateTaskList();
    }

    private void updateTaskList() {
        listModel.clear();
        for (Task task : toDoList.getTasks()) {
            listModel.addElement(task.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
