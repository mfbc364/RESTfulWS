package me.jmll.utm.rest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.jmll.utm.model.File;
import me.jmll.utm.model.FileLinkListResource;
import me.jmll.utm.model.Link;
import me.jmll.utm.rest.exception.ResourceNotFoundException;
import me.jmll.utm.service.FileService;

public class DirectoryRest {
	FileService fileService;
	
	@RequestMapping(value="api/v1/directory", method=RequestMethod.OPTIONS)
	@ResponseBody
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET");
		return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="api/v1/directory", method=RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getFilesJSON(@RequestParam("dir") String dir) {
		Path path = Paths.get(dir);
		Map<String, Object> response = new Hashtable<>(2);
		if(!Files.isDirectory(path) || Files.notExists(path)) {
			throw new ResourceNotFoundException("Directory was not found!");
		}
		else {
			List<Path> paths = new ArrayList<Path>();
			fileService.walkDir(path, paths);
			List<File> files = new ArrayList<File>();
			File file = new File();
			for(int i = 0; i <= paths.size(); i++) {
				file.setName(paths.get(i).getFileName().toString());
				file.setPath(paths.get(i).toFile().getPath());
				file.setFullPath(paths.get(i).toAbsolutePath().toString());
				file.setSizeBytes(paths.get(i).toFile().getTotalSpace());
				file.setLink(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().
						path(file.getFullPath()).toString(), "download"));
				files.add(file);
			}
			List<Link> links = new ArrayList<Link>();
			links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().
					path("/").build().toString(), "api"));
			links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().
					path("/directory").build().toString(), "self"));
			response.put("_links", links);
			response.put("data", files);
		}
		return response;
	}
	
	@RequestMapping(value="api/v1/directory", method=RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FileLinkListResource getFilesXML(@RequestParam("dir") String dir) {
		Path path = Paths.get(dir);
		FileLinkListResource filesLinksResource = new FileLinkListResource();
		if(!Files.isDirectory(path) || Files.notExists(path)) {
			throw new ResourceNotFoundException("Directory was not found!");
		}
		else {
			List<Path> paths = new ArrayList<Path>();
			fileService.walkDir(path, paths);
			List<File> files = new ArrayList<File>();
			File file = new File();
			for(int i = 0; i <= paths.size(); i++) {
				file.setName(paths.get(i).getFileName().toString());
				file.setPath(paths.get(i).toFile().getPath());
				file.setFullPath(paths.get(i).toAbsolutePath().toString());
				file.setSizeBytes(paths.get(i).toFile().getTotalSpace());
				file.setLink(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().
						path(file.getFullPath()).toString(), "download"));
				files.add(file);
			}
			List<Link> links = new ArrayList<Link>();
			links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().
					path("/").build().toString(), "api"));
			links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().
					path("/directory").build().toString(), "self"));
			filesLinksResource.setLinks(links);
			filesLinksResource.setFiles(files);
		}
		return filesLinksResource;
	}
}
