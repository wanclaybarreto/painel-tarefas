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

import br.com.wanclaybarreto.paineltarefas.application.service.AdministradorService;
import br.com.wanclaybarreto.paineltarefas.application.service.ApplicationServiceException;
import br.com.wanclaybarreto.paineltarefas.application.service.ClienteService;
import br.com.wanclaybarreto.paineltarefas.application.service.DesenvolvedorService;
import br.com.wanclaybarreto.paineltarefas.application.service.ModuloService;
import br.com.wanclaybarreto.paineltarefas.application.service.SituacaoTarefaService;
import br.com.wanclaybarreto.paineltarefas.application.service.TarefaService;
import br.com.wanclaybarreto.paineltarefas.application.service.TecnicoService;
import br.com.wanclaybarreto.paineltarefas.application.service.ValidationException;
import br.com.wanclaybarreto.paineltarefas.domain.administrador.Administrador;
import br.com.wanclaybarreto.paineltarefas.domain.administrador.AdministradorRepository;
import br.com.wanclaybarreto.paineltarefas.domain.cliente.Cliente;
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

@Controller
@RequestMapping(path = "/administrador")
public class AdministradorController {
	
	@Autowired
	private SituacaoTarefaRepository situacaoTarefaRepository;
	
	@Autowired
	private SituacaoTarefaService situacaoTarefaService;
	
	@Autowired
	private AdministradorRepository administradorRepository;
	
	@Autowired
	private AdministradorService administradorService;
	
	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;
	
	@Autowired
	private DesenvolvedorService desenvolvedorService;
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	@Autowired
	private ModuloService moduloService;
	
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
		Tarefa tarefa = new Tarefa();
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
			
			tarefa = new Tarefa();
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
			
			tarefa = new Tarefa();
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
	
    @GetMapping(path = "/clientes")
	public String clientes(Model model) {
		List<Cliente> clientes = clienteRepository.findAll(Sort.by("nome"));
		model.addAttribute("clientes", clientes);
		
		model.addAttribute("clienteCad", new Cliente());
		model.addAttribute("clienteEd", new Cliente());
		
		return "clientes";
	}
	
	@PostMapping(path = "/clientes/save")
	public String saveCliente(@ModelAttribute("clienteCad") @Valid Cliente cliente, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			clienteService.saveCliente(cliente);
			
			model.addAttribute("msgSucess", "Cliente cadastrado com sucesso!");
			
			model.addAttribute("clienteCad", new Cliente());
			model.addAttribute("clienteEd", new Cliente());
			
		}
		
		model.addAttribute("clienteEd", new Cliente());
		
		List<Cliente> clientes = clienteRepository.findAll(Sort.by("nome"));
		model.addAttribute("clientes", clientes);
		
