package ar.edu.um.isa.petclinic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Visits.
 */
@Entity
@Table(name = "visits")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Visits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "visitdate", nullable = false)
    private Instant visitdate;

    @NotNull
    @Size(max = 255)
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "visits", "type", "human", "vet" }, allowSetters = true)
    private Pets pet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Visits id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getVisitdate() {
        return this.visitdate;
    }

    public Visits visitdate(Instant visitdate) {
        this.setVisitdate(visitdate);
        return this;
    }

    public void setVisitdate(Instant visitdate) {
        this.visitdate = visitdate;
    }

    public String getDescription() {
        return this.description;
    }

    public Visits description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Pets getPet() {
        return this.pet;
    }

    public void setPet(Pets pets) {
        this.pet = pets;
    }

    public Visits pet(Pets pets) {
        this.setPet(pets);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Visits)) {
            return false;
        }
        return id != null && id.equals(((Visits) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Visits{" +
            "id=" + getId() +
            ", visitdate='" + getVisitdate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
