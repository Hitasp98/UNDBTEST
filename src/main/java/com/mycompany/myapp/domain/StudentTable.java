package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A StudentTable.
 */
@Entity
@Table(name = "student_table")
public class StudentTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "field")
    private String field;

    @Column(name = "address")
    private String address;

    @Column(name = "tel")
    private String tel;

    @ManyToMany
    @JoinTable(
        name = "rel_student_table__lesson_table",
        joinColumns = @JoinColumn(name = "student_table_id"),
        inverseJoinColumns = @JoinColumn(name = "lesson_table_id")
    )
    @JsonIgnoreProperties(value = { "studentTables" }, allowSetters = true)
    private Set<LessonTable> lessonTables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public StudentTable name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public StudentTable lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getField() {
        return this.field;
    }

    public StudentTable field(String field) {
        this.setField(field);
        return this;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAddress() {
        return this.address;
    }

    public StudentTable address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return this.tel;
    }

    public StudentTable tel(String tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Set<LessonTable> getLessonTables() {
        return this.lessonTables;
    }

    public void setLessonTables(Set<LessonTable> lessonTables) {
        this.lessonTables = lessonTables;
    }

    public StudentTable lessonTables(Set<LessonTable> lessonTables) {
        this.setLessonTables(lessonTables);
        return this;
    }

    public StudentTable addLessonTable(LessonTable lessonTable) {
        this.lessonTables.add(lessonTable);
        lessonTable.getStudentTables().add(this);
        return this;
    }

    public StudentTable removeLessonTable(LessonTable lessonTable) {
        this.lessonTables.remove(lessonTable);
        lessonTable.getStudentTables().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentTable)) {
            return false;
        }
        return id != null && id.equals(((StudentTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentTable{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", field='" + getField() + "'" +
            ", address='" + getAddress() + "'" +
            ", tel='" + getTel() + "'" +
            "}";
    }
}
