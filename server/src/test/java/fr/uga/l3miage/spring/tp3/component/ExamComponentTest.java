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

    @Test
    void getAllByIdTestNotFound(){
        // Given
        when(examRepository.findAllById(anySet())).thenReturn(Collections.emptyList());

        // then - when
        assertThrows(ExamNotFoundException.class, ()->examComponent.getAllById(Set.of(1L,2L)));
    };

    @Test
    void getAllByIdTestFound(){
        // given
        ExamEntity examEntity1 = ExamEntity
                .builder()
                .weight(3)
                .build();

        when(examRepository.findAllById(anySet())).thenReturn(List.of(examEntity1));

        // when - then
        assertDoesNotThrow(()->examComponent.getAllById(Set.of(1L)));
    }
}
