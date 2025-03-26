package kitra.easyweibo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("kitra.easyweibo.dao")
public class EasyWeiboApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyWeiboApplication.class, args);
	}

}
