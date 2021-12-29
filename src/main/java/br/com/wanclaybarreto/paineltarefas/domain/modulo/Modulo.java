package br.com.wanclaybarreto.paineltarefas.domain.modulo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "modulo")
public class Modulo implements Serializable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "O nome não pode ser vazio.")
	@Size(max = 50, message = "O nome não deve possuir mais do que 50 caracteres.")
	@Column(length = 50, nullable = false)
	private String nome;
	
	@OneToMany(mappedBy = "modulo")
	private Set<Tarefa> tarefas = new HashSet<>(0);
	
}
