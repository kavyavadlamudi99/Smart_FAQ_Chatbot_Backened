package com.faq.chatbot.service;

import com.faq.chatbot.entity.Faq;
import com.faq.chatbot.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqContextService {

    private static final int MAX_CONTEXT_LENGTH = 8000;

    private final FaqRepository faqRepository;

    public String buildContextForQuestion(String userQuestion) {
        List<Faq> activeFaqs = faqRepository.findByIsActiveTrue();

        StringBuilder context = new StringBuilder();
        int index = 1;

        for (Faq faq : activeFaqs) {
            String entry = "FAQ " + index + ":\n" +
                           "Question: " + faq.getQuestion() + "\n" +
                           "Answer: " + faq.getAnswer() + "\n\n";

            if (context.length() + entry.length() > MAX_CONTEXT_LENGTH) {
                log.debug("Reached max context length after {} FAQs", index - 1);
                break;
            }

            context.append(entry);
            index++;
        }

        return context.toString().trim();
    }
}
