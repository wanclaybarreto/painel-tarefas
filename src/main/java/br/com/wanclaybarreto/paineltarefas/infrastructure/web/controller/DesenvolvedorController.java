package br.com.wanclaybarreto.paineltarefas.infrastructure.web.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.wanclaybarreto.paineltarefas.application.service.ApplicationServiceException;
import br.com.wanclaybarreto.paineltarefas.application.service.ModuloService;
import br.com.wanclaybarreto.paineltarefas.application.service.SituacaoTarefaService;
import br.com.wanclaybarreto.paineltarefas.application.service.TarefaService;
import br.com.wanclaybarreto.paineltarefas.application.service.TecnicoService;
import br.com.wanclaybarreto.paineltarefas.application.service.ValidationException;
import br.com.wanclaybarreto.paineltarefas.domain.cliente.ClienteRepository;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.Desenvolvedor;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.DesenvolvedorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.modulo.Modulo;
import br.com.wanclaybarreto.paineltarefas.domain.modulo.ModuloRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.SituacaoTarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.SituacaoTarefaRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.TarefaRepository;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.Tecnico;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.TecnicoRepository;
import br.com.wanclaybarreto.paineltarefas.util.SecurityUtils;

@Controller
@RequestMapping(path = "/desenvolvedor")
public class DesenvolvedorController {
	
	@Autowired
	private SituacaoTarefaRepository situacaoTarefaRepository;
	
	@Autowired
	private SituacaoTarefaService situacaoTarefaService;
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	@Autowired
	private ModuloService moduloService;
	
	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
	private TarefaService tarefaService;
	
	
	
	@GetMapping(path = "/painel-tarefas")
	public String painelTarefas(Model model) {
		List<SituacaoTarefa> situacoesTarefa = situacaoTarefaRepository.findAll(Sort.by("indice"));
		model.addAttribute("situacoesTarefa", situacoesTarefa);
		
		Map<Integer, Tarefa[]> mapTarefaArrays = new HashMap<>();
		
		for (SituacaoTarefa sitTrf : situacoesTarefa) {
			
			Tarefa trf = new Tarefa();
			trf.setSituacao(sitTrf);
			
			Example<Tarefa> exTrf = Example.of(trf);
			
			List<Tarefa> tarefas = tarefaRepository.findAll(exTrf);
			
			mapTarefaArrays.put(sitTrf.getId().intValue(), tarefas.toArray(new Tarefa[0]));
			
		}
		
		model.addAttribute("mapTarefaArrays", mapTarefaArrays);
		
		return "painel-tarefas";
	}
	
	@GetMapping(path = "/tarefas/new")
	public String newTarefa(Model model) {
		Integer desenvolvedorLogadoId = SecurityUtils.loggedDesenvolvedor().getId();
		Desenvolvedor desenvolvedorLogado = desenvolvedorRepository.findById(desenvolvedorLogadoId).orElseThrow();
		
		Tarefa tarefa = new Tarefa();
		tarefa.setDesenvolvedor(desenvolvedorLogado);
		tarefa.setDataCadastro(LocalDate.now());
		
		model.addAttribute("tarefa", tarefa);
		
		ControllerHelper.setEditMode(model, false);
		ControllerHelper.addSituacoesTarefaToRequest(model, situacaoTarefaRepository);
		ControllerHelper.addTecnicosToRequest(model, tecnicoRepository);
		ControllerHelper.addClientesToRequest(model, clienteRepository);
		ControllerHelper.addModulosToRequest(model, moduloRepository);
		ControllerHelper.addDesenvolvedoresToRequest(model, desenvolvedorRepository);
		
		return "tarefas";
	}
	
