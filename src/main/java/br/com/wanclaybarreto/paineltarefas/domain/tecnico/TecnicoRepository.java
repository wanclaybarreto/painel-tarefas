package br.com.wanclaybarreto.paineltarefas.domain.tecnico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
	
	public Tecnico findByNome(String nome);
	
	public Tecnico findByEmail(String email);
	
}
