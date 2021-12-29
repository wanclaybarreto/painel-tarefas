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
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;
	
	@Autowired
	private AdministradorRepository administradorRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	public void saveTecnico(Tecnico tecnico) throws ValidationException {
		if ( !validateEmail(tecnico.getEmail(), tecnico.getId()) ) {
			throw new ValidationException("email--Esse e-mail já pertence a outra conta.");
		}
		
		if (tecnico.getId() != null) {
			Tecnico tecnicoDB = tecnicoRepository.findById(tecnico.getId()).orElseThrow();
			tecnico.setSenha(tecnicoDB.getSenha());
		} else {
			tecnico.encryptPassword();
		}
		
		tecnicoRepository.save(tecnico);
	}
	
	public boolean validateEmail(String email, Integer id) {
		Desenvolvedor desenvolvedor = desenvolvedorRepository.findByEmail(email);
		if (desenvolvedor != null) return false;
		
		Administrador administrador = administradorRepository.findByEmail(email);
		if (administrador != null) return false;
		
		Tecnico tecnico = tecnicoRepository.findByEmail(email);
		
		if (tecnico != null) {
			if (id == null) {
				return false;
			}
			
			if (!tecnico.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void deleteTecnico(Integer id) throws ApplicationServiceException {
		Tecnico tecnico = tecnicoRepository.findById(id).orElseThrow();
		Tarefa trf = new Tarefa();
		trf.setTecnico(tecnico);
		
		Example<Tarefa> exTrf = Example.of(trf);
		
		List<Tarefa> tarefas = tarefaRepository.findAll(exTrf);
		
		if (!tarefas.isEmpty()) {
			throw new ApplicationServiceException("Esse técnico possui tarefa(s) relacionada(s), portanto não será possível deletá-lo.");
		} else {
			tecnicoRepository.deleteById(id);
		}
	}
	
}
