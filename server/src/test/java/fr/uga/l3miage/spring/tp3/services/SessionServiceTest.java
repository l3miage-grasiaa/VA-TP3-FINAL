package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {
    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private ExamComponent examComponent;

    @MockBean
    private SessionComponent sessionComponent;

    @Test
    void createSessionTest() throws ExamNotFoundException {
        // given
        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .name("matin")
                .startDate(LocalDateTime.parse("2019-03-27T10:15:30"))
                .endDate(LocalDateTime.parse("2019-03-27T12:15:30"))
                .build();

        EcosSessionEntity ecosSessionEntity = new EcosSessionEntity();
        when(sessionMapper.toEntity(sessionCreationRequest)).thenReturn(ecosSessionEntity);
        ecosSessionEntity.setExamEntities(Set.of());

        EcosSessionProgrammationEntity programmationEntity = new EcosSessionProgrammationEntity();
        ecosSessionEntity.setEcosSessionProgrammationEntity(programmationEntity);
        when(sessionMapper.toEntity(sessionCreationRequest.getEcosSessionProgrammation())).thenReturn(programmationEntity);

        when(examComponent.getAllById(Set.of())).thenReturn(Set.of());
        when(sessionComponent.createSession(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);

        SessionResponse sessionResponseExpected = sessionMapper.toResponse(ecosSessionEntity);

        // when
        SessionResponse sessionResponse = sessionService.createSession(sessionCreationRequest);

        // then
        assertThat(sessionResponse).usingRecursiveComparison().isEqualTo(sessionResponseExpected);
    }

}
