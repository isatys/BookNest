package com.BookNest.BookNestCore.task;

import com.BookNest.BookNestCore.model.Emprunt;
import com.BookNest.BookNestCore.repository.EmpruntRepository;
import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private EmpruntRepository empruntRepository;  // Assurez-vous que ce repository est configuré
    @Autowired
    private UserService userService;

   @Scheduled(cron = "0 0 0 * * *") // Exécution chaque jour à minuit
   public void sendReminderEmails() {
        LocalDate today = LocalDate.now();
        LocalDate dateRetour = today.plusDays(7);

        List<Emprunt> emprunts = empruntRepository.findBydateRetour(dateRetour);
        System.out.println("Emprunts trouvés pour la date de retour : " + dateRetour + " : " + emprunts.size());

        for (Emprunt emprunt : emprunts) {
            String userEmail = emprunt.getUtilisateur().getEmail();
            String bookTitle = emprunt.getLivre().getTitre();
            userService.sendEmail(
                    userEmail,
                    "Rappel de retour de livre",
                    "Bonjour " + emprunt.getUtilisateur().getEmail() + ",\n\nVotre emprunt pour le livre "
                            + bookTitle + " est dû dans une semaine. Veuillez le retourner avant " + dateRetour + ".\n\nMerci!"
            );
            System.out.println("Email envoyé à : " + userEmail);

        }
    }
}

