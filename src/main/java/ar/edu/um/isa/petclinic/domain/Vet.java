package ar.edu.um.isa.petclinic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vet.
 */
@Entity
@Table(name = "vet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "firstname", length = 32, nullable = false)
    private String firstname;

    @NotNull
    @Size(max = 32)
    @Column(name = "lastname", length = 32, nullable = false)
    private String lastname;

    @OneToMany(mappedBy = "vet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "visits", "type", "human", "vet" }, allowSetters = true)
    private Set<Pets> pets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Vet firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Vet lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Pets> getPets() {
        return this.pets;
    }

    public void setPets(Set<Pets> pets) {
        if (this.pets != null) {
            this.pets.forEach(i -> i.setVet(null));
        }
        if (pets != null) {
            pets.forEach(i -> i.setVet(this));
        }
        this.pets = pets;
    }

    public Vet pets(Set<Pets> pets) {
        this.setPets(pets);
        return this;
    }

    public Vet addPets(Pets pets) {
        this.pets.add(pets);
        pets.setVet(this);
        return this;
    }

    public Vet removePets(Pets pets) {
        this.pets.remove(pets);
        pets.setVet(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vet)) {
            return false;
        }
        return id != null && id.equals(((Vet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vet{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            "}";
    }
}
