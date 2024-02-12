package models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskCategory implements Serializable {
    private int id;
    private String name;
    private String description;
}