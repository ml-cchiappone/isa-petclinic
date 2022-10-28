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
 * A Pets.
 */
@Entity
@Table(name = "pets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @OneToMany(mappedBy = "pet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pet" }, allowSetters = true)
    private Set<Visits> visits = new HashSet<>();

    @ManyToOne
    private Types type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pets" }, allowSetters = true)
    private Human human;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pets" }, allowSetters = true)
    private Vet vet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pets id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Pets name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Visits> getVisits() {
        return this.visits;
    }

    public void setVisits(Set<Visits> visits) {
        if (this.visits != null) {
            this.visits.forEach(i -> i.setPet(null));
        }
        if (visits != null) {
            visits.forEach(i -> i.setPet(this));
        }
        this.visits = visits;
    }

    public Pets visits(Set<Visits> visits) {
        this.setVisits(visits);
        return this;
    }

    public Pets addVisits(Visits visits) {
        this.visits.add(visits);
        visits.setPet(this);
        return this;
    }

    public Pets removeVisits(Visits visits) {
        this.visits.remove(visits);
        visits.setPet(null);
        return this;
    }

    public Types getType() {
        return this.type;
    }

    public void setType(Types types) {
        this.type = types;
    }

    public Pets type(Types types) {
        this.setType(types);
        return this;
    }

    public Human getHuman() {
        return this.human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public Pets human(Human human) {
        this.setHuman(human);
        return this;
    }

    public Vet getVet() {
        return this.vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }

    public Pets vet(Vet vet) {
        this.setVet(vet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pets)) {
            return false;
        }
        return id != null && id.equals(((Pets) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pets{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
