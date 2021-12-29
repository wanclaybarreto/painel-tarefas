package br.com.wanclaybarreto.paineltarefas.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import br.com.wanclaybarreto.paineltarefas.domain.modulo.Modulo;
import br.com.wanclaybarreto.paineltarefas.domain.modulo.ModuloRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.TarefaRepository;

@Service
public class ModuloService {
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	public void saveModulo(Modulo modulo) {
		moduloRepository.save(modulo);
	}
	
	public void deleteModulo(Integer id) throws ApplicationServiceException {
		Modulo modulo = moduloRepository.findById(id).orElseThrow();
		Tarefa trf = new Tarefa();
		trf.setModulo(modulo);
		
		Example<Tarefa> exTrf = Example.of(trf);
		
		List<Tarefa> tarefas = tarefaRepository.findAll(exTrf);
		
		if (!tarefas.isEmpty()) {
			throw new ApplicationServiceException("Esse módulo possui tarefa(s) relacionada(s), portanto não será possível deletá-lo.");
		} else {
			moduloRepository.deleteById(id);
		}
	}
	
}
