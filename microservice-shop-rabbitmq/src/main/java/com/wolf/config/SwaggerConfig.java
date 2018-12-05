package com.wolf.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig
{

	@Bean
	public Docket api()
	{
		return new Docket(DocumentationType.SWAGGER_2).pathMapping("/").select()
				.apis(RequestHandlerSelectors.basePackage("com.wolf.controller"))
				.build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo()
	{
		@SuppressWarnings("deprecation")
		ApiInfo apiInfo = new ApiInfo("RabbotMQ服务管理",               		//大标题
				"EMR Platform's REST API",                        		//小标题
				"1.0",                                             			//版本
				"NO terms of service", "医养开发部",                  //作者
				"The Apache License, Version 2.0",                 			//链接显示文字
				"http://www.apache.org/licenses/LICENSE-2.0.html"  		//网站链接
		);
		return apiInfo;
	}

}