	@PostMapping(path = "/tarefas/new/save")
	public String saveTarefa(@ModelAttribute("tarefa") @Valid Tarefa tarefa, Errors errors, Model model) {
		
		if (!errors.hasErrors()) {
			
			tarefaService.saveTarefa(tarefa);
			
			model.addAttribute("msg", "Tarefa cadastrada com sucesso!");
			
			Integer desenvolvedorLogadoId = SecurityUtils.loggedDesenvolvedor().getId();
			Desenvolvedor desenvolvedorLogado = desenvolvedorRepository.findById(desenvolvedorLogadoId).orElseThrow();
			
			tarefa = new Tarefa();
			tarefa.setDesenvolvedor(desenvolvedorLogado);
			tarefa.setDataCadastro(LocalDate.now());
			
			model.addAttribute("tarefa", tarefa);
			
		}
		
		ControllerHelper.setEditMode(model, false);
		ControllerHelper.addSituacoesTarefaToRequest(model, situacaoTarefaRepository);
		ControllerHelper.addTecnicosToRequest(model, tecnicoRepository);
		ControllerHelper.addClientesToRequest(model, clienteRepository);
		ControllerHelper.addModulosToRequest(model, moduloRepository);
		ControllerHelper.addDesenvolvedoresToRequest(model, desenvolvedorRepository);
		
		return "tarefas";
		
	}
	
	@GetMapping(path = "/tarefas/edit")
	public String editTarefa(Model model, @RequestParam("tarefa-id") Integer idTarefa) {
		Tarefa tarefa = tarefaRepository.findById(idTarefa).orElseThrow();
		
		model.addAttribute("tarefa", tarefa);
		
		ControllerHelper.setEditMode(model, true);
		ControllerHelper.addSituacoesTarefaToRequest(model, situacaoTarefaRepository);
		ControllerHelper.addTecnicosToRequest(model, tecnicoRepository);
		ControllerHelper.addClientesToRequest(model, clienteRepository);
		ControllerHelper.addModulosToRequest(model, moduloRepository);
		ControllerHelper.addDesenvolvedoresToRequest(model, desenvolvedorRepository);
		
		return "tarefas";
	}
	
	@PostMapping(path = "/tarefas/edit/save")
	public String updateTarefa(@ModelAttribute("tarefa") @Valid Tarefa tarefa, Errors errors, Model model) {
		
		ControllerHelper.setEditMode(model, true);
		
		if (!errors.hasErrors()) {
			
			tarefaService.saveTarefa(tarefa);
			
			model.addAttribute("msg", "Tarefa alterada com sucesso!");
			
			Integer desenvolvedorLogadoId = SecurityUtils.loggedDesenvolvedor().getId();
			Desenvolvedor desenvolvedorLogado = desenvolvedorRepository.findById(desenvolvedorLogadoId).orElseThrow();
			
			tarefa = new Tarefa();
			tarefa.setDesenvolvedor(desenvolvedorLogado);
			tarefa.setDataCadastro(LocalDate.now());
			
			model.addAttribute("tarefa", tarefa);
			
			ControllerHelper.setEditMode(model, false);
			
		}
		
		ControllerHelper.addSituacoesTarefaToRequest(model, situacaoTarefaRepository);
		ControllerHelper.addTecnicosToRequest(model, tecnicoRepository);
		ControllerHelper.addClientesToRequest(model, clienteRepository);
		ControllerHelper.addModulosToRequest(model, moduloRepository);
		ControllerHelper.addDesenvolvedoresToRequest(model, desenvolvedorRepository);
		
		return "tarefas";
		
	}
	
	
	@GetMapping(path = "/situacoes")
	public String situacoes(Model model) {
		List<SituacaoTarefa> situacoes = situacaoTarefaRepository.findAll(Sort.by("indice"));
		model.addAttribute("situacoes", situacoes);
		
		model.addAttribute("situacaoCad", new SituacaoTarefa());
		model.addAttribute("situacaoEd", new SituacaoTarefa());
		
		return "situacoes";
	}
	
	@PostMapping(path = "/situacoes/save")
	public String saveSituacao(@ModelAttribute("situacaoCad") @Valid SituacaoTarefa situacao, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			situacaoTarefaService.saveSituacao(situacao);
			
			model.addAttribute("msgSucess", "Situação cadastrada com sucesso!");
			
			model.addAttribute("situacaoCad", new SituacaoTarefa());
			model.addAttribute("situacaoEd", new SituacaoTarefa());
			
		}
		
