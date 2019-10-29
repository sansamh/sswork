package io.sansam.sswork.dao;

import io.sansam.sswork.po.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * StudentMapper
 * </p>
 *
 * @author houcb
 * @since 2019-10-29 10:07
 */
@Mapper
public interface StudentMapper {

    @Select("SELECT * FROM app.student")
    List<Student> selectAll();

    @Insert("insert into app.student values (#{id}, #{name}, #{active}, #{comment})")
    int addStudent(Student student);
}
