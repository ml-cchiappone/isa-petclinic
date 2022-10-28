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
 * A Human.
 */
@Entity
@Table(name = "human")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Human implements Serializable {

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

    @NotNull
    @Size(max = 255)
    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Size(max = 32)
    @Column(name = "city", length = 32)
    private String city;

    @NotNull
    @Size(max = 20)
    @Column(name = "telephone", length = 20, nullable = false)
    private String telephone;

    @OneToMany(mappedBy = "human")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "visits", "type", "human", "vet" }, allowSetters = true)
    private Set<Pets> pets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Human id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Human firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Human lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return this.address;
    }

    public Human address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Human city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Human telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<Pets> getPets() {
        return this.pets;
    }

    public void setPets(Set<Pets> pets) {
        if (this.pets != null) {
            this.pets.forEach(i -> i.setHuman(null));
        }
        if (pets != null) {
            pets.forEach(i -> i.setHuman(this));
        }
        this.pets = pets;
    }

    public Human pets(Set<Pets> pets) {
        this.setPets(pets);
        return this;
    }

    public Human addPets(Pets pets) {
        this.pets.add(pets);
        pets.setHuman(this);
        return this;
    }

    public Human removePets(Pets pets) {
        this.pets.remove(pets);
        pets.setHuman(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Human)) {
            return false;
        }
        return id != null && id.equals(((Human) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Human{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
