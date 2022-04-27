package socialnetwork.domain.validators;

import socialnetwork.domain.Message;


public class MessageValidator implements Validator<Message> {
    /**
     * valideaza un obiect de tip Message
     *
     * @param entity obiectul de validat
     * @throws ValidationException daca Mesajul nu are datele corecte
     */

    @Override
    public void validate(Message entity) throws ValidationException {
        String error = "";
        if (entity.getMessage() == "")
            error += "Mesajul nu poate sa fie null!\n";
        if (entity.getFrom() == null)
            error += "Nu exista expeditor!\n";
        if (entity.getTo().size() == 0)
            error += "Nu exista destinatari!\n";
        if (!error.isEmpty())
            throw new ValidationException(error);

    }
}
