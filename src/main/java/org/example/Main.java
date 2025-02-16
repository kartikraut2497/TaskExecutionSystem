package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.TaskPriority;
import org.example.models.Task;
import org.example.models.TaskQueue;
import org.example.services.IFairnessStrategy;
import org.example.services.TaskExecutor;
import org.example.services.impl.TimeFairnessStrategy;

@Slf4j
public class Main {

    public static void main(String[] args) {

        log.info("Inside Main Method");
        log.info("Start time: {}", System.currentTimeMillis());
        TaskQueue taskQueue = new TaskQueue();
        IFairnessStrategy fairnessStrategy = new TimeFairnessStrategy(taskQueue);
        TaskExecutor executor = new TaskExecutor(3, fairnessStrategy, taskQueue);

        taskQueue.addTask(new Task(1L, TaskPriority.HIGH, Main::addHighPriorityTask));
        taskQueue.addTask(new Task(2L, TaskPriority.HIGH, Main::addHighPriorityTask));
        taskQueue.addTask(new Task(3L, TaskPriority.HIGH, Main::addHighPriorityTask));
        taskQueue.addTask(new Task(4L, TaskPriority.HIGH, Main::addHighPriorityTask));
        taskQueue.addTask(new Task(5L, TaskPriority.HIGH, Main::addHighPriorityTask));

        taskQueue.addTask(new Task(10L, TaskPriority.MEDIUM, Main::addMediumPriorityTask));
        taskQueue.addTask(new Task(11L, TaskPriority.MEDIUM, Main::addMediumPriorityTask));
        taskQueue.addTask(new Task(12L, TaskPriority.MEDIUM, Main::addMediumPriorityTask));
        taskQueue.addTask(new Task(13L, TaskPriority.MEDIUM, Main::addMediumPriorityTask));

        taskQueue.addTask(new Task(21L, TaskPriority.LOW, Main::addLowPriorityTask));
        taskQueue.addTask(new Task(22L, TaskPriority.LOW, Main::addLowPriorityTask));
        taskQueue.addTask(new Task(23L, TaskPriority.LOW, Main::addLowPriorityTask));
        taskQueue.addTask(new Task(24L, TaskPriority.LOW, Main::addLowPriorityTask));
        taskQueue.addTask(new Task(25L, TaskPriority.LOW, Main::addLowPriorityTask));

        executor.executeTasks();

        Runtime.getRuntime().addShutdownHook(new Thread(executor::stop));
    }

    private static void addHighPriorityTask(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addMediumPriorityTask(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addLowPriorityTask(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}