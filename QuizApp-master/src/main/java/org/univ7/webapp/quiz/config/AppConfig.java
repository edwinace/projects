package org.univ7.webapp.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;

@Configuration
@Import({ ApplicationSecurityConfig.class, PersistenceConfig.class })
@ComponentScan(
		basePackages = {
				"org.univ7.webapp.quiz",
		},
		excludeFilters={
				@Filter(type = FilterType.ANNOTATION, value = Configuration.class)
		}
)
public class AppConfig extends WebMvcConfigurerAdapter{

	@Bean
	public ViewResolver handlebarsViewResolver() {
		HandlebarsViewResolver viewResolver = new HandlebarsViewResolver();
		viewResolver.setOrder(1);
		viewResolver.setPrefix("/WEB-INF/templates/");
		viewResolver.setSuffix(".hbs");
		viewResolver.setContentType("text/html; charset=UTF-8");
		// viewResolver.setCache(true);
		return viewResolver;
	}
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/bc/**").addResourceLocations("/bower_components/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/img/**").addResourceLocations("/img/");
		registry.addResourceHandler("/svg/**").addResourceLocations("/svg/");
//		registry.addResourceHandler("/**").setCachePeriod(86400);
	}
}