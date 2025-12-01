package id.taufiq.latihan.spring_ai.service;

import id.taufiq.latihan.spring_ai.exception.UnsupportedFileFormatException;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class IndexService {

    private final VectorStore vectorStore;

    public IndexService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void index(MultipartFile file,  String tenant) {
        try {
            validatePdf(file);
            Resource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            var pdfReader = new TikaDocumentReader(resource);
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> documents = pdfReader.get();
            for (Document document : documents) {
                document.getMetadata().put("tenant", tenant.toLowerCase());
            }
            vectorStore.accept(textSplitter.apply(documents));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to index uploaded file", e);
        }
    }

    private void validatePdf(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new UnsupportedFileFormatException("File format is not supported. Only PDF files are allowed.");
        }
    }
}
