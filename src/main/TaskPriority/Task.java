package main.TaskPriority;

public interface Task {

    main.TaskPriority.Priority priority();

    boolean validate();


    boolean execute();

}
