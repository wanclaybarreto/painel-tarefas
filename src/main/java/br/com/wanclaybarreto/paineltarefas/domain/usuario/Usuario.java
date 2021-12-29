package br.com.wanclaybarreto.paineltarefas.domain.usuario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.wanclaybarreto.paineltarefas.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class Usuario implements Serializable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "O nome não pode ser vazio.")
	@Size(max = 50, message = "O nome não deve possuir mais do que 50 caracteres.")
	@Column(length = 50, nullable = false)
	private String nome;
	
	@NotBlank(message = "O e-mail não pode ser vazio.")
	@Size(max = 70, message = "O e-mail é muito grande.")
	@Email(message = "O e-mail é inválido.")
	private String email;
	
	@NotBlank(message = "A senha não pode ser vazia.")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
	private String senha;
	
	public void encryptPassword() {
		this.senha = StringUtils.encrypt(this.senha);
	}
	
}
