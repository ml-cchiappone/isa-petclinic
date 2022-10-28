package ar.edu.um.isa.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vet.class);
        Vet vet1 = new Vet();
        vet1.setId(1L);
        Vet vet2 = new Vet();
        vet2.setId(vet1.getId());
        assertThat(vet1).isEqualTo(vet2);
        vet2.setId(2L);
        assertThat(vet1).isNotEqualTo(vet2);
        vet1.setId(null);
        assertThat(vet1).isNotEqualTo(vet2);
    }
}
