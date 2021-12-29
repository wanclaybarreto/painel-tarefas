package br.com.wanclaybarreto.paineltarefas.application.service;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.wanclaybarreto.paineltarefas.util.IOUtils;

@Service
public class LoadFileService {
	
	@Value("${paineltarefas.files.tarefas.imagens}")
	private String dirImagensTarefas;
	
	@Value("${paineltarefas.files.tarefas.videos}")
	private String dirVideosTarefas;
	
	@Value("${paineltarefas.files.tarefas.relatorios}")
	private String dirRelatoriosTarefas;
	
	public byte[] getBytes(String type, String fileName) {
		try {
			
			String dir;
			
			if("imagemTarefa".equals(type)) {
				dir = dirImagensTarefas;
			} else if ("videoTarefa".equals(type)) {
				dir = dirVideosTarefas;
			} else if ("relatorioTarefa".equals(type)) {
				dir = dirRelatoriosTarefas;
			} else {
				throw new Exception(type + " não é um tipo de arquivo válido!");
			}
			
			return IOUtils.getBytes(Paths.get(dir, fileName));
			
		} catch (Exception e) {
			
			throw new LoadFileServiceException(e);
			
		}
	}
	
}
