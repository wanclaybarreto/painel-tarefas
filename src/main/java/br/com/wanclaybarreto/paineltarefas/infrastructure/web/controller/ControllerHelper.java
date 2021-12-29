package br.com.wanclaybarreto.paineltarefas.infrastructure.web.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import br.com.wanclaybarreto.paineltarefas.domain.cliente.Cliente;
import br.com.wanclaybarreto.paineltarefas.domain.cliente.ClienteRepository;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.Desenvolvedor;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.DesenvolvedorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.modulo.Modulo;
import br.com.wanclaybarreto.paineltarefas.domain.modulo.ModuloRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.SituacaoTarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.SituacaoTarefaRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.Tecnico;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.TecnicoRepository;

public class ControllerHelper {

	public static void setEditMode(Model model, boolean isEdit) {
		model.addAttribute("editMode", isEdit);
	}
	
	public static void addSituacoesTarefaToRequest(Model model, SituacaoTarefaRepository repository) {
		List<SituacaoTarefa> situacoesTarefa = repository.findAll(Sort.by("id"));
		model.addAttribute("situacoesTarefa", situacoesTarefa);
	}
	
	public static void addTecnicosToRequest(Model model, TecnicoRepository repository) {
		List<Tecnico> tecnicos = repository.findAll(Sort.by("nome"));
		tecnicos.forEach((t) -> t.setSenha(null));
		model.addAttribute("tecnicos", tecnicos);
	}
	
	public static void addClientesToRequest(Model model, ClienteRepository repository) {
		List<Cliente> clientes = repository.findAll(Sort.by("nome"));
		model.addAttribute("clientes", clientes);
	}
	
	public static void addModulosToRequest(Model model, ModuloRepository repository) {
		List<Modulo> modulos = repository.findAll(Sort.by("nome"));
		model.addAttribute("modulos", modulos);
	}
	
	public static void addDesenvolvedoresToRequest(Model model, DesenvolvedorRepository repository) {
		List<Desenvolvedor> desenvolvedores = repository.findAll(Sort.by("nome"));
		desenvolvedores.forEach((d) -> d.setSenha(null));
		model.addAttribute("desenvolvedores", desenvolvedores);
	}
	
}
