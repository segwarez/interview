package com.segwarez;

import java.util.ArrayDeque;
import java.util.Deque;

public class MemoryLeak {
    public static void main(String[] args) {
        TaskList taskList = new TaskList();
        final TaskCreator taskCreator = new TaskCreator(taskList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000000; i++) {
                    taskCreator.createTask();
                }
            }
        }).start();
    }

    private static class TaskCreator {
        private TaskList taskList;

        public TaskCreator(TaskList taskList) {
            this.taskList = taskList;
        }

        public void createTask() {
            taskList.addTask(new Task());
        }
    }

    private static class TaskList {
        private Deque<Task> tasks = new ArrayDeque<Task>();

        public void addTask(Task task) {
            tasks.add(task);
            tasks.peek().execute();
        }
    }

    private static class Task {
        private Object[] array = new Object[1000];

        public void execute() {
        }
    }
}
