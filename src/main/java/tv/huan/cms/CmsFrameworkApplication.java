package tv.huan.cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("tv.huan.cms.mapper")
@ServletComponentScan
public class CmsFrameworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsFrameworkApplication.class, args);
	}
}
