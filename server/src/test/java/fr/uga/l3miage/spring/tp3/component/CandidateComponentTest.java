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
    /* 1)
    * Injeksi class CandidateComponent pake anotasi @AutoWired
    * */
    @Autowired
    private CandidateComponent candidateComponent;

    /* 2)
    * Jadi yang dikasih anotasi @MockBean adalah variabel-variabel yang ada atau dibuat
    * di class CandidateComponent. Dan engga secara otomatis @MockBean ini dibuat untuk
    * variabel-variabel tersebut. Misalnya, karena kita mau ngetes fungsi getAllEliminatedCandidate(),
    * jadi kita injeksi class CandidateEvaluationGridRepository dengan membuat variabel
    * bernama candidateEvaluationGridRepository yang bertipe CandidateEvaluationGridRepository
    * dengan diawali anotasi @MockBean sebelum deklarasi dari variabel tersebut. Nah untuk
    * ngetes fungsi ini, kita engga butuh menginjeksi CandidateRepository. Tapi disini kita
    * injeksi karena kita butuh dia untuk mengetes fungsi getCandidatById().
    * */
    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    public void testgetAllEliminatedCandidate(){
        // given

        /* 2.1)
        * Jadi ini tuh kita bikin entity dari class CandidateEvaluationGridEntity,
        * karena di fungsi getAllEliminatedCandidate() (fungsi yang mau kita test), kita pake
        * candidateEvaluationGridRepository yang mana berisi entity-entity dari class
        * CandidateEvaluationGridEntity. Itu makanya kita butuh bikin entity-entity
        * CandidateEvaluationGridEntity, untuk ngisi candidateEvaluationGridRepository.
        * Dan kita juga telah buat repository nya diatas, untuk mengetes fungsi ini.
        * */
        CandidateEvaluationGridEntity eliminatedCandidate1 = CandidateEvaluationGridEntity
                .builder()
                .grade(9.8)
                .build();

        CandidateEvaluationGridEntity eliminatedCandidate2 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .build();

        // ngebuat sebuah liste nilai, yang nantinya akan di assign ke sebuah candidate
        Set<CandidateEvaluationGridEntity> setCandidateEntity1 = new HashSet<>(); // membuat list nilai
        setCandidateEntity1.add(eliminatedCandidate1); // memasukkan nilai (valide) ke liste nilai

        // ngebuat sebuah liste nilai, yang nantinya akan di assign ke sebuah candidate
        Set<CandidateEvaluationGridEntity> setCandidateEntity2 = new HashSet<>(); // membuat liste kosong nilai
        setCandidateEntity2.add(eliminatedCandidate2); // memasukkan nilai (nonValide) ke liste nilai yang baru dibuat

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("1996-08-18"))
                .email("test3@gmail.com")
                .candidateEvaluationGridEntities(setCandidateEntity1) // membuat relasi antara class CandidateEntity dan class CandidateEvaluationGridEntityValide. Menyatukan candidateEntity dengan list candidateEvaluationGridEntityValide. Implementasi relasi unidirectionnel, tapi punya kita itu tuh bidirectionnel.
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.parse("1992-07-17"))
                .email("test3@gmail.com")
                .candidateEvaluationGridEntities(setCandidateEntity2) // membuat relasi antara class CandidateEntity dan class CandidateEvaluationGridEntityValide. Menyatukan candidateEntity dengan list candidateEvaluationGridEntityValide. Implementasi relasi unidirectionnel, tapi punya kita itu tuh bidirectionnel.
                .build();

        // implementasi bidirectionnel
        eliminatedCandidate1.setCandidateEntity(candidateEntity1);
        eliminatedCandidate2.setCandidateEntity(candidateEntity2);

        /* 2.2)
         * Nah kita harus masukkin si entity-entity dari class CandidateEvaluationGridEntity
         * ke dalam repository nya (candidateEvaluationGridRepository) yang udah dibuat
         * memang untuk menyimpan class CandidateEvaluationGridEntity dan di injeksi
         * untuk (si repository pake anotasi @MockBean) ngetes fungsi ini.
         * */
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        /* 2.3)
        * Nah kita harus masukkin si entity-entity dari class CandidateEvaluationGridEntity
        * ke dalam repository nya (candidateEvaluationGridRepository) yang udah dibuat
        * memang untuk menyimpan class CandidateEvaluationGridEntity dan di injeksi
        * untuk (si repository pake anotasi @MockBean) ngetes fungsi ini.
        * */
        candidateEvaluationGridRepository.save(eliminatedCandidate1);
        candidateEvaluationGridRepository.save(eliminatedCandidate2);

        /* 2.4)
        * Trus disini tuh kita kaya bilang "di candidateEvaluationGridRepository, carikan
        * saya semua gridEntity (maksudnya cari CandidateEvaluationGridEntity, karena si
        * si fungsi findAllByGradeIsLessThanEqual tuh balikkin set yang bertipe
        * CandidateEvaluationGridEntity) yang nilai nya kurang atau sama dengan 5. DAN
        * KEMBALIKAN KEPADA SAYA (apapun yang terjadi) sebuah set yang berisi entity-entity
        * yang saya taro sebagai argumen dari Set.of(..., ...)".
        * Nah jadi sebenarnya si angka yang jadi argumen dari fungsi findAllByGradeIsLessThanEqual
        * engga ngaruh gitu (udah gua test berkali-kali. Meskipun nilai nya gua ganti jadi 10 atau
        * 15, ukuran dari variabel eliminatedCandidates selalu sama dengan jumlah entitas yang gua
        * taro sebagai argumen di Set.of()). Trus nanti kalo ngecek pake asserThat(...), itu tuh
        * adalah entitas yang ditaro di Set.of(...) ini. Jadi kalo misalnya gua taro entitas
        * eliminatedCandidate1 di Set.of, maka assertThat dengan getBirthDate()
        * isEqualTo(LocalDate.parse("1996-08-18")) jadi bener. Dan ketika gua cuman taro entitas
        * eliminatedCandidate2 di Set.of(...) maka nanti assertThan dengan getBirtDate()
        * isEqualTo(LocalDate.parse("1992-07-17")) jadi bener. Dan ketika gua taro kedua
        * entitas tersebut, ternyata dia naronya engga jelas. Jadi nilai nya ganti-ganti terus.
        * Lebih ke engga tau cara kerjanya sih; apakah stacking atau engga.
        */
        when(candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5)).thenReturn(Set.of(eliminatedCandidate1));

        // when
        Set<CandidateEntity> eliminatedCandidates = candidateComponent.getAllEliminatedCandidate();

        // then
        assertEquals(1, eliminatedCandidates.size());
        assertThat(eliminatedCandidates.stream().findFirst().get().getBirthDate()).isEqualTo(LocalDate.parse("1996-08-18"));
        //assertTrue(eliminatedCandidates.stream().anyMatch(candidate -> candidate.getBirthDate() == (LocalDate.parse("1996-08-18"))));
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