		model.addAttribute("situacaoEd", new SituacaoTarefa());
		
		List<SituacaoTarefa> situacoes = situacaoTarefaRepository.findAll(Sort.by("indice"));
		model.addAttribute("situacoes", situacoes);
		
		return "situacoes";
	}
	
	@PostMapping(path = "/situacoes/edit")
	public String editSituacao(@ModelAttribute("situacaoEd") @Valid SituacaoTarefa situacao, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			situacaoTarefaService.saveSituacao(situacao);
			
			model.addAttribute("msgSucess", "Situação alterada com sucesso!");
			
			model.addAttribute("situacaoCad", new SituacaoTarefa());
			model.addAttribute("situacaoEd", new SituacaoTarefa());
			
		}
		
		model.addAttribute("situacaoCad", new SituacaoTarefa());
		
		List<SituacaoTarefa> situacoes = situacaoTarefaRepository.findAll(Sort.by("indice"));
		model.addAttribute("situacoes", situacoes);
		
		return "situacoes";
	}
	
	@PostMapping(path = "/situacoes/delete")
	public String deleteSituacao(Model model, @RequestParam("id-situacao-del") Integer idSituacaoDel) {
		try {
			situacaoTarefaService.deleteSituacao(idSituacaoDel);
		} catch (ApplicationServiceException e) {
			model.addAttribute("msgError", e.getMessage());
		}
		
		List<SituacaoTarefa> situacoes = situacaoTarefaRepository.findAll(Sort.by("indice"));
		model.addAttribute("situacoes", situacoes);
		model.addAttribute("situacaoCad", new SituacaoTarefa());
		model.addAttribute("situacaoEd", new SituacaoTarefa());
		
		return "situacoes";
	}
	
	@GetMapping(path = "/modulos")
	public String modulos(Model model) {
		List<Modulo> modulos = moduloRepository.findAll(Sort.by("nome"));
		model.addAttribute("modulos", modulos);
		
		model.addAttribute("moduloCad", new Modulo());
		model.addAttribute("moduloEd", new Modulo());
		
		return "modulos";
	}
	
	@PostMapping(path = "/modulos/save")
	public String saveModulo(@ModelAttribute("moduloCad") @Valid Modulo modulo, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			moduloService.saveModulo(modulo);
			
			model.addAttribute("msgSucess", "Módulo cadastrado com sucesso!");
			
			model.addAttribute("moduloCad", new Modulo());
			model.addAttribute("moduloEd", new Modulo());
			
		}
		
		model.addAttribute("moduloEd", new Modulo());
		
		List<Modulo> modulos = moduloRepository.findAll(Sort.by("nome"));
		model.addAttribute("modulos", modulos);
		
		return "modulos";
	}
	
	@PostMapping(path = "/modulos/edit")
	public String editModulo(@ModelAttribute("moduloEd") @Valid Modulo modulo, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			moduloService.saveModulo(modulo);
			
			model.addAttribute("msgSucess", "Módulo alterado com sucesso!");
			
			model.addAttribute("moduloCad", new Modulo());
			model.addAttribute("moduloEd", new Modulo());
			
		}
		
		model.addAttribute("moduloCad", new Modulo());
		
		List<Modulo> modulos = moduloRepository.findAll(Sort.by("nome"));
		model.addAttribute("modulos", modulos);
		
		return "modulos";
	}
	
	@PostMapping(path = "/modulos/delete")
	public String deleteModulo(Model model, @RequestParam("id-modulo-del") Integer idModuloDel) {
		try {
			moduloService.deleteModulo(idModuloDel);
		} catch (ApplicationServiceException e) {
			model.addAttribute("msgError", e.getMessage());
		}
		
		List<Modulo> modulos = moduloRepository.findAll(Sort.by("nome"));
		model.addAttribute("modulos", modulos);
		model.addAttribute("moduloCad", new Modulo());
		model.addAttribute("moduloEd", new Modulo());
		
		return "modulos";
	}
	
	@GetMapping(path = "/tecnicos")
	public String tecnicos(Model model) {
		List<Tecnico> tecnicos = tecnicoRepository.findAll(Sort.by("nome"));
		model.addAttribute("tecnicos", tecnicos);
		
		model.addAttribute("tecnicoCad", new Tecnico());
		model.addAttribute("tecnicoEd", new Tecnico());
		
		return "tecnicos";
	}
	
	@PostMapping(path = "/tecnicos/save")
	public String saveTecnico(@ModelAttribute("tecnicoCad") @Valid Tecnico tecnico, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			try {
				tecnicoService.saveTecnico(tecnico);
				
				model.addAttribute("msgSucess", "Técnico cadastrado com sucesso!");
				
				model.addAttribute("tecnicoCad", new Tecnico());
				model.addAttribute("tecnicoEd", new Tecnico());
			} catch (ValidationException e) {
				String[] fieldAndMessage = e.getMessage().split("--"); 
				errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
			}
			
		}
		
		model.addAttribute("tecnicoEd", new Tecnico());
		
		List<Tecnico> tecnicos = tecnicoRepository.findAll(Sort.by("nome"));
		model.addAttribute("tecnicos", tecnicos);
		
		return "tecnicos";
	}
	
	@PostMapping(path = "/tecnicos/edit")
	public String editTecnico(@ModelAttribute("tecnicoEd") @Valid Tecnico tecnico, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			try {
				tecnicoService.saveTecnico(tecnico);
				
				model.addAttribute("msgSucess", "Técnico alterado com sucesso!");
				
				model.addAttribute("tecnicoCad", new Tecnico());
				model.addAttribute("tecnicoEd", new Tecnico());
			} catch (ValidationException e) {
				String[] fieldAndMessage = e.getMessage().split("--"); 
				errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
			}
			
		}
		
		model.addAttribute("tecnicoCad", new Tecnico());
		
		List<Tecnico> tecnicos = tecnicoRepository.findAll(Sort.by("nome"));
		model.addAttribute("tecnicos", tecnicos);
		
		return "tecnicos";
	}
	
	@PostMapping(path = "/tecnicos/delete")
	public String deleteTecnico(Model model, @RequestParam("id-tecnico-del") Integer idTecnicoDel) {
		try {
			tecnicoService.deleteTecnico(idTecnicoDel);
		} catch (ApplicationServiceException e) {
			model.addAttribute("msgError", e.getMessage());
		}
		
		List<Tecnico> tecnicos = tecnicoRepository.findAll(Sort.by("nome"));
		model.addAttribute("tecnicos", tecnicos);
		model.addAttribute("tecnicoCad", new Tecnico());
		model.addAttribute("tecnicoEd", new Tecnico());
		
		return "tecnicos";
	}
	
	@GetMapping(path = "/relatorios")
	public String relatorio(Model model) {
		
		ControllerHelper.setEditMode(model, false);
		ControllerHelper.addSituacoesTarefaToRequest(model, situacaoTarefaRepository);
		ControllerHelper.addClientesToRequest(model, clienteRepository);
		ControllerHelper.addModulosToRequest(model, moduloRepository);
		ControllerHelper.addTecnicosToRequest(model, tecnicoRepository);
		ControllerHelper.addDesenvolvedoresToRequest(model, desenvolvedorRepository);
		
		return "relatorios";
		
	}
	
	@GetMapping(path = "/relatorios/create")
	public String createRelatorio(Model model,
								@RequestParam("id-situacao") Integer idSituacao,
								@RequestParam("id-tecnico") Integer idTecnico,
								@RequestParam("id-desenvolvedor") Integer idDesenvolvedor,
								@RequestParam("id-cliente") Integer idCliente,
								@RequestParam("id-modulo") Integer idModulo,
								@RequestParam("data-desc") Integer dataDesc,
								@RequestParam("data-in") String dataIn,
								@RequestParam("data-fin") String dataFin) {
		
		Tarefa tarefa = new Tarefa();
		
		situacaoTarefaRepository.findById(idSituacao).ifPresent((s) -> tarefa.setSituacao(s));
		tecnicoRepository.findById(idTecnico).ifPresent((t) -> tarefa.setTecnico(t));
		desenvolvedorRepository.findById(idDesenvolvedor).ifPresent((d) -> tarefa.setDesenvolvedor(d));
		clienteRepository.findById(idCliente).ifPresent((c) -> tarefa.setCliente(c));
		moduloRepository.findById(idModulo).ifPresent((m) -> tarefa.setModulo(m));
		
		Example<Tarefa> exTrf = Example.of(tarefa);
		
		List<Tarefa> tarefas = tarefaRepository.findAll(exTrf);
		
		if (dataDesc == 1) {
			
			tarefas = tarefas.stream()
							 .filter(t -> Duration.between(t.getDataCadastro() != null
							 			      			       ? LocalDateTime.parse(t.getDataCadastro().toString() + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))
							 			      			       : LocalDateTime.parse("0001-01-01 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H")),
							 			      			   LocalDateTime.parse(dataIn + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))).toDays() <= 0
							 		  && Duration.between(t.getDataCadastro() != null
			 			      			       			      ? LocalDateTime.parse(t.getDataCadastro().toString() + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))
			 			      			       			      : LocalDateTime.parse("0001-01-01 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H")),
			 			      			       			  LocalDateTime.parse(dataFin + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))).toDays() >= 0)
							 .collect(Collectors.toList());
			
		} else if (dataDesc == 2) {
			
			tarefas = tarefas.stream()
							 .filter(t -> Duration.between(t.getDataPrevisaoFinalizacao() != null
							 			      			       ? LocalDateTime.parse(t.getDataPrevisaoFinalizacao().toString() + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))
							 			      			       : LocalDateTime.parse("0001-01-01 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H")),
							 			      			   LocalDateTime.parse(dataIn + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))).toDays() <= 0
							 		  && Duration.between(t.getDataPrevisaoFinalizacao() != null
			 			      			       			      ? LocalDateTime.parse(t.getDataPrevisaoFinalizacao().toString() + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))
			 			      			       			      : LocalDateTime.parse("0001-01-01 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H")),
			 			      			       			  LocalDateTime.parse(dataFin + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))).toDays() >= 0)
							 .collect(Collectors.toList());
			
		} else if (dataDesc == 3) {
			
			tarefas = tarefas.stream()
							 .filter(t -> Duration.between(t.getDataFinalizacao() != null
							 			      			       ? LocalDateTime.parse(t.getDataFinalizacao().toString() + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))
							 			      			       : LocalDateTime.parse("0001-01-01 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H")),
							 			      			   LocalDateTime.parse(dataIn + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))).toDays() <= 0
							 		  && Duration.between(t.getDataFinalizacao() != null
			 			      			       			      ? LocalDateTime.parse(t.getDataFinalizacao().toString() + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))
			 			      			       			      : LocalDateTime.parse("0001-01-01 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H")),
			 			      			       			  LocalDateTime.parse(dataFin + " 0", DateTimeFormatter.ofPattern("yyyy-MM-dd H"))).toDays() >= 0)
							 .collect(Collectors.toList());
			
		}
		
		tarefaService.generateReport(null, tarefas);
		
		ControllerHelper.setEditMode(model, true);
		ControllerHelper.addSituacoesTarefaToRequest(model, situacaoTarefaRepository);
		ControllerHelper.addClientesToRequest(model, clienteRepository);
		ControllerHelper.addModulosToRequest(model, moduloRepository);
		ControllerHelper.addTecnicosToRequest(model, tecnicoRepository);
		ControllerHelper.addDesenvolvedoresToRequest(model, desenvolvedorRepository);
		
		return "relatorios";
		
	}

}
