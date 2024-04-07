package fr.uga.l3miage.spring.tp3.component;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ExamComponentTest {
    @Autowired
    private ExamComponent examComponent;

    @MockBean
    private ExamRepository examRepository;

    /*
    * Jadi kalo dari yang gua liat, kalo misalnya untuk komponen itu kita mau ngecek dia ketemu (Found), atau engga
    * ketemu (NotFound). Atau dia itu bener ngeluarin exception kalo ga ketemu dan ngebalikin sesuatu kalo nemu.
    * Nah untuk itu, pertama-tama kita liat fungsi yang mau kita cek dulu. Biasanya kita ngecek
    * antara repository dan component nya.
    */

    /*
    * Jadi kita konfigurasi repository seperti yang kita mau, misalnya kalo
    * untuk input apa aja, si repository harus ngeluarin list kosong. Nah implementasi dari kalimat yang barusan itu
    * ada di bagian yang when(.....).
    * Setelah itu kita ngecek si component (entitas yang memang mau kita coba). Kita ngecek, untuk input yang engga
    * ditemuin, dia akan mengembalikkan exception.class. Implementasi nya yaitu ada di assertThrows(...)
    */
    @Test
    void getAllByIdTestNotFound(){
        // Given
        when(examRepository.findAllById(anySet())).thenReturn(Collections.emptyList());

        // then - when
        assertThrows(ExamNotFoundException.class, ()->examComponent.getAllById(Set.of(1L,2L)));
    };


    /*
    * Nah buat test fungsi yang Found, kita bikin sebuah entity yang nanti berguna untuk ngetest repository. Jadi
    * maksudnya nanti, apa aja inputnya, si repository harus ngembaliin sesuatu, yaitu si entitas yang kita buat tersebut.
    *
    * Trus nanti buat si component, apapun inputnya, dia nanti ga bakal balikkin exception.
    */
    @Test
    void getAllByIdTestFound(){
        // given
        ExamEntity examEntity1 = ExamEntity
                .builder()
                .weight(3)
                .build();

        when(examRepository.findAllById(anySet())).thenReturn(List.of(examEntity1));

        /*
        * Biar test nya passed, ukuran dari yang when diatas komentar ini dan assertDoesNotThrow dibawah komentar ini
        * harus sama. Maksudnya UKURAN list of ExamEntity dari examRepository (List.of(examEntity1)) harus sama dengan
        * set of ExamEntity dari examComponent (Set.of(1L)). Nah ternyata si fungsi getAllById() itu ngecek ukuran dari
        * set nya aja engga elemen-elemen nya juga.
        */

        // when - then
        assertDoesNotThrow(()->examComponent.getAllById(Set.of(1L)));
    }
}
