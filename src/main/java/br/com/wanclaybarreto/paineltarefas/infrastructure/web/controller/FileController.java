package br.com.wanclaybarreto.paineltarefas.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.wanclaybarreto.paineltarefas.application.service.LoadFileService;

@Controller
public class FileController {
	
	@Autowired
	private LoadFileService loadFileService;
	
	@ResponseBody
	@GetMapping(path = "/files/{type}/{name}")
	public byte[] getBytes(@PathVariable("type") String type, @PathVariable("name") String name) {
		
		return loadFileService.getBytes(type, name);
		
	}
	
	@ResponseBody
	@GetMapping(path = "/archives/{type}/{name}")
	public HttpEntity<byte[]> getArchive(@PathVariable("type") String type, @PathVariable("name") String name) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add("Content-Disposition", "attachment;filename=\""+ name +"\"");

		HttpEntity<byte[]> entity = new HttpEntity<byte[]>( loadFileService.getBytes(type, name), httpHeaders);

		return entity;
		
	}
	
}
