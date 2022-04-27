package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.exceptions.FriendRequestValidationException;

public class FriendRequestValidator implements Validator<FriendRequest> {
    /** valideaza un obiect de tip friendrequest
     * @param entity obiectul de validat
     * @throws FriendRequestValidationException daca mesajul nu este valid
     */
    @Override
    public void validate(FriendRequest entity) throws ValidationException{
        String error="";
        if(entity.getUser1()==null ||entity.getUser2()==null )
            error+="User cannot be null\n";
        if(entity.getStatus().toString().compareTo("PENDING")!=0&&entity.getStatus().toString().compareTo("APPROVED")!=0&&entity.getStatus().toString().compareTo("REJECTED")!=0)
            error+="Status is invalid\n";
        if(entity.getYear()<1900)
            error+="Year cannot be smaller than 1900!\n";
        if(entity.getMonth()<1 || entity.getMonth()>12)
            error+="Month has to be between 1 and 12!\n";
        if(entity.getDay()<1 || entity.getDay()>32)
            error+="Day has to be between 1 and 32!\n";
        if(!error.isEmpty())
            throw new FriendRequestValidationException(error);
    }
}
