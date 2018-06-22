package com.optimal.kdm.manage.server;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.optimal.kdm.common.data.factory.BaseRepositoryFactoryBean;

@EnableJpaRepositories(basePackages = {
		"com.optimal.kdm.module.manage.biz.factory" }, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class// 指定自己的工厂类
)
@SpringBootApplication
public class ManageServerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ManageServerApplication.class).web(WebApplicationType.SERVLET).run(args);
	}

}
