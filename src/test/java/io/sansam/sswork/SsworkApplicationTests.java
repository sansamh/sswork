package io.sansam.sswork;

import io.sansam.sswork.util.SSBeanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class SsworkApplicationTests {

	public static class Person {
		private String gender;

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}
	}

	public static class Student extends Person {
		private String name;
		private String age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}
	}

	@Test
	void contextLoads() {
		final Student student = new Student();
		student.setName("张三");
		student.setGender("男");
		final Map<String, Object> map = SSBeanUtils.convertBean2Map(student, true, true, null);
		System.out.println(map);
	}

}
