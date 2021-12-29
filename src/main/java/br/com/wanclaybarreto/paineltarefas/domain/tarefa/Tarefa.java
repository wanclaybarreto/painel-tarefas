package br.com.wanclaybarreto.paineltarefas.domain.tarefa;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import br.com.wanclaybarreto.paineltarefas.domain.cliente.Cliente;
import br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor.Desenvolvedor;
import br.com.wanclaybarreto.paineltarefas.domain.modulo.Modulo;
import br.com.wanclaybarreto.paineltarefas.domain.tecnico.Tecnico;
import br.com.wanclaybarreto.paineltarefas.infrastructure.web.validator.UploadConstraint;
import br.com.wanclaybarreto.paineltarefas.util.FileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tarefa")
public class Tarefa implements Serializable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message = "A situação precisa ser especificada.")
	@ManyToOne
	@JoinColumn(name = "situacao_tarefa_id")
	private SituacaoTarefa situacao;
	
	@NotBlank(message = "O título não pode ser vazio.")
	@Column(nullable = false)
	private String titulo;
	
	@NotBlank(message = "A descrição não pode ser vazia.")
	@Column(columnDefinition = "TEXT", nullable = false)
	private String descricao;
	
	private String imagem;
	
	@UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG})
	private transient MultipartFile imagemFile;
	
	private String video;
	
	@UploadConstraint(acceptedTypes = {FileType.MP4, FileType.MOV, FileType.OGG})
	private transient MultipartFile videoFile;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate dataCadastro;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPrevisaoFinalizacao;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataFinalizacao;
	
	@Column(columnDefinition = "TEXT", nullable = true)
	private String solucao;
	
	@NotNull(message = "O técnico precisa ser especificado.")
	@ManyToOne
	@JoinColumn(name = "tecnico_id")
	private Tecnico tecnico;
	
	@ManyToOne
	@JoinColumn(name = "desenvolvedor_id")
	private Desenvolvedor desenvolvedor;
	
	@NotNull(message = "O cliente precisa ser especificado.")
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@NotNull(message = "O módulo precisa ser especificado.")
	@ManyToOne
	@JoinColumn(name = "modulo_id")
	private Modulo modulo;
	
	public void setImagemFileName() {
		if (getId() == null) {
			throw new IllegalStateException("É preciso primeiro gravar o registro (Tarefa).");
		}
		
		imagem = getId() + "-imagem." + FileType.of(imagemFile.getContentType()).getExtension();
	}
	
	public void setVideoFileName() {
		if (getId() == null) {
			throw new IllegalStateException("É preciso primeiro gravar o registro (Tarefa).");
		}
		
		video = getId() + "-video." + FileType.of(videoFile.getContentType()).getExtension();
	}
	
}
