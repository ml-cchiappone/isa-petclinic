package ar.edu.um.isa.petclinic.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ar.edu.um.isa.petclinic.domain.Vet} entity.
 */
public class VetDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 32)
    private String firstname;

    @NotNull
    @Size(max = 32)
    private String lastname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VetDTO)) {
            return false;
        }

        VetDTO vetDTO = (VetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VetDTO{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            "}";
    }
}
