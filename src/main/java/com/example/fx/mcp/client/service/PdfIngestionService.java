package com.example.fx.mcp.client.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfIngestionService {
	private final VectorStore vectorStore;

	public PdfIngestionService(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	public void ingest(Resource pdfResource) {
		// 1. Read PDF page by page
		PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfResource);

		// 2. Split text into manageable chunks
		TokenTextSplitter splitter = TokenTextSplitter.builder()
				.withChunkSize(1024)
				.build();
		List<Document> chunks = splitter.apply(reader.get());

		// 3. Add to local vector database
		vectorStore.accept(chunks);
	}
}
