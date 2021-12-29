package br.com.wanclaybarreto.paineltarefas.domain.desenvolvedor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DesenvolvedorRepository extends JpaRepository<Desenvolvedor, Integer> {
	
	public Desenvolvedor findByEmail(String email);
	
}
