package ar.edu.um.isa.petclinic.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HumanMapperTest {

    private HumanMapper humanMapper;

    @BeforeEach
    public void setUp() {
        humanMapper = new HumanMapperImpl();
    }
}
