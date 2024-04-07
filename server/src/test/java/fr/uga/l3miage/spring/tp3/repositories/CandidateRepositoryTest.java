package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static fr.uga.l3miage.spring.tp3.enums.TestCenterCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    void testRequestfindAllByTestCenterEntityCode(){
        // given
        TestCenterEntity testCenterEntityGRE = TestCenterEntity
                .builder()
                .code(GRE)
                .university("Universite Grenoble Alpes")
                .city("Grenoble")
                .build();

        TestCenterEntity testCenterEntityDIJ = TestCenterEntity
                .builder()
                .code(DIJ)
                .university("Universite de Dijon")
                .city("Dijon")
                .build();

        testCenterRepository.save(testCenterEntityGRE);
        testCenterRepository.save(testCenterEntityDIJ);


        CandidateEntity candidateEntityGRE = CandidateEntity
                .builder()
                .email("iAmAStudent@univ-grenoble-alpes.fr")
                .testCenterEntity(testCenterEntityGRE)
                .build();

        CandidateEntity candidateEntityDIJ = CandidateEntity
                .builder()
                .email("test@univ-dijon.fr")
                .testCenterEntity(testCenterEntityDIJ)
                .build();

        candidateRepository.save(candidateEntityGRE);
        candidateRepository.save(candidateEntityDIJ);

        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(GRE); // selection de la bd
        Set<CandidateEntity> candidateEntitiesResponses1 = candidateRepository.findAllByTestCenterEntityCode(DIJ); // selection de la bd

        //then
        assertThat(candidateEntitiesResponses1).hasSize(1);
        assertThat(candidateEntitiesResponses).hasSize(1);

    }

    @Test
    void testRequestfindAllByCandidateEvaluationGridEntitiesGradeLessThan(){
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

        Set<CandidateEvaluationGridEntity> setCandidateEntity2 = new HashSet<>();
        setCandidateEntity2.add(candidateEvaluationGridEntityNonValide);


        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .email("iAmAStudent@univ-grenoble-alpes.fr")
                .candidateEvaluationGridEntities(setCandidateEntity1)
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .email("whereAreU?@gmail.com")
                .candidateEvaluationGridEntities(setCandidateEntity2)
                .build();

        candidateEvaluationGridEntityValide.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntityNonValide.setCandidateEntity(candidateEntity2);


        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);


        candidateEvaluationGridRepository.save(candidateEvaluationGridEntityValide);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntityNonValide);


        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(15); // selection de la bd

        //then
        assertThat(candidateEntitiesResponses).hasSize(2);
    }

    @Test
    void testRequestfindAllByHasExtraTimeFalseAndBirthDateBefore(){
        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("1992-07-17"))
                .hasExtraTime(true)
                .firstname("Prenom de test")
                .lastname("Nom de test")
                .email("test@gmail.com")
                .phoneNumber("+33 1 00")
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("2001-06-17"))
                .hasExtraTime(true)
                .firstname("Prenom de test1")
                .lastname("Nom de test1")
                .email("test1@gmail.com")
                .phoneNumber("+33 1 11")
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("1995-05-18"))
                .hasExtraTime(false)
                // .id(12345678910L)
                .firstname("Prenom de test2")
                .lastname("Nom de test2")
                .email("test2@gmail.com")
                .phoneNumber("+33 1 22")
                .build();

        CandidateEntity candidateEntity3 = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("1996-08-18"))
                .hasExtraTime(false)
                .firstname("Prenom de test3")
                .lastname("Nom de test3")
                .email("test3@gmail.com")
                .phoneNumber("+33 1 33")
                .build();


        // savegarde les entity
        candidateRepository.save(candidateEntity); // insertion dans la bd
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);
        candidateRepository.save(candidateEntity3);

        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("2000-01-01"));
        Set<CandidateEntity> candidateEntitiesResponses1 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("1900-01-01"));

        //then
        assertThat(candidateEntitiesResponses).hasSize(2); // Le résultat sont candidateEntity2 et candidateEntity3; puisqu'ils sont false et en dessous de 2000
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getBirthDate()).isEqualTo(LocalDate.parse("1995-05-18"));

        assertThat(candidateEntitiesResponses1).hasSize(0);

    }
}
