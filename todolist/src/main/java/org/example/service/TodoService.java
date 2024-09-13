package org.example.service;

import org.example.model.Task;
import org.example.model.User;
import org.example.repository.TaskRepository;
import java.util.List;

public class TodoService {
    private TaskRepository taskRepository;

    public TodoService() {
        this.taskRepository = new TaskRepository();
    }

    public void addTask(User user, String description) {
        Task newTask = new Task(description, false, user);
        taskRepository.save(newTask);
    }

    public List<Task> getUserTasks(User user) {
        return taskRepository.findAllByUser(user);
    }

    public void markTaskAsCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId);
        if (task != null) {
            task.setCompleted(true);
            taskRepository.update(task);
        }
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId);
        if (task != null) {
            taskRepository.delete(task);
        }
    }

    public void updateTaskDescription(Long taskId, String newDescription) {
        Task task = taskRepository.findById(taskId);
        if (task != null) {
            task.setDescription(newDescription);
            taskRepository.update(task);
        }
    }
}