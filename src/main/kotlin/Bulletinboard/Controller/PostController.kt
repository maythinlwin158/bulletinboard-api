package Bulletinboard.Controller

//import com.opencsv.bean.CsvToBean
//import com.opencsv.bean.CsvToBeanBuilder

import Bulletinboard.DTO.PostEditDTO
import Bulletinboard.DTO.PostListDTO
import Bulletinboard.form.PostForm
import Bulletinboard.model.Posts
import Bulletinboard.service.PostService
import Bulletinboard.util.ExcelGenerator
import Bulletinboard.service.CsvService
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid


@RestController
class PostController(private val postService: PostService, private val csvService: CsvService) {

    @GetMapping("/posts")
    fun getAllPosts(@RequestParam("search", defaultValue = "")search: String
    , @RequestParam("page", defaultValue = "1")page: Int): List<PostListDTO> = postService.getAllPosts(search, PageRequest.of(page - 1, 2))

    @PostMapping("/post")
    fun createPost(@Valid @RequestBody post: PostForm): ResponseEntity<String> {
        return if (postService.createPost(post))
            ResponseEntity.ok("success")
        else ResponseEntity.badRequest().body("insert failed")
    }

    @GetMapping("/post/{postId}")
    fun editPost(@PathVariable(value = "postId")postId: Int): ResponseEntity<PostEditDTO> {
       return postService.getPostById(postId).map { post ->
            ResponseEntity.ok(post)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/post/{postId}")
    fun updatePost(@PathVariable(value = "postId")postId: Int,@Valid @RequestBody post: Posts ): ResponseEntity<String> {
        return if (postService.updatePostById(postId, post)) ResponseEntity.ok("successfully update") else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/post/{postId}")
    fun deletePost(@PathVariable(value = "postId")postId: Int): ResponseEntity<Void> {
        return if (postService.postDeleteById(postId)) ResponseEntity<Void>(HttpStatus.OK) else ResponseEntity.notFound().build()
    }

    @GetMapping("/posts/excel")
    fun downloadPosts(@RequestParam("search", defaultValue = "")search: String
                    , @RequestParam("page", defaultValue = "1")page: Int): ResponseEntity<InputStreamResource> {
        val posts = postService.getAllPosts(search, PageRequest.of(page - 1, 2))

        val bis = ExcelGenerator.customerPDFReport(posts);

        val headers = HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=posts.xlsx");

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(InputStreamResource(bis));
    }


    @PostMapping("/posts")
    fun uploadCsvFile(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        val importedEntries = csvService.uploadCsvFile(file)
        return ResponseEntity.ok(importedEntries)
    }
}