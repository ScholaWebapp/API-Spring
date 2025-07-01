package com.scholaapi.service;

import com.scholaapi.dto.VideoRequest;
import com.scholaapi.dto.DocumentRequest;
import com.scholaapi.dto.QuizRequest;
import com.scholaapi.dto.QuizQuestionRequest;
import com.scholaapi.dto.QuizSubmissionRequest;
import com.scholaapi.dto.VideoResponseDTO;
import com.scholaapi.dto.DocumentResponseDTO;
import com.scholaapi.model.Module;
import com.scholaapi.model.Resource;
import com.scholaapi.model.Video;
import com.scholaapi.model.Document;
import com.scholaapi.model.Quiz;
import com.scholaapi.model.QuizQuestion;
import com.scholaapi.repository.ModuleRepository;
import com.scholaapi.repository.ResourceRepository;
import com.scholaapi.repository.VideoRepository;
import com.scholaapi.repository.DocumentRepository;
import com.scholaapi.repository.QuizRepository;
import com.scholaapi.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private FileUploadService fileUploadService;

    // Create video resource
    @Transactional
    public Resource createVideoResource(VideoRequest request) {
        Module module = moduleRepository.findById(request.getModuleUuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));

        // Create video entity
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setDuration(request.getDuration());
        video.setFilename(""); // This will be set when file is uploaded

        Video savedVideo = videoRepository.save(video);

        // Create resource entity
        Resource resource = new Resource();
        resource.setModule(module);
        resource.setResourceType(Resource.ResourceType.VIDEO);
        resource.setVideo(savedVideo);
        resource.setOrderIndex(getNextOrderIndex(module.getUuid()));

        return resourceRepository.save(resource);
    }

    // Get video resource by UUID
    public Resource getVideoResource(UUID videoUuid) {
        return resourceRepository.findByVideoUuid(videoUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video resource not found"));
    }

    // Get all videos in a module
    public List<Resource> getVideosByModule(UUID moduleUuid) {
        if (!moduleRepository.existsById(moduleUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found");
        }
        return resourceRepository.findByModuleUuidAndResourceTypeOrderByOrderIndexAsc(moduleUuid, Resource.ResourceType.VIDEO);
    }

    // Update video resource
    @Transactional
    public Resource updateVideoResource(UUID videoUuid, VideoRequest request) {
        Resource resource = getVideoResource(videoUuid);
        Video video = resource.getVideo();

        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setDuration(request.getDuration());

        videoRepository.save(video);
        return resource;
    }

    // Delete video resource
    @Transactional
    public void deleteVideoResource(UUID videoUuid) {
        Resource resource = getVideoResource(videoUuid);
        Video video = resource.getVideo();

        // Delete the video file from disk
        try {
            fileUploadService.deleteFile(video.getFilename(), "video");
        } catch (Exception e) {
            // Log error but continue with deletion
        }

        // Delete from database
        resourceRepository.delete(resource);
        videoRepository.delete(video);
    }

    // Helper method to get next order index
    private Integer getNextOrderIndex(UUID moduleUuid) {
        List<Resource> existingResources = resourceRepository.findByModuleUuidOrderByOrderIndexAsc(moduleUuid);
        return existingResources.size() + 1;
    }

    // Create document resource
    @Transactional
    public Resource createDocumentResource(DocumentRequest request) {
        Module module = moduleRepository.findById(request.getModuleUuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));

        // Create document entity
        Document document = new Document();
        document.setTitle(request.getTitle());
        document.setDescription(request.getDescription());
        document.setFileType(request.getFileType());
        document.setFilename(""); // This will be set when file is uploaded

        Document savedDocument = documentRepository.save(document);

        // Create resource entity
        Resource resource = new Resource();
        resource.setModule(module);
        resource.setResourceType(Resource.ResourceType.DOCUMENT);
        resource.setDocument(savedDocument);
        resource.setOrderIndex(getNextOrderIndex(module.getUuid()));

        return resourceRepository.save(resource);
    }

    // Get document resource by UUID
    public Resource getDocumentResource(UUID documentUuid) {
        return resourceRepository.findByDocumentUuid(documentUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document resource not found"));
    }

    // Get all documents in a module
    public List<Resource> getDocumentsByModule(UUID moduleUuid) {
        if (!moduleRepository.existsById(moduleUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found");
        }
        return resourceRepository.findByModuleUuidAndResourceTypeOrderByOrderIndexAsc(moduleUuid, Resource.ResourceType.DOCUMENT);
    }

    // Update document resource
    @Transactional
    public Resource updateDocumentResource(UUID documentUuid, DocumentRequest request) {
        Resource resource = getDocumentResource(documentUuid);
        Document document = resource.getDocument();

        document.setTitle(request.getTitle());
        document.setDescription(request.getDescription());
        document.setFileType(request.getFileType());

        documentRepository.save(document);
        return resource;
    }

    // Delete document resource
    @Transactional
    public void deleteDocumentResource(UUID documentUuid) {
        Resource resource = getDocumentResource(documentUuid);
        Document document = resource.getDocument();

        // Delete the document file from disk
        try {
            fileUploadService.deleteFile(document.getFilename(), "document");
        } catch (Exception e) {
            // Log error but continue with deletion
        }

        // Delete from database
        resourceRepository.delete(resource);
        documentRepository.delete(document);
    }

    // Create quiz resource
    @Transactional
    public Resource createQuizResource(QuizRequest request) {
        Module module = moduleRepository.findById(request.getModuleUuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));

        // Create quiz entity
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setPassingScore(request.getPassingScore());

        Quiz savedQuiz = quizRepository.save(quiz);

        // Create quiz questions
        if (request.getQuestions() != null) {
            for (int i = 0; i < request.getQuestions().size(); i++) {
                QuizQuestionRequest questionRequest = request.getQuestions().get(i);
                QuizQuestion question = new QuizQuestion();
                question.setQuiz(savedQuiz);
                question.setQuestionText(questionRequest.getQuestion());
                question.setOrderIndex(i + 1);
                
                // Set options (assuming 4 options)
                List<String> options = questionRequest.getOptions();
                if (options.size() >= 1) question.setOptionA(options.get(0));
                if (options.size() >= 2) question.setOptionB(options.get(1));
                if (options.size() >= 3) question.setOptionC(options.get(2));
                if (options.size() >= 4) question.setOptionD(options.get(3));
                
                // Set correct answer
                Integer correctIndex = questionRequest.getCorrectAnswerIndex();
                if (correctIndex != null && correctIndex >= 0 && correctIndex < 4) {
                    QuizQuestion.CorrectAnswer[] answers = QuizQuestion.CorrectAnswer.values();
                    question.setCorrectAnswer(answers[correctIndex]);
                }
                
                question.setPoints(1); // All questions worth 1 point
                quizQuestionRepository.save(question);
            }
        }

        // Create resource entity
        Resource resource = new Resource();
        resource.setModule(module);
        resource.setResourceType(Resource.ResourceType.QUIZ);
        resource.setQuiz(savedQuiz);
        resource.setOrderIndex(getNextOrderIndex(module.getUuid()));

        return resourceRepository.save(resource);
    }

    // Get quiz resource by UUID
    public Resource getQuizResource(UUID quizUuid) {
        return resourceRepository.findByQuizUuid(quizUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz resource not found"));
    }

    // Get all quizzes in a module
    public List<Resource> getQuizzesByModule(UUID moduleUuid) {
        if (!moduleRepository.existsById(moduleUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found");
        }
        return resourceRepository.findByModuleUuidAndResourceTypeOrderByOrderIndexAsc(moduleUuid, Resource.ResourceType.QUIZ);
    }

    // Get quiz for student (without correct answers)
    public Quiz getQuizForStudent(UUID quizUuid) {
        Resource resource = getQuizResource(quizUuid);
        Quiz quiz = resource.getQuiz();
        
        // Remove correct answers from questions for students
        for (QuizQuestion question : quiz.getQuestions()) {
            question.setCorrectAnswer(null);
        }
        
        return quiz;
    }

    // Submit quiz answers
    public QuizSubmissionResult submitQuiz(UUID quizUuid, QuizSubmissionRequest submission) {
        Resource resource = getQuizResource(quizUuid);
        Quiz quiz = resource.getQuiz();
        
        int totalQuestions = quiz.getQuestions().size();
        int correctAnswers = 0;
        List<Boolean> answerResults = new ArrayList<>();
        
        for (int i = 0; i < totalQuestions; i++) {
            QuizQuestion question = quiz.getQuestions().get(i);
            
            if (i < submission.getAnswers().size()) {
                Integer studentAnswer = submission.getAnswers().get(i);
                boolean isCorrect = question.getCorrectAnswer().ordinal() == studentAnswer;
                answerResults.add(isCorrect);
                
                if (isCorrect) {
                    correctAnswers++;
                }
            } else {
                answerResults.add(false);
            }
        }
        
        double percentage = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
        boolean passed = quiz.getPassingScore() == null || percentage >= quiz.getPassingScore();
        
        return new QuizSubmissionResult(correctAnswers, totalQuestions, percentage, passed, answerResults);
    }

    // Update quiz resource
    @Transactional
    public Resource updateQuizResource(UUID quizUuid, QuizRequest request) {
        Resource resource = getQuizResource(quizUuid);
        Quiz quiz = resource.getQuiz();

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setPassingScore(request.getPassingScore());

        quizRepository.save(quiz);
        return resource;
    }

    // Delete quiz resource
    @Transactional
    public void deleteQuizResource(UUID quizUuid) {
        Resource resource = getQuizResource(quizUuid);
        Quiz quiz = resource.getQuiz();

        // Delete from database
        resourceRepository.delete(resource);
        quizRepository.delete(quiz);
    }

    // Quiz submission result class
    public static class QuizSubmissionResult {
        private int correctAnswers;
        private int totalQuestions;
        private double percentage;
        private boolean passed;
        private List<Boolean> answerResults;

        public QuizSubmissionResult(int correctAnswers, int totalQuestions, double percentage, boolean passed, List<Boolean> answerResults) {
            this.correctAnswers = correctAnswers;
            this.totalQuestions = totalQuestions;
            this.percentage = percentage;
            this.passed = passed;
            this.answerResults = answerResults;
        }

        // Getters
        public int getCorrectAnswers() { return correctAnswers; }
        public int getTotalQuestions() { return totalQuestions; }
        public double getPercentage() { return percentage; }
        public boolean isPassed() { return passed; }
        public List<Boolean> getAnswerResults() { return answerResults; }
    }

    // Update video filename
    @Transactional
    public void updateVideoFilename(UUID videoUuid, String filename) {
        Video video = videoRepository.findById(videoUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
        video.setFilename(filename);
        videoRepository.save(video);
    }

    // Update document filename
    @Transactional
    public void updateDocumentFilename(UUID documentUuid, String filename) {
        Document document = documentRepository.findById(documentUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        document.setFilename(filename);
        documentRepository.save(document);
    }

    public VideoResponseDTO toVideoResponseDTO(Video video) {
        VideoResponseDTO dto = new VideoResponseDTO();
        dto.setUuid(video.getUuid());
        dto.setTitle(video.getTitle());
        dto.setDescription(video.getDescription());
        dto.setFilename(video.getFilename());
        dto.setDuration(video.getDuration());
        return dto;
    }

    public DocumentResponseDTO toDocumentResponseDTO(Document document) {
        DocumentResponseDTO dto = new DocumentResponseDTO();
        dto.setUuid(document.getUuid());
        dto.setTitle(document.getTitle());
        dto.setDescription(document.getDescription());
        dto.setFilename(document.getFilename());
        dto.setFileType(document.getFileType());
        dto.setFileSize(document.getFileSize());
        return dto;
    }
} 