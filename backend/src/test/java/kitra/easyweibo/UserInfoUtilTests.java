package kitra.easyweibo;

import kitra.easyweibo.util.UserInfoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserInfoUtilTests {
	@Test
	public void testUserNameValidator() {
		Assertions.assertTrue(UserInfoUtil.validateUsername("admin"));
		Assertions.assertTrue(UserInfoUtil.validateUsername("kitra"));
		Assertions.assertTrue(UserInfoUtil.validateUsername("123456"));
		Assertions.assertTrue(UserInfoUtil.validateUsername("9vBeDVCD"));
		Assertions.assertTrue(UserInfoUtil.validateUsername("965dbdefe8a5sf"));
		Assertions.assertTrue(UserInfoUtil.validateUsername("96___"));

		Assertions.assertFalse(UserInfoUtil.validateUsername(""));
		Assertions.assertFalse(UserInfoUtil.validateNickname(" "));
		Assertions.assertFalse(UserInfoUtil.validateUsername("  \t"));
		Assertions.assertFalse(UserInfoUtil.validateUsername("  \n  "));
		Assertions.assertFalse(UserInfoUtil.validateUsername("123-a"));
		Assertions.assertFalse(UserInfoUtil.validateUsername("1中文2"));
		Assertions.assertFalse(UserInfoUtil.validateUsername("123456 "));
		// 25字符
		Assertions.assertFalse(UserInfoUtil.validateUsername("1132135415241524152315241"));
	}

	@Test
	public void testNicknameValidator() {
		Assertions.assertTrue(UserInfoUtil.validateNickname("admin"));
		Assertions.assertTrue(UserInfoUtil.validateNickname("草木升华"));
		// 64字符
		Assertions.assertTrue(UserInfoUtil.validateNickname("特辣的海藻~特辣的海藻~特辣的海藻~11   36655447788996655441223112233665544778899"));

		Assertions.assertFalse(UserInfoUtil.validateNickname(""));
		Assertions.assertFalse(UserInfoUtil.validateNickname(" "));
		Assertions.assertFalse(UserInfoUtil.validateUsername("  \t"));
		Assertions.assertFalse(UserInfoUtil.validateUsername("  \n  "));
		// 65字符
		Assertions.assertFalse(UserInfoUtil.validateNickname("特辣的海藻~特辣的海藻~特辣的海藻~11   366554477889966554412231122336655447788991"));
	}

	@Test
	public void testPasswordValidator() {
		Assertions.assertTrue(UserInfoUtil.validatePassword("12345678"));
		Assertions.assertTrue(UserInfoUtil.validatePassword("6++@#$%^^&!]=+\\/.<"));

		Assertions.assertFalse(UserInfoUtil.validatePassword("abc"));
		Assertions.assertFalse(UserInfoUtil.validatePassword("111111111111中文"));
		Assertions.assertFalse(UserInfoUtil.validatePassword("1 588888999992"));
		Assertions.assertFalse(UserInfoUtil.validatePassword("1\n588888999992"));
		Assertions.assertFalse(UserInfoUtil.validatePassword("1\n588888999992"));
		// 33字符
		Assertions.assertFalse(UserInfoUtil.validatePassword("158888899999215888889999921236000"));
	}
}