		return "clientes";
	}
	
	@PostMapping(path = "/clientes/edit")
	public String editCliente(@ModelAttribute("clienteEd") @Valid Cliente cliente, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			clienteService.saveCliente(cliente);
			
			model.addAttribute("msgSucess", "Cliente alterado com sucesso!");
			
			model.addAttribute("clienteCad", new Cliente());
			model.addAttribute("clienteEd", new Cliente());
			
		}
		
		model.addAttribute("clienteCad", new Cliente());
		
		List<Cliente> clientes = clienteRepository.findAll(Sort.by("nome"));
		model.addAttribute("clientes", clientes);
		
		return "clientes";
	}
	
	@PostMapping(path = "/clientes/delete")
	public String deleteCliente(Model model, @RequestParam("id-cliente-del") Integer idClienteDel) {
		try {
			clienteService.deleteCliente(idClienteDel);
		} catch (ApplicationServiceException e) {
			model.addAttribute("msgError", e.getMessage());
		}
		
		List<Cliente> clientes = clienteRepository.findAll(Sort.by("nome"));
		model.addAttribute("clientes", clientes);
		model.addAttribute("clienteCad", new Cliente());
		model.addAttribute("clienteEd", new Cliente());
		
		return "clientes";
	}
	
	@GetMapping(path = "/administradores")
	public String administradores(Model model) {
		List<Administrador> administradores = administradorRepository.findAll(Sort.by("nome"));
		model.addAttribute("administradores", administradores);
		
		model.addAttribute("administradorCad", new Administrador());
		model.addAttribute("administradorEd", new Administrador());
		
		return "administradores";
	}
	
	@PostMapping(path = "/administradores/save")
	public String saveAdministrador(@ModelAttribute("administradorCad") @Valid Administrador administrador, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			try {
				administradorService.saveAdministrador(administrador);
				
				model.addAttribute("msgSucess", "Administrador cadastrado com sucesso!");
				
				model.addAttribute("administradorCad", new Administrador());
				model.addAttribute("administradorEd", new Administrador());
			} catch (ValidationException e) {
				String[] fieldAndMessage = e.getMessage().split("--"); 
				errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
			}
			
		}
		
		model.addAttribute("administradorEd", new Administrador());
		
		List<Administrador> administradores = administradorRepository.findAll(Sort.by("nome"));
		model.addAttribute("administradores", administradores);
		
		return "administradores";
	}
	
	@PostMapping(path = "/administradores/edit")
	public String editAdministrador(@ModelAttribute("administradorEd") @Valid Administrador administrador, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			try {
				if (administrador.getId() == 1) {
					model.addAttribute("msgError", "Não é possível alterar o administrador padrão!");
				} else {
					administradorService.saveAdministrador(administrador);
					
					model.addAttribute("msgSucess", "Administrador alterado com sucesso!");
				}
				
				model.addAttribute("administradorEd", new Administrador());
			} catch (ValidationException e) {
				String[] fieldAndMessage = e.getMessage().split("--"); 
				errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
			}
			
		}
		
		model.addAttribute("administradorCad", new Administrador());
		
		List<Administrador> administradores = administradorRepository.findAll(Sort.by("nome"));
		model.addAttribute("administradores", administradores);
		
		return "administradores";
	}
	
	@PostMapping(path = "/administradores/delete")
	public String deleteAdministrador(Model model, @RequestParam("id-administrador-del") Integer idAdministradorDel) {
		try {
			Administrador administrador = administradorRepository.findById(idAdministradorDel).orElseThrow();
			
			if (administrador.getId() == 1) {
				model.addAttribute("msgError", "Não é possível deletar o administrador padrão!");
			} else {
				administradorService.deleteAdministrador(idAdministradorDel);
			}
		} catch (ApplicationServiceException e) {
			model.addAttribute("msgError", e.getMessage());
		}
		
		List<Administrador> administradores = administradorRepository.findAll(Sort.by("nome"));
		model.addAttribute("administradores", administradores);
		model.addAttribute("administradorCad", new Administrador());
		model.addAttribute("administradorEd", new Administrador());
		
		return "administradores";
	}
	
	@GetMapping(path = "/desenvolvedores")
	public String desenvolvedores(Model model) {
		List<Desenvolvedor> desenvolvedores = desenvolvedorRepository.findAll(Sort.by("nome"));
		model.addAttribute("desenvolvedores", desenvolvedores);
		
		model.addAttribute("desenvolvedorCad", new Desenvolvedor());
		model.addAttribute("desenvolvedorEd", new Desenvolvedor());
		
		return "desenvolvedores";
	}
	
	@PostMapping(path = "/desenvolvedores/save")
	public String saveDesenvolvedor(@ModelAttribute("desenvolvedorCad") @Valid Desenvolvedor desenvolvedor, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			try {
				desenvolvedorService.saveDesenvolvedor(desenvolvedor);
				
				model.addAttribute("msgSucess", "Desenvolvedor cadastrado com sucesso!");
				
				model.addAttribute("desenvolvedorCad", new Desenvolvedor());
				model.addAttribute("desenvolvedorEd", new Desenvolvedor());
			} catch (ValidationException e) {
				String[] fieldAndMessage = e.getMessage().split("--"); 
				errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
			}
			
		}
		
		model.addAttribute("desenvolvedorEd", new Desenvolvedor());
		
		List<Desenvolvedor> desenvolvedores = desenvolvedorRepository.findAll(Sort.by("nome"));
		model.addAttribute("desenvolvedores", desenvolvedores);
		
		return "desenvolvedores";
	}
	
	@PostMapping(path = "/desenvolvedores/edit")
	public String editDesenvolvedor(@ModelAttribute("desenvolvedorEd") @Valid Desenvolvedor desenvolvedor, Errors errors, Model model) {
		if (!errors.hasErrors()) {
			
			try {
				desenvolvedorService.saveDesenvolvedor(desenvolvedor);
				
				model.addAttribute("msgSucess", "Desenvolvedor alterado com sucesso!");
				
				model.addAttribute("desenvolvedorCad", new Desenvolvedor());
				model.addAttribute("desenvolvedorEd", new Desenvolvedor());
			} catch (ValidationException e) {
				String[] fieldAndMessage = e.getMessage().split("--"); 
				errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
			}
			
		}
		
		model.addAttribute("desenvolvedorCad", new Desenvolvedor());
		
		List<Desenvolvedor> desenvolvedores = desenvolvedorRepository.findAll(Sort.by("nome"));
		model.addAttribute("desenvolvedores", desenvolvedores);
		
		return "desenvolvedores";
	}
	
	@PostMapping(path = "/desenvolvedores/delete")
	public String deleteDesenvolvedor(Model model, @RequestParam("id-desenvolvedor-del") Integer idDesenvolvedorDel) {
		try {
			desenvolvedorService.deleteDesenvolvedor(idDesenvolvedorDel);
		} catch (ApplicationServiceException e) {
			model.addAttribute("msgError", e.getMessage());
		}
		
		List<Desenvolvedor> desenvolvedores = desenvolvedorRepository.findAll(Sort.by("nome"));
		model.addAttribute("desenvolvedores", desenvolvedores);
		model.addAttribute("desenvolvedorCad", new Desenvolvedor());
		model.addAttribute("desenvolvedorEd", new Desenvolvedor());
		
		return "desenvolvedores";
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
