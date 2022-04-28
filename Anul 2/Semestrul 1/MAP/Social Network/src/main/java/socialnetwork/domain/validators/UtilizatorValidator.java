package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.UserValidationException;

public class UtilizatorValidator implements Validator<Utilizator> {
    /**
     * valideaza un obiect de tip Utiliaztpr
     * @param entity obiectul de validat
     * @throws ValidationException daca Utilizatorul nu are datele corecte
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException{
        String error="";
        String s=entity.getFirstName();
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    error+="First Name of the user cannot contain numbers!\n";
                    break;
                }
            }
        }
        if(s.isEmpty()) error+="First Name cannot be null\n";

        s=entity.getLastName();
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    error+="Last Name of the user cannot contain numbers!\n";
                    break;
                }
            }
        }
        if(s.isEmpty()) error+="Last Name cannot be null\n";

        s = entity.getUserName();
        if(s.isEmpty()) error+="User Name cannot be null\n";

        if(!error.isEmpty()) throw new UserValidationException(error);

    }
}
