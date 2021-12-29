package br.com.wanclaybarreto.paineltarefas.infrastructure.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.com.wanclaybarreto.paineltarefas.util.SecurityUtils;

public class AuthenticationSucessHandlerImpl implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Role role = SecurityUtils.loggedUser().getRole();
		
		if (role == Role.TECNICO) {
			response.sendRedirect("tecnico/painel-tarefas");
		} else if (role == Role.DESENVOLVEDOR) {
			response.sendRedirect("desenvolvedor/painel-tarefas");
		} else if (role == Role.ADMINISTRADOR) {
			response.sendRedirect("administrador/painel-tarefas");
		} else {
			throw new IllegalStateException("Erro de autenticação!");
		}
		
	}

}
