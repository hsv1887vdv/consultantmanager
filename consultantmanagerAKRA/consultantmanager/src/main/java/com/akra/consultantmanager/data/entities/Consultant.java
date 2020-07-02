package com.akra.consultantmanager.data.entities;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="consultant")
public class Consultant {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String firstName;

    @Column
    String lastName;

    @Column
    boolean inProject;

    @Column
    @ElementCollection
    List<String> technologies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isInProject() {
        return inProject;
    }

    public void setInProject(boolean inProject) {
        this.inProject = inProject;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }
}
