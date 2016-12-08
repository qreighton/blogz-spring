package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		String verify_error = "passwords must match";
		String username_error = "must be 4 to 11 characters";
		String password_error = "must be 6 to 20 characters";
		int x = 3;
		if (password.equals(verify)){
			verify_error = "";
			x = x - 1;
		}
		if (User.isValidPassword(password)){
			password_error = "";
			x = x - 1;
		}
		if (User.isValidUsername(username)){
			username_error = "";
			x = x - 1;
		}
		if (x > 0){
			model.addAttribute("verify_error",verify_error);
			model.addAttribute("username_error",username_error);
			model.addAttribute("password_error",password_error);
			model.addAttribute("username",username);
			return "signup";}
		
		User user = new User(username,password);
		userDao.save(user);
		HttpSession thisSession = request.getSession();
		
		setUserInSession(thisSession,user);
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username =="" || password ==""){
			String error = "you have to enter a name and password";
			model.addAttribute("error",error);
			model.addAttribute("username",username);
			return "login";
		}
		User checkuser = userDao.findByUsername(username);
		if (checkuser == null){
			String error = "no such user on file";
			model.addAttribute("error",error);
			model.addAttribute("username",username);
			return "login";
		}
		if(checkuser.isMatchingPassword(password)== false){
			String error = "password doesnt match user";
			model.addAttribute("error",error);
			model.addAttribute("username",username);
			return "login";
		}
		HttpSession thisSession = request.getSession();
		
		setUserInSession(thisSession,checkuser);
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
