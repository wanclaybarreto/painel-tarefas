package br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import br.com.wanclaybarreto.paineltarefas.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "desenvolvedor")
public class Desenvolvedor extends Usuario {
	
	@OneToMany(mappedBy = "desenvolvedor")
	private Set<Tarefa> tarefas = new HashSet<>(0);
	
}
