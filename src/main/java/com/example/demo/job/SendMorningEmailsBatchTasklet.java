package com.example.demo.job;



import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendMorningEmailsBatchTasklet implements Tasklet {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) {
        log.info("Inizio invio email di buongiorno...");

        try {
            // Recupera tutte le email degli utenti
            List<String> emails = userRepository.findAllEmails();
            log.info("Trovate {} email da processare", emails.size());

            emailService.sendMorningGreetings(emails);

            log.info("Invio email di buongiorno completato");
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            log.error("Errore durante l'esecuzione del batch di invio email", e);
            throw new RuntimeException("Errore durante l'invio delle email di buongiorno", e);
        }
    }
}
