package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A LessonTable.
 */
@Entity
@Table(name = "lesson_table")
public class LessonTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit")
    private String unit;

    @Column(name = "address")
    private String address;

    @Column(name = "tel")
    private String tel;

    @ManyToMany(mappedBy = "lessonTables")
    @JsonIgnoreProperties(value = { "lessonTables" }, allowSetters = true)
    private Set<StudentTable> studentTables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LessonTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public LessonTable name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public LessonTable unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAddress() {
        return this.address;
    }

    public LessonTable address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return this.tel;
    }

    public LessonTable tel(String tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Set<StudentTable> getStudentTables() {
        return this.studentTables;
    }

    public void setStudentTables(Set<StudentTable> studentTables) {
        if (this.studentTables != null) {
            this.studentTables.forEach(i -> i.removeLessonTable(this));
        }
        if (studentTables != null) {
            studentTables.forEach(i -> i.addLessonTable(this));
        }
        this.studentTables = studentTables;
    }

    public LessonTable studentTables(Set<StudentTable> studentTables) {
        this.setStudentTables(studentTables);
        return this;
    }

    public LessonTable addStudentTable(StudentTable studentTable) {
        this.studentTables.add(studentTable);
        studentTable.getLessonTables().add(this);
        return this;
    }

    public LessonTable removeStudentTable(StudentTable studentTable) {
        this.studentTables.remove(studentTable);
        studentTable.getLessonTables().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonTable)) {
            return false;
        }
        return id != null && id.equals(((LessonTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonTable{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", address='" + getAddress() + "'" +
            ", tel='" + getTel() + "'" +
            "}";
    }
}
