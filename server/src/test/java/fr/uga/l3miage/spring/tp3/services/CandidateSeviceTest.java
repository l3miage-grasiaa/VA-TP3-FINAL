package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateSeviceTest {
    @Autowired
    private CandidateService candidateService;

    @MockBean
    private CandidateComponent candidateComponent;

    @Test
    void getCandidateAverageTest() throws CandidateNotFoundException {
        // given
        CandidateEvaluationGridEntity candidateEvaluationGridEntityValide = CandidateEvaluationGridEntity
                .builder()
                .grade(12.5)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntityNonValide = CandidateEvaluationGridEntity
                .builder()
                .grade(9.8)
                .build();

        Set<CandidateEvaluationGridEntity> setCandidateEntity1 = new HashSet<>();
        setCandidateEntity1.add(candidateEvaluationGridEntityValide);
        setCandidateEntity1.add(candidateEvaluationGridEntityNonValide);

        ExamEntity examEntity = ExamEntity
                .builder()
                .weight(3)
                .candidateEvaluationGridEntities(setCandidateEntity1)
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .email("iAmAStudent@univ-grenoble-alpes.fr")
                .candidateEvaluationGridEntities(setCandidateEntity1)
                .build();


        candidateEvaluationGridEntityValide.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntityNonValide.setCandidateEntity(candidateEntity1);

        candidateEvaluationGridEntityValide.setExamEntity(examEntity);
        candidateEvaluationGridEntityNonValide.setExamEntity(examEntity);

        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidateEntity1);

        // when
        Double average = candidateService.getCandidateAverage(anyLong());

        /*
        * average = 0
        * a = (12.5 * 3) // 37.5
        * average + a // 37.5
        * a = (9.8 * 3) // 29.4
        * average + a // 66.9
        * average / listGridEntity.length() // 66.9 / 2
        * */
        // Then
        assertEquals(33.45, average);
    }
}
