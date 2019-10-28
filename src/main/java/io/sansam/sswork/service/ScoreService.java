package io.sansam.sswork.service;

import io.sansam.sswork.dao.ScoreMapper;
import io.sansam.sswork.po.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScoreService {

    @Autowired
    ScoreMapper scoreMapper;

    public List<Score> selectAll() {
        return scoreMapper.selectAll();
    }

    @Transactional
    public int addScore(Score score) {
        int i = scoreMapper.addScore(score);
        System.out.println("i= "+i);
        int j = 1 / 0;
        return i;
    }
}
