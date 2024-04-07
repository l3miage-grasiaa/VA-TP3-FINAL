package fr.uga.l3miage.spring.tp3.component;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    // Injection le component et des dependances
    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    public void testgetAllEliminatedCandidate(){
        // given
        CandidateEvaluationGridEntity eliminatedCandidate1 = CandidateEvaluationGridEntity
                .builder()
                .grade(9.8)
                .build();

        CandidateEvaluationGridEntity eliminatedCandidate2 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .build();

        Set<CandidateEvaluationGridEntity> setCandidateEntity1 = new HashSet<>(); // création de set CandidateEvaluationGridEntity
        setCandidateEntity1.add(eliminatedCandidate1); // rempli le set avec l'entity eliminatedCandidate1

        // meme chose pour la deuxième set et deuxième entity
        Set<CandidateEvaluationGridEntity> setCandidateEntity2 = new HashSet<>();
        setCandidateEntity2.add(eliminatedCandidate2);

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("1996-08-18"))
                .email("test3@gmail.com")
                .candidateEvaluationGridEntities(setCandidateEntity1) // implementation de la relation unidirectionnel
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("1992-07-17"))
                .email("test3@gmail.com")
                .candidateEvaluationGridEntities(setCandidateEntity2) // assign la deuxième set contenant la deuxième griEntity à la deuxième candidat (relation unidirectionnel)
                .build();

        // implementation bidirectionnel
        eliminatedCandidate1.setCandidateEntity(candidateEntity1);
        eliminatedCandidate2.setCandidateEntity(candidateEntity2);

        // sauvegarde les candidats
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        // sauvegarde les evaluationGridEntity
        candidateEvaluationGridRepository.save(eliminatedCandidate1);
        candidateEvaluationGridRepository.save(eliminatedCandidate2);

        when(candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5)).thenReturn(Set.of(eliminatedCandidate1));

        // when
        Set<CandidateEntity> eliminatedCandidates = candidateComponent.getAllEliminatedCandidate();

        // then
        assertEquals(1, eliminatedCandidates.size());
        assertThat(eliminatedCandidates.stream().findFirst().get().getBirthDate()).isEqualTo(LocalDate.parse("1996-08-18"));
    }

    @Test
    public void getCandidatByIdNotFound() {
        // Given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CandidateNotFoundException.class, () -> candidateComponent.getCandidatById(anyLong()));
    }

    @Test
    void getCandidatByIdFound(){
        //Given
        CandidateEntity candidateEntity = CandidateEntity.builder()
                .email("iAmAStudent@univ-grenoble-alpes.fr")
                .build();
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));

        // when - then
        assertDoesNotThrow(()->candidateComponent.getCandidatById(anyLong()));
    }
}
