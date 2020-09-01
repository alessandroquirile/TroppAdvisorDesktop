package dao_implementations;

import dao_interfaces.AccountDAO;
import models.Account;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class AccountDAO_Cognito implements AccountDAO {

    @Override
    public boolean login(Account account) {
        return false;
        // codice per il login
    }
}