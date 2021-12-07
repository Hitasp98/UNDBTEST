package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.StudentTable} entity.
 */
public class StudentTableDTO implements Serializable {

    private Long id;

    private String name;

    private String lastName;

    private String field;

    private String address;

    private String tel;

    private Set<LessonTableDTO> lessonTables = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Set<LessonTableDTO> getLessonTables() {
        return lessonTables;
    }

    public void setLessonTables(Set<LessonTableDTO> lessonTables) {
        this.lessonTables = lessonTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentTableDTO)) {
            return false;
        }

        StudentTableDTO studentTableDTO = (StudentTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentTableDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", field='" + getField() + "'" +
            ", address='" + getAddress() + "'" +
            ", tel='" + getTel() + "'" +
            ", lessonTables=" + getLessonTables() +
            "}";
    }
}
