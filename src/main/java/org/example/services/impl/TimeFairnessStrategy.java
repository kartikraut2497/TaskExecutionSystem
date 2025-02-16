package org.example.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.TaskQueue;
import org.example.services.IFairnessStrategy;

@AllArgsConstructor
@Slf4j
public class TimeFairnessStrategy implements IFairnessStrategy {

    private TaskQueue taskQueue;

    @Override
    public void reAlignTasks() {
        if(taskQueue.getLowPriorityQueue().isEmpty()) return ;

        log.info("Re-Aligning tasks...");
        boolean upgradeTask = false;
        do{
            long currTime = System.currentTimeMillis();
            assert taskQueue.getLowPriorityQueue().peek() != null;
            if(currTime-taskQueue.getLowPriorityQueue().peek().getCreationTime() > 5000){
                long taskId = taskQueue.getLowPriorityQueue().peek().getTaskId();
                log.info("Promoting task {}", taskId);
                taskQueue.upgradeTasks();
                upgradeTask = true;
            }
            else upgradeTask = false;
        }
        while(upgradeTask);

    }
}
