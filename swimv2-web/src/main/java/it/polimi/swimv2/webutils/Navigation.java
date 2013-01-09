package it.polimi.swimv2.webutils;

import it.polimi.swimv2.entity.User;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Navigation {
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	
	public Navigation(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public void fwd(String destination) throws IOException, ServletException {
		request.getRequestDispatcher(destination).forward(request, response);
	}

	public void redirect(String destination) throws IOException, ServletException {
		redirect(destination, true);
	}
	
	public void redirect(String destination, boolean isLocal) throws IOException, ServletException {
		String prefix = isLocal ? request.getContextPath() : "";
		response.sendRedirect(prefix + destination);
	}
	
	public User getLoggedUser() {
		return (User) request.getSession().getAttribute("user");
	}
	
	
	public void setLogin(User u) {
		request.getSession().setAttribute("user", u);
	}
	
	public void setLogout() {
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
	}
	
	/* Tiny wrapper around request\response objects... */
	@SuppressWarnings("unchecked")
	public <T> T getParam(String name) {
		return (T) request.getParameter(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		return (T) request.getAttribute(name);
	}
	
	public <T> void setAttribute(String name, T value) {
		request.setAttribute(name,  value);
	}
	
	public AccessRole getRole() {
		HttpSession session = request.getSession();
		if(session == null) {
			return AccessRole.UNREGISTERED;
		} else {
			User u = (User) session.getAttribute("user");
			if(u == null) {
				return AccessRole.UNREGISTERED;
			} else {
				return u.isAdmin() ? AccessRole.ADMIN : AccessRole.USER;
			}
		}
	}
	
	public List<FileItem> getUploadedFiles() throws FileUploadException {
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		@SuppressWarnings("unchecked")
		List<FileItem> items = upload.parseRequest(request);
		return items;
	}
	
}
