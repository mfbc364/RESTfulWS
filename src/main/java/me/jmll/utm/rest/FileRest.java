package me.jmll.utm.rest;

import me.jmll.utm.service.FileServiceImpl;
import me.jmll.utm.view.DownloadView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import me.jmll.utm.rest.exception.ResourceNotFoundException;
import me.jmll.utm.service.FileService;

public class FileRest {
	FileService fileService;
	
	@RequestMapping(value="api/v1/file", method=RequestMethod.OPTIONS)
	@ResponseBody
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,HEAD,GET,POST");
		return new ResponseEntity<>(null, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value="api/v1/file", method=RequestMethod.GET)
	@ResponseBody
	public DownloadView downloadFile(@RequestParam("path") String path) throws IOException {
		Path fileName = Paths.get(path);
		if(Files.notExists(fileName)) {
			throw new ResourceNotFoundException("File was not found!");
		}
		else {
			fileService.getFile(path);
		}
		return new DownloadView(fileName.getFileName().toString(), Files.probeContentType(fileName), Files.readAllBytes(fileName));
	}
	
	@RequestMapping(value="api/v1/file", method=RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String deleteFile(@RequestParam("path") String path) {
		Path fileName = Paths.get(path);
		if(Files.notExists(fileName)) {
			throw new ResourceNotFoundException("File was not found!");
		}
		else {
			File file = new File(fileName.getFileName().toString());
			file.delete();
		}
		String response;
		response = "File " + fileName.getFileName().toString() + " is deleted!";
		return response;
	}
	
	@RequestMapping(value="api/v1/file", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
										@RequestParam("name") String name,
										@RequestParam("dir")  String dir        ) {
		Path path = Paths.get(dir);
		if(file.isEmpty()) {
			return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
		}
		else {
			FileServiceImpl up = new FileServiceImpl();
			up.uploadFile(file, name, dir);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", path.toString());
		return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
	}
}
