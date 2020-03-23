package io.sansam.sswork.dao;

import com.oracle.tools.packager.Log;
import io.sansam.sswork.po.Score;
import io.sansam.sswork.po.Student;
import io.sansam.sswork.service.TestDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

import java.util.List;

@SpringBootTest
@Slf4j
class ScoreMapperTest {

    @Autowired
    TestDataSourceService dataSourceService;

    @Test
    void selectAll() {
        final List<Student> students = dataSourceService.selectAllStudent();

        List<Score> scores = dataSourceService.selectAllScore();
        log.info("scores = {}", scores);
        log.info("students = {}", students);
//        Assertions.assertNotNull(scores);
    }

    @Test
    void addScore() {
        Score score = new Score();
        score.setId(15);
        score.setName("15");
        score.setKecheng("15");
        score.setChengji("15");
        System.out.println(score);
//        int i = dataSourceService.addScore(score);

//        System.out.println(i);

//        Assertions.assertEquals(1, i);

    }

    @Test
    public void muiltyTest() {
        Score score = new Score();
        score.setId(15);
        score.setName("15");
        score.setKecheng("15");
        score.setChengji("15");
        Student student = new Student();
        student.setId(2);
        student.setName("李四");
        student.setActive(true);

        Log.info("student" + student);
        Log.info("score" + score);
        final int i = dataSourceService.addScore(score, student);
        System.out.println("i= " + i);

    }
}