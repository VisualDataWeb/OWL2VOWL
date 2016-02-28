package de.uni_stuttgart.vis.vowl.owl2vowl.server;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;

@Component
public class ContextPath implements ServletContextAware {

	private static String contextPath;

	public static String getContextPath() {
		return contextPath;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		ContextPath.contextPath = servletContext.getRealPath(File.separator) + File.separator;
	}
}
