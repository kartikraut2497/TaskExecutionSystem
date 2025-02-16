package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Task;
import org.example.models.TaskQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Data
@AllArgsConstructor
@Slf4j
public class TaskExecutor {

    private int numThreads;
    private IFairnessStrategy fairnessStrategy;
    private TaskQueue taskQueue;

    public void executeTasks(){
        log.info("Executing Tasks");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(numThreads+1);

        for(int i=0; i<numThreads; i++){
            executorService.submit(this::executeAndPickupTasks);
        }

        executorService.scheduleAtFixedRate(fairnessStrategy::reAlignTasks, 5, 5, TimeUnit.SECONDS);
    }

    private void executeAndPickupTasks() {
        log.info("Thread {} picking up tasks", Thread.currentThread().threadId());
        while(true){
            Task currTask = null;
            try {
                currTask = this.taskQueue.getTask();
                if(currTask != null) {
                    log.info("Thread {} executing task {}", Thread.currentThread().threadId(), currTask.getTaskId());
                    currTask.runTask();
                }
                else{
                    log.info("All tasks are executed at time {}", System.currentTimeMillis());
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        log.info("End time: {}", System.currentTimeMillis());
    }

}
