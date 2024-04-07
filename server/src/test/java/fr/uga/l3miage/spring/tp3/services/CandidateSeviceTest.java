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
        // membuat entitas nilai
        CandidateEvaluationGridEntity candidateEvaluationGridEntityValide = CandidateEvaluationGridEntity
                .builder()
                .grade(12.5)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntityNonValide = CandidateEvaluationGridEntity
                .builder()
                .grade(9.8)
                .build();

        // ngebuat sebuah liste nilai, yang nantinya akan di assign ke sebuah candidate
        Set<CandidateEvaluationGridEntity> setCandidateEntity1 = new HashSet<>(); // membuat list nilai
        setCandidateEntity1.add(candidateEvaluationGridEntityValide); // memasukkan nilai (valide) ke liste nilai
        setCandidateEntity1.add(candidateEvaluationGridEntityNonValide);

        ExamEntity examEntity = ExamEntity
                .builder()
                .weight(3)
                .candidateEvaluationGridEntities(setCandidateEntity1)
                .build();

        // membuat entitas candidate. untuk meng-assign (salah satu) liste nilai yang tadi udah dibuat
        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .email("iAmAStudent@univ-grenoble-alpes.fr")
                .candidateEvaluationGridEntities(setCandidateEntity1) // membuat relasi antara class CandidateEntity dan class CandidateEvaluationGridEntityValide. Menyatukan candidateEntity dengan list candidateEvaluationGridEntityValide. Implementasi relasi unidirectionnel, tapi punya kita itu tuh bidirectionnel.
                .build();


        // implementasi bidirectionnel antara sebuah nilai dan seorang kandidat
        candidateEvaluationGridEntityValide.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntityNonValide.setCandidateEntity(candidateEntity1);

        // implementasi bidirectionnel antara sebuah credit (weight) dan sebuah nilai
        candidateEvaluationGridEntityValide.setExamEntity(examEntity);
        candidateEvaluationGridEntityNonValide.setExamEntity(examEntity);

        /*
        * signature dari fungsi ini ditambahin throws CandidateNotFoundException, karena kalo engga nanti error. Karena
        * si fungsi yang mau kita test itu (getCandidateAverageTest()) ngembaliin CandidateNotFoundRestException kalo
        * misalnya engga ketemu. Jadi biar koheren, fungsi ini (getCandidateAverageTestTest()) juga kita tambahin throws
        * kalo misalnya ada error atau engga nemu kandidat berdasarkan Id yang dikasih.
        * */
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
