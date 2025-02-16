package org.example.models;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.TaskPriority;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@Slf4j
public class TaskQueue {

    private BlockingQueue<Task> highPriorityQueue;
    private BlockingQueue<Task> mediumPriorityQueue;
    private BlockingQueue<Task> lowPriorityQueue;

    public TaskQueue(){
        this.highPriorityQueue = new LinkedBlockingQueue<>();
        this.mediumPriorityQueue = new LinkedBlockingQueue<>();
        this.lowPriorityQueue = new LinkedBlockingQueue<>();
    }

    public Task getTask() throws InterruptedException {
        Task task;
        if((task = this.highPriorityQueue.poll()) != null) return task;
        if((task = this.mediumPriorityQueue.poll()) != null) return task;

        return this.lowPriorityQueue.poll();
    }

    public void addTask(Task task){
        if(task.getTaskPriority().equals(TaskPriority.HIGH)) this.highPriorityQueue.add(task);
        else if(task.getTaskPriority().equals(TaskPriority.MEDIUM)) this.mediumPriorityQueue.add(task);
        else this.lowPriorityQueue.add(task);
    }

    public void upgradeTasks(){
        log.info("Upgrading low priority Tasks");
        this.mediumPriorityQueue.add(Objects.requireNonNull(this.lowPriorityQueue.poll()));
    }

}
