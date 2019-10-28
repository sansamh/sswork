package io.sansam.sswork.dao;

import io.sansam.sswork.po.Score;
import io.sansam.sswork.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreMapperTest {

    @Autowired
    ScoreService scoreService;

    @Test
    void selectAll() {
        List<Score> scores = scoreService.selectAll();
        System.out.println(scores);
    }

    @Test
    void addScore() {
        Score score = new Score();
        score.setId(14);
        score.setName("14");
        score.setKecheng("14");
        score.setChengji("14");
        System.out.println(score);
        int i = scoreService.addScore(score);

        System.out.println(i);

    }
}