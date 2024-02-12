package com.example.primefaces.bean;

import Services.TaskCategoryService;
import lombok.Getter;
import lombok.Setter;
import models.TaskCategory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ManagedBean
@SessionScoped
public class TaskCategoryBean {

    private List<TaskCategory> taskCategoryList = new ArrayList<>();
    @Inject
    private TaskCategoryService service = new TaskCategoryService();

    @PostConstruct
    public void init() {
        taskCategoryList = service.getAllTaskCategories();
    }
}
