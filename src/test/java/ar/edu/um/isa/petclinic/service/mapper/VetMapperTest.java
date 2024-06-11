package ar.edu.um.isa.petclinic.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.isa.petclinic.domain.Vet;
import ar.edu.um.isa.petclinic.service.dto.VetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VetMapperTest {

    private VetMapper vetMapper;

    @BeforeEach
    public void setUp() {
        vetMapper = new VetMapperImpl();
    }

    @Test
    public void testNullVetToEntity() {
        Vet vet = vetMapper.toEntity((VetDTO) null);

        assertNull(vet);
    }

    @Test
    public void testVetToEntity() {
        VetDTO dto = new VetDTO();
        dto.setFirstname("set first_name");
        dto.setLastname("set last_name");

        Vet vet = vetMapper.toEntity(dto);

        assertNotNull(vet);
        assertEquals("set first_name", vet.getFirstname());
        assertEquals("set last_name", vet.getLastname());
    }
    

    @Test
    public void testPartialUpdate() {
        Vet vet = new Vet();
        VetDTO dto = new VetDTO();
        dto.setFirstname("updated first name");
        dto.setLastname("updated last name");
        vetMapper.partialUpdate(vet, dto);

        assertEquals("updated first name", vet.getFirstname());
        assertEquals("updated last name", vet.getLastname());
    }
}
