package socialnetwork.domain.validators;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.exceptions.PrietenieValidationException;

public class PrietenieValidator implements Validator<Prietenie> {
    /**
     * valideaza un obiect de tip prietenie
     * @param entity obiectul de validat
     * @throws ValidationException daca Prieteniea nu are datele corecte
     */
    @Override

    public void validate(Prietenie entity) throws ValidationException {
        String error="";
        if(entity.getId().getLeft()==null||entity.getId().getRight()==null)
            error+="ID cannot be null\n";
        if(entity.getYear()<1900)
            error+="Year cannot be smaller than 1900!\n";
        if(entity.getMonth()<1 || entity.getMonth()>12)
            error+="Month has to be between 1 and 12!\n";
        if(entity.getDay()<1 || entity.getDay()>32)
            error+="Day has to be between 1 and 32!\n";

        if(!error.isEmpty()) throw new PrietenieValidationException(error);


}
}