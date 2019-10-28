package io.sansam.sswork.dao;

import io.sansam.sswork.po.Score;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface ScoreMapper {

    /**
     * 查询所有数据
     * @return
     */
    @Select("SELECT * FROM score")
    List<Score> selectAll();

    @Insert("insert into score values (#{id}, #{name}, #{kecheng}, #{chengji})")
    int addScore(Score score);
}
