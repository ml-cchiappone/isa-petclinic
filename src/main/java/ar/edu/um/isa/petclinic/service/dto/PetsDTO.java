package ar.edu.um.isa.petclinic.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ar.edu.um.isa.petclinic.domain.Pets} entity.
 */
public class PetsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 32)
    private String name;

    private TypesDTO type;

    private HumanDTO human;

    private VetDTO vet;

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

    public TypesDTO getType() {
        return type;
    }

    public void setType(TypesDTO type) {
        this.type = type;
    }

    public HumanDTO getHuman() {
        return human;
    }

    public void setHuman(HumanDTO human) {
        this.human = human;
    }

    public VetDTO getVet() {
        return vet;
    }

    public void setVet(VetDTO vet) {
        this.vet = vet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PetsDTO)) {
            return false;
        }

        PetsDTO petsDTO = (PetsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, petsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PetsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type=" + getType() +
            ", human=" + getHuman() +
            ", vet=" + getVet() +
            "}";
    }
}
