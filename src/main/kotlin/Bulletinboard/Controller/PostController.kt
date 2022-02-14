package Bulletinboard.Controller

import Bulletinboard.DTO.PostEditDTO
import Bulletinboard.DTO.PostListDTO
import Bulletinboard.form.PostEditForm
import Bulletinboard.form.PostForm
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

    /**
     * Get ALl Posts
     *
     * @param search String
     * @param page Integer
     * @return List<PostListDTO> PostListDTO
     */
    @GetMapping("/posts")
    fun getAllPosts(@RequestParam("search", defaultValue = "")search: String
    , @RequestParam("page", defaultValue = "1")page: Int): List<PostListDTO> = postService.getAllPosts(search, PageRequest.of(page - 1, 5))

    /**
     * Create Post
     *
     * @param post PostForm
     * @return ResponseEntity String
     */
    @PostMapping("/post")
    fun createPost(@Valid @RequestBody post: PostForm): ResponseEntity<String> {
        return if (postService.createPost(post))
            ResponseEntity.ok("success")
        else ResponseEntity.badRequest().body("insert failed")
    }

    /**
     * Display Edit Form
     *
     * @param postId Integer
     * @return PostEditDTO
     */
    @GetMapping("/post/{postId}")
    fun editPost(@PathVariable(value = "postId")postId: Int): ResponseEntity<PostEditDTO> {
       return postService.getPostById(postId).map { post ->
            ResponseEntity.ok(post)
        }.orElse(ResponseEntity.notFound().build())
    }

    /**
     * Update Post
     *
     * @param postId Integer
     * @param post PostEditForm
     * @return String
     */
    @PutMapping("/post/{postId}")
    fun updatePost(@PathVariable(value = "postId")postId: Int,@Valid @RequestBody post: PostEditForm ): ResponseEntity<String> {
        return if (postService.updatePostById(postId, post)) ResponseEntity.ok("successfully update") else ResponseEntity.notFound().build()
    }

    /**
     * Delete Post
     *
     * @param postId Integer
     * @return ResponseEntity
     */
    @DeleteMapping("/post/{postId}")
    fun deletePost(@PathVariable(value = "postId")postId: Int): ResponseEntity<Void> {
        return if (postService.postDeleteById(postId)) ResponseEntity<Void>(HttpStatus.OK) else ResponseEntity.notFound().build()
    }

    /**
     * Download Posts
     *
     * @param search String
     * @param page Integer
     * @return InputStreamResource
     */
    @GetMapping("/posts/excel")
    fun downloadPosts(@RequestParam("search", defaultValue = "")search: String
                    , @RequestParam("page", defaultValue = "1")page: Int): ResponseEntity<InputStreamResource> {
        val posts = postService.getAllPostsForExcel(search)

        val bis = ExcelGenerator.customerPDFReport(posts);

        val headers = HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=posts.xlsx");

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(InputStreamResource(bis));
    }


    /**
     * Upload post by CSV
     *
     * @param file MultipartFile
     * @return String
     */
    @PostMapping("/posts")
    fun uploadCsvFile(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        val importedEntries = csvService.uploadCsvFile(file)
        return ResponseEntity.ok(importedEntries)
    }
}