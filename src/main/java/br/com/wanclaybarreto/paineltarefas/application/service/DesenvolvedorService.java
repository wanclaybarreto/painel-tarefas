package br.com.wanclaybarreto.paineltarefas.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import br.com.wanclaybarreto.paineltarefas.domain.administrador.Administrador;
import br.com.wanclaybarreto.paineltarefas.domain.administrador.AdministradorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.Desenvolvedor;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.DesenvolvedorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.TarefaRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.Tecnico;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.TecnicoRepository;

@Service
public class DesenvolvedorService {
	
	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private AdministradorRepository administradorRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	public void saveDesenvolvedor(Desenvolvedor desenvolvedor) throws ValidationException {
		if ( !validateEmail(desenvolvedor.getEmail(), desenvolvedor.getId()) ) {
			throw new ValidationException("email--Esse e-mail já pertence a outra conta.");
		}
		
		if (desenvolvedor.getId() != null) {
			Desenvolvedor desenvolvedorDB = desenvolvedorRepository.findById(desenvolvedor.getId()).orElseThrow();
			desenvolvedor.setSenha(desenvolvedorDB.getSenha());
		} else {
			desenvolvedor.encryptPassword();
		}
		
		desenvolvedorRepository.save(desenvolvedor);
	}
	
	public boolean validateEmail(String email, Integer id) {
		Tecnico tecnico = tecnicoRepository.findByEmail(email);
		if (tecnico != null) return false;
		
		Administrador administrador = administradorRepository.findByEmail(email);
		if (administrador != null) return false;
		
		Desenvolvedor desenvolvedor = desenvolvedorRepository.findByEmail(email);
		
		if (desenvolvedor != null) {
			if (id == null) {
				return false;
			}
			
			if (!desenvolvedor.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void deleteDesenvolvedor(Integer id) throws ApplicationServiceException {
		Desenvolvedor desenvolvedor = desenvolvedorRepository.findById(id).orElseThrow();
		Tarefa trf = new Tarefa();
		trf.setDesenvolvedor(desenvolvedor);
		
		Example<Tarefa> exTrf = Example.of(trf);
		
		List<Tarefa> tarefas = tarefaRepository.findAll(exTrf);
		
		if (!tarefas.isEmpty()) {
			throw new ApplicationServiceException("Esse desenvolvedor possui tarefa(s) relacionada(s), portanto não será possível deletá-lo.");
		} else {
			desenvolvedorRepository.deleteById(id);
		}
	}
	
}
