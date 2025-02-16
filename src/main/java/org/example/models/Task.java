package org.example.models;

import lombok.Data;
import org.example.enums.TaskPriority;

@Data
public class Task {

    private Long taskId;
    private TaskPriority taskPriority;
    private Runnable job;
    private Long creationTime;

    public Task(Long taskId, TaskPriority taskPriority, Runnable job){
        this.taskId = taskId;
        this.taskPriority = taskPriority;
        this.job = job;
        this.creationTime = System.currentTimeMillis();
    }

    public void runTask(){
        this.job.run();
    }

}
