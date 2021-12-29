package br.com.wanclaybarreto.paineltarefas.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import br.com.wanclaybarreto.paineltarefas.domain.tarefa.SituacaoTarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.SituacaoTarefaRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.TarefaRepository;

@Service
public class SituacaoTarefaService {
	
	@Autowired
	private SituacaoTarefaRepository situacaoTarefaRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	public void saveSituacao(SituacaoTarefa situacao) {
		situacaoTarefaRepository.save(situacao);
	}
	
	public void deleteSituacao(Integer id) throws ApplicationServiceException {
		SituacaoTarefa situacao = situacaoTarefaRepository.findById(id).orElseThrow();
		Tarefa trf = new Tarefa();
		trf.setSituacao(situacao);
		
		Example<Tarefa> exTrf = Example.of(trf);
		
		List<Tarefa> tarefas = tarefaRepository.findAll(exTrf);
		
		if (!tarefas.isEmpty()) {
			throw new ApplicationServiceException("Essa situação possui tarefa(s) relacionada(s), portanto não será possível deletá-la.");
		} else {
			situacaoTarefaRepository.deleteById(id);
		}
	}
	
}
