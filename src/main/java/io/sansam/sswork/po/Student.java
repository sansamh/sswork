package io.sansam.sswork.po;

import lombok.Data;

/**
 * <p>
 * Student
 * </p>
 *
 * @author houcb
 * @since 2019-10-29 10:07
 */
@Data
public class Student {

    private long id;
    private String name;
    private boolean active;
    private String comment;
}
