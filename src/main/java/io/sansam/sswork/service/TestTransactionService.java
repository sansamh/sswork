package io.sansam.sswork.service;

import io.sansam.sswork.dao.ScoreMapper;
import io.sansam.sswork.dao.StudentMapper;
import io.sansam.sswork.po.Score;
import io.sansam.sswork.po.Student;
import io.sansam.sswork.util.TransactionDebugUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TestTransactionService {

    @Resource
    ScoreMapper scoreMapper;

    @Resource
    StudentMapper studentMapper;

    public List<Score> selectAllScore() {
        return scoreMapper.selectAll();
    }

    public List<Student> selectAllStudent() {
        return studentMapper.selectAll();
    }

//    @Transactional(rollbackFor = Exception.class)
    public int addScore(Score score, Student student) {
        TransactionDebugUtil.transactionRequired("addScore");
        int i = scoreMapper.addScore(score);
        log.info("sco i= "+i);
        i = i + addStudent(student);

        int j = 1/ 0;
        return i;
    }

    @Transactional(rollbackFor = Exception.class, propagation =
            Propagation.REQUIRES_NEW)
    public int addStudent(Student student) {
        TransactionDebugUtil.transactionRequired("addStudent");

        final int i = studentMapper.addStudent(student);
        log.info("stu i= "+i);

        return i;
    }
}
