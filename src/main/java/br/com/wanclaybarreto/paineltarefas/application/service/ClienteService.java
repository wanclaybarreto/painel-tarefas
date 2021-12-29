package br.com.wanclaybarreto.paineltarefas.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import br.com.wanclaybarreto.paineltarefas.domain.cliente.Cliente;
import br.com.wanclaybarreto.paineltarefas.domain.cliente.ClienteRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.TarefaRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	public void saveCliente(Cliente cliente) {
		clienteRepository.save(cliente);
	}
	
	public void deleteCliente(Integer id) throws ApplicationServiceException {
		Cliente cliente = clienteRepository.findById(id).orElseThrow();
		Tarefa trf = new Tarefa();
		trf.setCliente(cliente);
		
		Example<Tarefa> exTrf = Example.of(trf);
		
		List<Tarefa> tarefas = tarefaRepository.findAll(exTrf);
		
		if (!tarefas.isEmpty()) {
			throw new ApplicationServiceException("Esse cliente possui tarefa(s) relacionada(s), portanto não será possível deletá-lo.");
		} else {
			clienteRepository.deleteById(id);
		}
	}
	
}
