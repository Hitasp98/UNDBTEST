package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.TeacherTable} entity.
 */
public class TeacherTableDTO implements Serializable {

    private Long id;

    private String name;

    private String lastName;

    private String field;

    private String certifiction;

    private String tel;

    private String address;

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

    public String getCertifiction() {
        return certifiction;
    }

    public void setCertifiction(String certifiction) {
        this.certifiction = certifiction;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeacherTableDTO)) {
            return false;
        }

        TeacherTableDTO teacherTableDTO = (TeacherTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teacherTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherTableDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", field='" + getField() + "'" +
            ", certifiction='" + getCertifiction() + "'" +
            ", tel='" + getTel() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
