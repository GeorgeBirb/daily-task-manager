package Services;

import lombok.Getter;
import lombok.Setter;
import models.TaskCategory;

import javax.faces.bean.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TaskCategoryService {
    private List<TaskCategory> taskCategoryList = new ArrayList<>();

    public List<TaskCategory> getAllTaskCategories() {
        TaskCategory taskCategory1 = new TaskCategory();
        TaskCategory taskCategory2 = new TaskCategory();

        taskCategory1.setId(1);
        taskCategory1.setName("Study");
        taskCategory1.setDescription("Study Description");


        taskCategory2.setId(2);
        taskCategory2.setName("Work Out");
        taskCategory2.setDescription("Work Out Description");

        taskCategoryList.add(taskCategory1);
        taskCategoryList.add(taskCategory2);

        return taskCategoryList;
    }
}
