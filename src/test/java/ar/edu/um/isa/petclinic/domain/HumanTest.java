package ar.edu.um.isa.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Human.class);
        Human human1 = new Human();
        human1.setId(1L);
        Human human2 = new Human();
        human2.setId(human1.getId());
        assertThat(human1).isEqualTo(human2);
        human2.setId(2L);
        assertThat(human1).isNotEqualTo(human2);
        human1.setId(null);
        assertThat(human1).isNotEqualTo(human2);
    }
}
