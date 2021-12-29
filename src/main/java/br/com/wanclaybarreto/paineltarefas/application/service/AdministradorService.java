package br.com.wanclaybarreto.paineltarefas.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wanclaybarreto.paineltarefas.domain.administrador.Administrador;
import br.com.wanclaybarreto.paineltarefas.domain.administrador.AdministradorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.Desenvolvedor;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.DesenvolvedorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.Tecnico;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.TecnicoRepository;

@Service
public class AdministradorService {
	
	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private AdministradorRepository administradorRepository;
	
	public void saveAdministrador(Administrador administrador) throws ValidationException {
		if ( !validateEmail(administrador.getEmail(), administrador.getId()) ) {
			throw new ValidationException("email--Esse e-mail já pertence a outra conta.");
		}
		
		if (administrador.getId() != null) {
			Administrador administradorDB = administradorRepository.findById(administrador.getId()).orElseThrow();
			administrador.setSenha(administradorDB.getSenha());
		} else {
			administrador.encryptPassword();
		}
		
		administradorRepository.save(administrador);
	}
	
	public boolean validateEmail(String email, Integer id) {
		Tecnico tecnico = tecnicoRepository.findByEmail(email);
		if (tecnico != null) return false;
		
		Desenvolvedor desenvolvedor = desenvolvedorRepository.findByEmail(email);
		if (desenvolvedor != null) return false;
		
		Administrador administrador = administradorRepository.findByEmail(email);
		
		if (administrador != null) {
			if (id == null) {
				return false;
			}
			
			if (!administrador.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void deleteAdministrador(Integer id) throws ApplicationServiceException {
		
		administradorRepository.deleteById(id);
		
	}
	
}
