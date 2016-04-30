package org.univ7.webapp.quiz.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.univ7.webapp.quiz.filter.JwtRememberMeFilter;

// Web.xml
public class WebInitializer implements WebApplicationInitializer {

	private final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
	
	private static final String ALL = "/*";
	private static final String PRIVATE_API = "/api/private/*";
	private static final String ROOT = "/";
	private static final String DISPATCHER = "dispatcher";
	private static final String UTF8 = "UTF-8";
	private static final String TRUE = "true";
	
	@Override
	public void onStartup(ServletContext servletContext) {
		
		rootContext.register(AppConfig.class);
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		// spring application context의 dispatcher servlet
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(AppConfig.class);

		// dispatcher servlet 등록
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(DISPATCHER, new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(ROOT);
		dispatcher.setInitParameter("throwExceptionIfNoHandlerFound", TRUE);
		
		// filter
		FilterRegistration charEncodingfilterReg = servletContext.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
		charEncodingfilterReg.setInitParameter("encoding", UTF8);
		charEncodingfilterReg.setInitParameter("forceEncoding", TRUE);
		charEncodingfilterReg.addMappingForUrlPatterns(null, false, ALL);
		
//		FilterRegistration springSecurityFilterReg = servletContext.addFilter("JwtRememberMeFilter", JwtRememberMeFilter.class);
//		springSecurityFilterReg.addMappingForUrlPatterns(null, false, PRIVATE_API);
	}
}