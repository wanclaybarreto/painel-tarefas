package br.com.wanclaybarreto.paineltarefas.domain.administrador;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.wanclaybarreto.paineltarefas.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "administrador")
public class Administrador extends Usuario {
	
}
