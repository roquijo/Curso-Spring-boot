package com.devsenior.co.producto.feign;

import com.devsenior.co.producto.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "Posts", url = "${post.baseUrl}")
public interface IPostRest {

    @GetMapping("/{id}")
    Post getPostById(@PathVariable Integer id);

    @GetMapping
    List<Post> getPosts();
}
