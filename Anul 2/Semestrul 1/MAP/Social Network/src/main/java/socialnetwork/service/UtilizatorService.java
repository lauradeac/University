package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.domain.exceptions.UserValidationException;
import socialnetwork.repository.Repository;
//import sun.nio.ch.Util;

import java.util.*;

public class UtilizatorService  {
    private Repository<Long, Utilizator> repo;

    public Repository<Long,Utilizator> getRepo() {
        return repo;
    }

    public PrietenieService servicep;

    public Utilizator getUser(Long id){
        return repo.findOne(id);
    }

    public UtilizatorService(Repository<Long, Utilizator> repo) {
        this.repo = repo;

    }

    /**
     * Adauga un utilizator
     * @param messageTask utilizatorul de adaugat
     * @return utilizatorul adaugat
     */
    public Utilizator addUtilizator(Utilizator messageTask) {
       try{
        Utilizator task = repo.save(messageTask);
        return task;
       } catch(UserValidationException e){throw new ServiceException(e.getMessage());}
    }

    /**
     * sterge un utilizator
     * @param userName - username-ul utilizatorului de sters
     * @return utilizatorul sters
     */
    public Utilizator removeUtilizator(String userName, Repository<Tuple<Long, Long>, Prietenie> repoP){
        Utilizator del = getUtilizatorByUserName(userName);
        long idToDelete = 0;
        try {
                    idToDelete = del.getId();
                   //servicep.userFriendships(idToDelete);
                   List<Prietenie> prietenieList=new ArrayList<>();
                    for(Prietenie p: repoP.findAll())
                    {
                        if(p.getId().getRight()==idToDelete || p.getId().getLeft()==idToDelete)
                        {
                            prietenieList.add(p);
                        }
                    }

            if(del == null){
                try {
                    throw new ServiceException("Username-ul dat nu exista!");
                } catch (ServiceException e) {
                    System.out.println(e + "\n");
                }
            }
            else{
                //idToDelete = del.getId();
                repo.delete(idToDelete);
            }

            if (del!=null)
                    {
                        for(Prietenie p: prietenieList)
                            repoP.delete(p.getId());
                    }
            else
                throw new ServiceException("Acest utilizator nu exista!");
            return del;
        }

        catch(IllegalArgumentException e) {throw new ServiceException(e.getMessage());} //null id//

    }

    /**
     * @return toate obiectele de tip Utilizator
     */
    public Iterable<Utilizator> getAll(){
        return repo.findAll();
    }

    /**
     * @param id id-ul utilizatorului cautat
     * @return obiectul de tip utiliator cu id ul dat
     */

    public Utilizator getUtilizator(Long id){
        for(Utilizator u:repo.findAll())
            if(u.getId()==id) return u;
        return null;
    }

    /**
     * Metoda care face cautarea unui Utilizator dupa username
     * @param userName - String
     * @return null - daca nu exista
     *         user - daca exista
     */

    public Utilizator getUtilizatorByUserName(String userName) {
        Iterable<Utilizator> users = repo.findAll();
        Utilizator user = null;
        for (Utilizator currentUser: users){
            if (currentUser.getUserName().equals(userName)){
                user = currentUser;
                break;
            }
        }
        return user;
    }

    /**
     * Metoda care verifica daca un utilizator exista, dupa username-ul lui
     * @param username - String
     * @return boolean
     */
    public boolean existUtilizator(String username){
        boolean exist = false;
        if(getUtilizatorByUserName(username)!= null){
            //daca il gaseste
            exist = true;
        }
        return exist;
    }


    /**
     * @return id ul ulitmului utilizator din fisier
     */
    public Long getLastID(){
        long id = 0;
        for(Utilizator u: repo.findAll())
            if(id<u.getId())
                id=u.getId();
        return id;
    }

    /**
     * parcurgere in adancime
     */
    public void DFS(Utilizator user, List<Utilizator> visited){
        visited.add(user);
        for(Utilizator u:user.getFriends()){
            if(!visited.contains(u)){
                DFS(u,visited);
            }
        }
    }
    /**
     * @return compConexe=nr de comp conexe
     */
    public int NumarComunitati(){
        int compConexe=0;
        List<Utilizator> visited=new ArrayList<Utilizator>();
        for(Utilizator user:getAll()){
            if(!visited.contains(user)){ //not visited
                DFS(user,visited);
                compConexe++;

            }

        }
        return compConexe;
    }

    /**
     * parcurgere in adancime
     */
    public void dfs(Utilizator x, Map<Long,Long> vizitat, Map<Long,Long>  distanta, Long dist) {
        dist++;
        distanta.replace(x.getId(),dist);
        vizitat.replace(x.getId(), (long) 1);
        for(Utilizator ele:x.getFriends()) {
            if(vizitat.get(ele.getId()) == 0) {
                dfs(ele,vizitat,distanta,dist);
            }
        }

    }

    /**
     * afiseaza cea mai sociabila comunitate( comp. conexa cu cel mai lung drum si lungimea lui)
     * @return max=lungimea drumului maxim
     */
    public Integer ComunitateSociabila(){
        Map<Long,Long> distanta = new HashMap<Long,Long>();
        Map<Long,Long>  vizitat = new HashMap<Long,Long>();
        List<Utilizator> friends = new ArrayList<Utilizator>();
        Integer max = 0;
        if(repo.size()>0) {
            for (Utilizator ele : getAll()) //initializare
            {
                distanta.put(ele.getId(), (long) 0);
                vizitat.put(ele.getId(), (long) 0);
            }

            Utilizator user1 = null; //utilizatorul la care se va termina cel mai lung drum

            for (Utilizator ele : getAll()) {
                dfs(ele, vizitat, distanta, (long) 0); //determin componenta sa conexa si distantele pana la fiecare vf accesibil
                for (Utilizator user : getAll()) {
                    Long id = user.getId();
                    if (distanta.get(id) > max) {
                        max = Math.toIntExact(distanta.get(id)); //lungimea drumului maxim
                        user1 = repo.findOne(id);
                    }
                    vizitat.replace(id, (long) 0); //reinitializez
                    distanta.replace(id, (long) 0);
                }
            }

            dfs(user1, vizitat, distanta, (long) 0);
            for (Utilizator user : getAll()) { //afisez cea mai sociabila comunitate
                Long id = user.getId();
                if (distanta.get(id) > 0)
                    System.out.println(id);
            }
        }

        return max;
    }

}
