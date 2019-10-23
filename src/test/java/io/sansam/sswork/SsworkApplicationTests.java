package io.sansam.sswork;

import io.sansam.sswork.common.constant.CommonConstant;
import io.sansam.sswork.common.lock.DistributedLock;
import io.sansam.sswork.util.SSBeanUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
class SsworkApplicationTests {

    @Autowired
    DistributedLock distributedLock;


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

//	@Test
//    public void lockTest() {
//        Jedis jedis = new Jedis();
//        Mockito.when(jedis.set(Mockito.anyString(), Mockito.anyString(), new SetParams())).thenReturn("OK");
//
//        String key = UUID.randomUUID().toString();
//        String val = "value";
//        long time = 10000L;
//        final String lock = distributedLock.lock(key, val, time, time);
//        System.out.println("lockTest -- " + lock);
//    }

}
