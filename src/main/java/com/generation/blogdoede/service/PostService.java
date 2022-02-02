package com.generation.blogdoede.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.generation.blogdoede.commons.mappers.PostMapper;
import com.generation.blogdoede.domain.model.Post;
import com.generation.blogdoede.domain.repository.PostRepository;
import com.generation.blogdoede.dto.PostDTO;

@Service
public class PostService {

	private @Autowired PostMapper mapper;
	private @Autowired PostRepository repo;

	public ResponseEntity<List<PostDTO>> getAllPosts(){
		List<Post> allPosts = repo.findAll();	
		return foundPosts(allPosts);
	}

	public ResponseEntity<List<PostDTO>> getPostByTitulo(String titulo) {
		List<Post> postsByTitle = repo.findAllByPostTitleContainingIgnoreCase(titulo);		
		return foundPosts(postsByTitle);
	}
	
	private ResponseEntity<List<PostDTO>> foundPosts(List<Post> posts){
		if (posts.isEmpty()) {
			return ResponseEntity.status(204).build();
		} else {
			return ResponseEntity.status(200)
					.body(posts.stream()
					.map(mapper::toDTO)
					.collect(Collectors.toList())); 
		}
	}
	
	public ResponseEntity<PostDTO> getPostById(Long id) {
		return repo.findById(id)
				.map(resp -> ResponseEntity.ok(mapper.toDTO(resp)))
				.orElse(ResponseEntity.notFound().build());
	}

	public ResponseEntity<PostDTO> createNewPost(Post postagem) {
		return ResponseEntity
				.status(201)
				.body(mapper.toDTO(repo.save(postagem)));
	}
	
	
	
}
