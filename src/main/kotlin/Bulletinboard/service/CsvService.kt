package Bulletinboard.service

import Bulletinboard.form.PostForm
import Bulletinboard.model.Posts
import Bulletinboard.repository.PostRepository
import com.codersee.csvupload.exception.BadRequestException
import com.codersee.csvupload.exception.CsvImportException
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Service
class CsvService(private val postRepository: PostRepository, private val postService: PostService) {

    fun uploadCsvFile(file: MultipartFile): String {
        throwIfFileEmpty(file)
        var fileReader: BufferedReader? = null

        try {
            fileReader = BufferedReader(InputStreamReader(file.inputStream))
            val csvToBean = createCSVToBean(fileReader)
            for (postForm in csvToBean.parse()) {
                if (postForm.title.isNullOrEmpty() || postForm.description.isNullOrEmpty()) return "title and description cannot be null"
                if (postForm.title?.let { postService.findByTitle(it) } == false) {
                    return "title already exists"
                }
                var post: Posts = Posts()
                post.title = postForm.title
                post.description = postForm.description
                post.createdUserId = 1
                post.updatedUserId = 1
                postRepository.save(post)
            }
            return "success"
        } catch (ex: Exception) {
            throw CsvImportException("Error during csv import")
        } finally {
            closeFileReader(fileReader)
        }
    }

    private fun throwIfFileEmpty(file: MultipartFile) {
        if (file.isEmpty)
            throw BadRequestException("Empty file")
    }

    private fun createCSVToBean(fileReader: BufferedReader?): CsvToBean<PostForm> =
        CsvToBeanBuilder<PostForm>(fileReader)
            .withType(PostForm::class.java)
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private fun closeFileReader(fileReader: BufferedReader?) {
        try {
            fileReader!!.close()
        } catch (ex: IOException) {
            throw CsvImportException("Error during csv import")
        }
    }
}
