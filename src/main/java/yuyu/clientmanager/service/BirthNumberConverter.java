package yuyu.clientmanager.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BirthNumberConverter {

    public LocalDate birthNumberToLocalDate(String birthNumber) {
        int year = Integer.parseInt(birthNumber.substring(0, 2));
        int month = Integer.parseInt(birthNumber.substring(2, 4));
        int day = Integer.parseInt(birthNumber.substring(4, 6));

        if (year <= 54) {
            year += 2000;
        } else {
            year += 1900;
        }

        if (month > 50) {
            month -= 50;
        }

        return LocalDate.of(year, month, day);
    }
}
