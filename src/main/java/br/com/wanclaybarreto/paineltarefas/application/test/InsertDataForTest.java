package br.com.wanclaybarreto.paineltarefas.application.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.Tecnico;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.TecnicoRepository;

@Component
public class InsertDataForTest {
	
	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ModuloRepository moduloRepository;
	
	@Autowired
	private SituacaoTarefaRepository situacaoTarefaRepository;
	
	@Autowired
	private AdministradorRepository administradorRepository;
	
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		desenvolvedores();
		tecnicos();
		clientes();
		modulos();
		situacoes();

		administrador();
		
	}
	
	public void desenvolvedores() {
		
		Desenvolvedor d1 = new Desenvolvedor();
		d1.setNome("Desenvolvedor 01");
		d1.setEmail("desenvolvedor01@email.com");
		d1.setSenha("desenvolvedor01");
		d1.encryptPassword();
		
		desenvolvedorRepository.save(d1);
		
		Desenvolvedor d2 = new Desenvolvedor();
		
		d2.setNome("Desenvolvedor 02");
		d2.setEmail("desenvolvedor02@email.com");
		d2.setSenha("desenvolvedor02");
		d2.encryptPassword();
		
		desenvolvedorRepository.save(d2);
		
	}
	
	public void tecnicos() {
		
		Tecnico t1 = new Tecnico();
		t1.setNome("Técnico 01");
		t1.setEmail("tecnico01@email.com");
		t1.setSenha("tecnico01");
		t1.encryptPassword();
		
		tecnicoRepository.save(t1);
		
		Tecnico t2 = new Tecnico();
		
		t2.setNome("Técnico 02");
		t2.setEmail("tecnico02@email.com");
		t2.setSenha("tecnico02");
		t2.encryptPassword();
		
		tecnicoRepository.save(t2);
		
	}
	
	public void clientes() {
		
		Cliente c1 = new Cliente();
		c1.setNome("Cliente 01");
		clienteRepository.save(c1);
		
		Cliente c2 = new Cliente();
		c2.setNome("Cliente 02");
		clienteRepository.save(c2);
		
		Cliente c3 = new Cliente();
		c3.setNome("Cliente 03");
		clienteRepository.save(c3);
		
		Cliente c4 = new Cliente();
		c4.setNome("CLiente 04");
		clienteRepository.save(c4);
		
		Cliente c5 = new Cliente();
		c5.setNome("CLiente 05");
		clienteRepository.save(c5);
		
	}
	
	public void modulos() {
		
		Modulo m1 = new Modulo();
		m1.setNome("Módulo 01");
		moduloRepository.save(m1);
		
	}
	
	
	public void situacoes() {
		
		SituacaoTarefa s1 = new SituacaoTarefa();
		s1.setNome("Situação 01");
		s1.setIndice(1);
		s1.setCor("#FE6B7D");
		situacaoTarefaRepository.save(s1);
		
		SituacaoTarefa s2 = new SituacaoTarefa();
		s2.setNome("Situação 02");
		s2.setIndice(2);
		s2.setCor("#FDAA22");
		situacaoTarefaRepository.save(s2);
		
		SituacaoTarefa s3 = new SituacaoTarefa();
		s3.setNome("Situação 03");
		s3.setIndice(3);
		s3.setCor("#3E7DAE");
		situacaoTarefaRepository.save(s3);
		
		SituacaoTarefa s4 = new SituacaoTarefa();
		s4.setNome("Situação 04");
		s4.setIndice(4);
		s4.setCor("#0BD18C");
		situacaoTarefaRepository.save(s4);
		
	}
	
	public void administrador() {
		
		Administrador a1 = new Administrador();
		
		a1.setId(1);
		a1.setNome("Administrador");
		a1.setEmail("admin@email.com");
		a1.setSenha("admin123");
		a1.encryptPassword();
		
		administradorRepository.save(a1);
		
	}
	
}
