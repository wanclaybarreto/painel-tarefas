package br.com.wanclaybarreto.paineltarefas.domain.tarefa;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "situacao_tarefa")
public class SituacaoTarefa implements Serializable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "O nome não pode ser vazio.")
	@Size(max = 50, message = "O nome não deve possuir mais do que 50 caracteres.")
	@Column(length = 50, nullable = false)
	private String nome;
	
	@NotNull(message = "O índice não pode ser vazio.")
	@Max(value = 1000, message = "O índice não pode ser maior que 1000.")
	private Integer indice;
	
	@NotNull(message = "Especifique uma cor.")
	private String cor;
	
	@OneToMany(mappedBy = "situacao")
	private Set<Tarefa> tarefas = new HashSet<>(0);
	
}
