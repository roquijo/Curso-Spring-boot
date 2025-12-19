package com.devsenior.co.producto.controller;

import com.devsenior.co.producto.feign.IPostRest;
import com.devsenior.co.producto.model.Post;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "API para consultar posts")
@SecurityRequirement(name = "token")
public class PostController {

    @Autowired
    private IPostRest postRest;

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postRest.getPostById(id);
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('producto-rol2')")
    public List<Post> getPosts() {
        return postRest.getPosts();
    }
}
