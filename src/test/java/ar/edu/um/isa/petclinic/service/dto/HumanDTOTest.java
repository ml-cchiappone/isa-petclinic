package ar.edu.um.isa.petclinic.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HumanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumanDTO.class);
        HumanDTO humanDTO1 = new HumanDTO();
        humanDTO1.setId(1L);
        HumanDTO humanDTO2 = new HumanDTO();
        assertThat(humanDTO1).isNotEqualTo(humanDTO2);
        humanDTO2.setId(humanDTO1.getId());
        assertThat(humanDTO1).isEqualTo(humanDTO2);
        humanDTO2.setId(2L);
        assertThat(humanDTO1).isNotEqualTo(humanDTO2);
        humanDTO1.setId(null);
        assertThat(humanDTO1).isNotEqualTo(humanDTO2);
    }
}
