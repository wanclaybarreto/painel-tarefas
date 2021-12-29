package br.com.wanclaybarreto.paineltarefas.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.wanclaybarreto.paineltarefas.domain.administrador.Administrador;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.Desenvolvedor;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.Tecnico;
import br.com.wanclaybarreto.paineltarefas.infrastructure.web.security.LoggedUser;

public class SecurityUtils {
	
	public static LoggedUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		return (LoggedUser) authentication.getPrincipal();
	}
	
	public static Tecnico loggedTecnico() {
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			throw new IllegalStateException("Não existe um usuário logado!");
		}
		
		if (!(loggedUser.getUsuario() instanceof Tecnico)) {
			throw new IllegalStateException("O usuário logado não é um técnico!");
		}
		
		return (Tecnico) loggedUser.getUsuario();
	}
	
	public static Desenvolvedor loggedDesenvolvedor() {
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			throw new IllegalStateException("Não existe um usuário logado!");
		}
		
		if (!(loggedUser.getUsuario() instanceof Desenvolvedor)) {
			throw new IllegalStateException("O usuário logado não é um desenvolvedor!");
		}
		
		return (Desenvolvedor) loggedUser.getUsuario();
	}
	
	public static Administrador loggedAdministrador() {
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			throw new IllegalStateException("Não existe um usuário logado!");
		}
		
		if (!(loggedUser.getUsuario() instanceof Administrador)) {
			throw new IllegalStateException("O usuário logado não é um administrador!");
		}
		
		return (Administrador) loggedUser.getUsuario();
	}
	
}
