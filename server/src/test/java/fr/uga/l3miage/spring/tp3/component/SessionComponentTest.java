package fr.uga.l3miage.spring.tp3.component;

import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;

    @MockBean
    private EcosSessionRepository ecosSessionRepository;

    @MockBean
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;

    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;


    // comment
    @Test
    void createSessionTest(){
        // given
        EcosSessionEntity sessionEntity = EcosSessionEntity.builder().build();
        EcosSessionProgrammationEntity programmationEntity = EcosSessionProgrammationEntity.builder().build();
        EcosSessionProgrammationStepEntity ecosSessionProgrammationStepEntity = EcosSessionProgrammationStepEntity.builder().build();
        sessionEntity.setEcosSessionProgrammationEntity(programmationEntity);

        // given
        when(ecosSessionProgrammationRepository.save(programmationEntity)).thenReturn(programmationEntity);
        when(ecosSessionProgrammationStepRepository.save(ecosSessionProgrammationStepEntity)).thenReturn(ecosSessionProgrammationStepEntity);

        // when
        sessionComponent.createSession(sessionEntity);

        // then
        verify(ecosSessionRepository, times(1)).save(sessionEntity);
        verify(ecosSessionProgrammationRepository, times(1)).save(programmationEntity);
        verify(ecosSessionProgrammationStepRepository, times(1)).saveAll(sessionEntity.getEcosSessionProgrammationEntity().getEcosSessionProgrammationStepEntities());
    }
}
