package ar.edu.um.isa.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PetsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pets.class);
        Pets pets1 = new Pets();
        pets1.setId(1L);
        Pets pets2 = new Pets();
        pets2.setId(pets1.getId());
        assertThat(pets1).isEqualTo(pets2);
        pets2.setId(2L);
        assertThat(pets1).isNotEqualTo(pets2);
        pets1.setId(null);
        assertThat(pets1).isNotEqualTo(pets2);
    }
}
