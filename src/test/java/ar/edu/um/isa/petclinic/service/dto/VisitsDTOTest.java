package ar.edu.um.isa.petclinic.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisitsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitsDTO.class);
        VisitsDTO visitsDTO1 = new VisitsDTO();
        visitsDTO1.setId(1L);
        VisitsDTO visitsDTO2 = new VisitsDTO();
        assertThat(visitsDTO1).isNotEqualTo(visitsDTO2);
        visitsDTO2.setId(visitsDTO1.getId());
        assertThat(visitsDTO1).isEqualTo(visitsDTO2);
        visitsDTO2.setId(2L);
        assertThat(visitsDTO1).isNotEqualTo(visitsDTO2);
        visitsDTO1.setId(null);
        assertThat(visitsDTO1).isNotEqualTo(visitsDTO2);
    }
}
