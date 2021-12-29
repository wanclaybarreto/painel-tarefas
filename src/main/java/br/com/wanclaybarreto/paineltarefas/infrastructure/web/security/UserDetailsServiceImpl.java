package br.com.wanclaybarreto.paineltarefas.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.wanclaybarreto.paineltarefas.domain.administrador.AdministradorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.DesenvolvedorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.TecnicoRepository;
import br.com.wanclaybarreto.paineltarefas.domain.usuario.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;
	
	@Autowired
	private AdministradorRepository administradorRepository; 
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = tecnicoRepository.findByEmail(username);
		
		if (usuario == null) {
			usuario = desenvolvedorRepository.findByEmail(username);
			
			if (usuario == null) {
				usuario = administradorRepository.findByEmail(username);
				
				if (usuario == null) {
					throw new UsernameNotFoundException(username);
				}
			}
		}
		
		return new LoggedUser(usuario);
		
	}
}
