package ar.edu.um.isa.petclinic.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypesDTO.class);
        TypesDTO typesDTO1 = new TypesDTO();
        typesDTO1.setId(1L);
        TypesDTO typesDTO2 = new TypesDTO();
        assertThat(typesDTO1).isNotEqualTo(typesDTO2);
        typesDTO2.setId(typesDTO1.getId());
        assertThat(typesDTO1).isEqualTo(typesDTO2);
        typesDTO2.setId(2L);
        assertThat(typesDTO1).isNotEqualTo(typesDTO2);
        typesDTO1.setId(null);
        assertThat(typesDTO1).isNotEqualTo(typesDTO2);
    }
}
