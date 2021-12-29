package br.com.wanclaybarreto.paineltarefas.application.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.wanclaybarreto.paineltarefas.domain.tarefa.Tarefa;
import br.com.wanclaybarreto.paineltarefas.domain.tarefa.TarefaRepository;
import br.com.wanclaybarreto.paineltarefas.util.ReportsUtils;

@Service
public class TarefaService {
	
	@Autowired
	private UploadService uploadService;
	
	@Value("${paineltarefas.files.tarefas.imagens}")
	private String dirImagens;
	
	@Value("${paineltarefas.files.tarefas.videos}")
	private String dirVideos;
	
	@Value("${paineltarefas.files.tarefas.relatorios}")
	private String dirRelatorios;
	
	@Value("${paineltarefas.files.tarefas.relatorios.modelo}")
	private String dirJasperFile;
	
	@Autowired
	TarefaRepository tarefaRepository;
	
	@Transactional
	public void saveTarefa(Tarefa tarefa) {
		
		boolean haveImg = true;
		boolean haveVid = true;
		
		if (tarefa.getImagemFile() == null || tarefa.getImagemFile().isEmpty()) haveImg = false;
		if (tarefa.getVideoFile()  == null || tarefa.getVideoFile().isEmpty()) haveVid = false;
		
		MultipartFile imagemFile;
		MultipartFile videoFile;
		
		if (tarefa.getId() != null && !haveImg) {
			
			Tarefa tarefaDB = tarefaRepository.findById(tarefa.getId()).orElseThrow();
			tarefa.setImagem(tarefaDB.getImagem());
			
		}
		
		if (tarefa.getId() != null && !haveVid) {
			
			Tarefa tarefaDB = tarefaRepository.findById(tarefa.getId()).orElseThrow();
			tarefa.setVideo(tarefaDB.getVideo());
			
		}
		
		imagemFile = tarefa.getImagemFile();
		videoFile  = tarefa.getVideoFile();
		
		tarefa = tarefaRepository.save(tarefa);
		
		if (haveImg) {
			tarefa.setImagemFile(imagemFile);
			tarefa.setImagemFileName();
			uploadService.uploadFile(tarefa.getImagemFile(), tarefa.getImagem(), dirImagens);
		}
		
		if (haveVid) {
			tarefa.setVideoFile(videoFile);
			tarefa.setVideoFileName();
			uploadService.uploadFile(tarefa.getVideoFile(), tarefa.getVideo(), dirVideos);
		}
		
	}
	
	public void generateReport(Map<String, Object> params, List<Tarefa> tarefas) {
		ReportsUtils.generateReportPDF(dirJasperFile, params, tarefas, dirRelatorios+"/tarefas.pdf");
	}
	
}
