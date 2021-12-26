package maddox.TaskPriority;

public interface Task {

    maddox.TaskPriority.Priority priority();

    boolean validate();


    boolean execute();

}
