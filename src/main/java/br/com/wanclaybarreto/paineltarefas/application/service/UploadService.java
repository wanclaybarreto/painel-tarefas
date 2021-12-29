package br.com.wanclaybarreto.paineltarefas.application.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.wanclaybarreto.paineltarefas.util.IOUtils;

@Service
public class UploadService {
	
	public void uploadFile(MultipartFile multipartFile, String fileName, String outputDir) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, outputDir);
		} catch (IOException e) {
			throw new UploadServiceException(e);
		}
	}
	
}
